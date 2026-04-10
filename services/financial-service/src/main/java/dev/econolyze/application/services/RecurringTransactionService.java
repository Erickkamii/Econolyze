package dev.econolyze.application.services;

import dev.econolyze.application.dto.*;
import dev.econolyze.application.dto.request.CreateRecurringRequest;
import dev.econolyze.application.dto.request.UpdateRecurringRequest;
import dev.econolyze.application.dto.response.RecurrencySummaryResponse;
import dev.econolyze.application.dto.response.RecurringTemplateResponse;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.mapper.RecurrencyTemplateMapper;
import dev.econolyze.application.mapper.TransactionMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.RecurringTemplate;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.infrastructure.repository.RecurrencyTemplateRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class RecurringTransactionService {
    @Inject
    RecurrencyTemplateRepository recurrencyTemplateRepository;
    @Inject
    TransactionRepository transactionRepository;
    @Inject
    RecurrencyTemplateMapper recurrencyTemplateMapper;
    @Inject
    TransactionMapper transactionMapper;
    @Inject
    UserContext userContext;

    @WithTransaction
    public Uni<RecurringTemplateResponse> createRecurring(CreateRecurringRequest request) {
        RecurrencyTemplateDTO dto = mapRequestToDTO(request);
        return createRecurringFromDTO(dto);
    }

    @WithTransaction
    public Uni<RecurringTemplateResponse> createRecurringFromDTO(RecurrencyTemplateDTO dto) {
        RecurringTemplate template = RecurringTemplate.builder()
                .userId(dto.userId())
                .amount(dto.amount())
                .type(dto.type())
                .category(dto.category())
                .method(dto.method())
                .frequency(dto.frequency())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .maxOccurrences(dto.maxOccurrences())
                .nextOccurrence(dto.startDate())
                .description(dto.description())
                .timesProcessed(0)
                .isActive(true)
                .build();
        return recurrencyTemplateRepository.persist(template)
                .map(recurrencyTemplateMapper::mapToResponse);
    }

    private RecurrencyTemplateDTO mapRequestToDTO(CreateRecurringRequest request) {
        LocalDate effectiveStartDate = request.startDate() != null
                ? request.startDate()
                : LocalDate.now();

        return new RecurrencyTemplateDTO(
                null,
                userContext.getUserId(),
                request.amount(),
                request.type(),
                request.category(),
                request.paymentMethod(),
                request.description(),
                request.frequency(),
                effectiveStartDate,
                request.endDate(),
                request.maxOccurrences(),
                effectiveStartDate,
                0,
                true,
                null
        );
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processRecurringTransactions() {
        doProcess().await().indefinitely();
    }

    @WithTransaction
    public Uni<Void> doProcess(){
        LocalDate today = LocalDate.now();
        return recurrencyTemplateRepository.findActiveWithNextOccurrenceBefore(today)
                .flatMap(templates -> {
                    List<Uni<Void>> tasks = templates.stream()
                            .filter(this::shouldProcess)
                            .map(template ->
                                    createTransactionFromTemplate(template)
                                            .invoke(ignored -> updateNextOccurrence(template))
                            )
                            .toList();
                    if(tasks.isEmpty()) return Uni.createFrom().voidItem();
                    return Uni.combine().all().unis(tasks).discardItems();
                });
    }

    private Uni<Void> createTransactionFromTemplate(RecurringTemplate template) {
        Transaction transaction = Transaction.builder()
                .userId(template.getUserId())
                .amount(template.getAmount())
                .type(template.getType())
                .category(template.getCategory())
                .description(template.getDescription() + " (Recorrente)")
                .date(template.getNextOccurrence())
                .recurringTemplateId(template.getId())
                .isRecurring(true)
                .build();

        template.setTimesProcessed(template.getTimesProcessed() + 1);

        if (template.getMaxOccurrences() != null
                && template.getTimesProcessed() >= template.getMaxOccurrences()) {
            template.setIsActive(false);
        }

        return transactionRepository.persist(transaction).replaceWithVoid();
    }

    private void updateNextOccurrence(RecurringTemplate template) {
        LocalDate next = calculateNextDate(
                template.getNextOccurrence(),
                template.getFrequency()
        );

        if (next == null) {
            template.setIsActive(false);
            return;
        }

        if (template.getEndDate() != null && next.isAfter(template.getEndDate())) {
            template.setIsActive(false);
        } else {
            template.setNextOccurrence(next);
        }
    }

    @WithTransaction
    public Uni<RecurringTemplateResponse> updateTemplate(Long templateId, UpdateRecurringRequest request) {
        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .map(template -> {
                    if (request.amount() != null) template.setAmount(request.amount());
                    if (request.description() != null) template.setDescription(request.description());
                    if (request.category() != null) template.setCategory(request.category());
                    if (request.method() != null) template.setMethod(request.method());
                    if (request.endDate() != null) template.setEndDate(request.endDate());
                    if (request.maxOccurrences() != null) template.setMaxOccurrences(request.maxOccurrences());
                    return template;
                }).map(recurrencyTemplateMapper::mapToResponse);
    }

    @WithTransaction
    public Uni<RecurringTemplateResponse> toggleActive(Long templateId) {
        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .map(t -> {
                    t.setIsActive(!t.getIsActive());
                    return t;
                })
                .map(recurrencyTemplateMapper::mapToResponse);
    }

    @WithTransaction
    public Uni<Void> deleteTemplate(Long templateId) {
        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .flatMap(recurrencyTemplateRepository::delete);
    }

    @WithSession
    public Uni<List<RecurringTemplateResponse>> getAllTemplatesByUserId() {
        return recurrencyTemplateRepository.findActiveByUserId(userContext.getUserId())
                .map(t -> t.stream()
                        .map(recurrencyTemplateMapper::mapToResponse)
                        .toList());
    }

    @WithSession
    public Uni<PagedResponse<TransactionResponse>> getTransactionHistory(Long templateId, int page, int pageSize) {
        Uni<List<Transaction>> listUni = transactionRepository.findPagedByRecurringTemplateId(templateId, page, pageSize);
        Uni<Long> countUni = transactionRepository.count("recurringTemplateId = ?1", templateId);
        return PagedResponse.from(listUni, countUni, page, pageSize, transactionMapper::mapToResponse);
    }

    @WithSession
    public Uni<PagedResponse<LocalDate>> previewNextRecurrencies(
            Long templateId,
            int page,
            int pageSize,
            Integer maxResults) {

        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .map(t -> {
                    int limit = (maxResults != null && maxResults > 0 && maxResults<=5000)?maxResults:1000;

                    List<LocalDate> allDates = generateAllOccurrences(t, limit);
                    return paginateDates(allDates, page, pageSize);
                });
    }

    private List<LocalDate> generateAllOccurrences(RecurringTemplate template, int maxResults) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = template.getNextOccurrence();
        int count = 0;

        while (current != null && count < maxResults) {
            dates.add(current);
            current = calculateNextDate(current, template.getFrequency());
            count++;

            if (template.getMaxOccurrences() != null
                    && (template.getTimesProcessed() + count) >= template.getMaxOccurrences()) {
                break;
            }

            if (template.getEndDate() != null
                    && current != null
                    && current.isAfter(template.getEndDate())) {
                break;
            }
        }

        return dates;
    }

    private PagedResponse<LocalDate> paginateDates(List<LocalDate> allDates, int page, int pageSize) {
        int totalElements = allDates.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        if (start >= totalElements) {
            return new PagedResponse<>(List.of(), page, pageSize, 0, 0);
        }

        List<LocalDate> pagedDates = allDates.subList(start, end);
        return new PagedResponse<>(pagedDates, page, pageSize, totalElements, totalPages);
    }

    private boolean shouldProcess(RecurringTemplate template) {
        LocalDate today = LocalDate.now();
        LocalDate nextOccurrence = template.getNextOccurrence();

        if (nextOccurrence == null || nextOccurrence.isAfter(today)) {
            return false;
        }

        if (!template.getIsActive()) {
            return false;
        }

        if (template.getMaxOccurrences() != null
                && template.getTimesProcessed() >= template.getMaxOccurrences()) {
            return false;
        }

        if (template.getEndDate() != null
                && today.isAfter(template.getEndDate())) {
            return false;
        }

        return true;
    }

    private LocalDate calculateNextDate(LocalDate current, RecurrenceFrequency frequency) {
        if (current == null || frequency == null) {
            return null;
        }

        return switch (frequency) {
            case DAILY -> current.plusDays(1);
            case WEEKLY -> current.plusWeeks(1);
            case BIWEEKLY -> current.plusWeeks(2);
            case MONTHLY -> current.plusMonths(1);
            case QUARTERLY -> current.plusMonths(3);
            case SEMIANNUALLY -> current.plusMonths(6);
            case YEARLY -> current.plusYears(1);
            case OTHER, NONE -> null;
        };
    }

    @WithSession
    public Uni<RecurringTemplateResponse> getTemplateById(Long templateId) {
        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .map(recurrencyTemplateMapper::mapToResponse);
    }

    @WithSession
    public Uni<RecurrencySummaryResponse> getRecurrencySummary(Long templateId) {
        return recurrencyTemplateRepository.findById(templateId)
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Template not found: " + templateId))
                .map(t -> {
                    List<LocalDate> allDates = generateAllOccurrences(t, 5000);

                    int totalOccurrences = allDates.size();
                    int timesProcessed = t.getTimesProcessed() != null ? t.getTimesProcessed() : 0;
                    int remaining = totalOccurrences - timesProcessed;

                    BigDecimal totalAmount = t.getAmount().multiply(new BigDecimal(totalOccurrences));
                    BigDecimal paidAmount = t.getAmount().multiply(new BigDecimal(timesProcessed));
                    BigDecimal remainingAmount = t.getAmount().multiply(new BigDecimal(remaining));

                    return new RecurrencySummaryResponse(
                            t.getId(),
                            t.getAmount(),
                            t.getFrequency(),
                            totalOccurrences,
                            remaining,
                            totalAmount,
                            paidAmount,
                            remainingAmount,
                            allDates.isEmpty() ? null : allDates.getFirst(),
                            allDates.isEmpty() ? null : allDates.getLast(),
                            t.getNextOccurrence(),
                            t.getIsActive()
                    );
                });

    }

}