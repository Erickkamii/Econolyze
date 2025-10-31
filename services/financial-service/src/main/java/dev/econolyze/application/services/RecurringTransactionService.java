package dev.econolyze.application.services;

import dev.econolyze.application.dto.*;
import dev.econolyze.application.mapper.RecurrencyTemplateMapper;
import dev.econolyze.application.mapper.TransactionMapper;
import dev.econolyze.domain.entity.RecurringTemplate;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.infrastructure.repository.RecurrencyTemplateRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecurringTransactionService {
    @Inject
    RecurrencyTemplateRepository recurrencyTemplateRepository;
    @Inject
    TransactionRepository transactionRepository;
    @Inject
    RecurrencyTemplateMapper recurrencyTemplateMapper;
    @Inject
    TransactionMapper transactionMapper;

    @Transactional
    public RecurrencyTemplateDTO createRecurring(CreateRecurringRequest request) {
        RecurrencyTemplateDTO dto = mapRequestToDTO(request);
        return createRecurringFromDTO(dto);
    }

    @Transactional
    public RecurrencyTemplateDTO createRecurringFromDTO(RecurrencyTemplateDTO dto) {
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

        recurrencyTemplateRepository.persist(template);
        return recurrencyTemplateMapper.mapToDTO(template);
    }

    private RecurrencyTemplateDTO mapRequestToDTO(CreateRecurringRequest request) {
        LocalDate effectiveStartDate = request.startDate() != null
                ? request.startDate()
                : LocalDate.now();

        return new RecurrencyTemplateDTO(
                null,
                getCurrentUserId(),
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

    @Scheduled(cron = "0 0 1 * * ?") // 01:00 todo dia
    @Transactional
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();
        List<RecurringTemplate> dueTemplates = recurrencyTemplateRepository
                .findActiveWithNextOccurrenceBefore(today);

        for (RecurringTemplate template : dueTemplates) {
            if (shouldProcess(template)) {
                createTransactionFromTemplate(template);
                updateNextOccurrence(template);
            }
        }
    }

    private void createTransactionFromTemplate(RecurringTemplate template) {
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

        transactionRepository.persist(transaction);

        template.setTimesProcessed(template.getTimesProcessed() + 1);

        if (template.getMaxOccurrences() != null
                && template.getTimesProcessed() >= template.getMaxOccurrences()) {
            template.setIsActive(false);
        }
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

    @Transactional
    public RecurrencyTemplateDTO updateTemplate(Long templateId, UpdateRecurringRequest request) {
        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        if (request.amount() != null) {
            template.setAmount(request.amount());
        }
        if (request.description() != null) {
            template.setDescription(request.description());
        }
        if (request.category() != null) {
            template.setCategory(request.category());
        }
        if (request.paymentMethod() != null) {
            template.setMethod(request.paymentMethod());
        }
        if (request.endDate() != null) {
            template.setEndDate(request.endDate());
        }
        if (request.maxOccurrences() != null) {
            template.setMaxOccurrences(request.maxOccurrences());
        }

        return recurrencyTemplateMapper.mapToDTO(template);
    }

    @Transactional
    public RecurrencyTemplateDTO toggleActive(Long templateId) {
        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        template.setIsActive(!template.getIsActive());
        return recurrencyTemplateMapper.mapToDTO(template);
    }

    @Transactional
    public void deleteTemplate(Long templateId) {
        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        recurrencyTemplateRepository.delete(template);
    }

    public List<RecurrencyTemplateDTO> getAllTemplatesByUserId(Long userId) {
        List<RecurringTemplate> templates = recurrencyTemplateRepository.findActiveByUserId(userId);
        return templates.stream()
                .map(recurrencyTemplateMapper::mapToDTO)
                .toList();
    }

    public PagedResponse<TransactionDTO> getTransactionHistory(Long templateId, int page, int pageSize) {
        return PagedResponse.fromPanacheQuery(
                transactionRepository.findPagedByRecurringTemplateId(templateId, page, pageSize),
                page,
                pageSize,
                transactionMapper::mapToDTO
        );
    }

    public PagedResponse<LocalDate> previewNextRecurrencies(
            Long templateId,
            int page,
            int pageSize,
            Integer maxResults) {

        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        int limit = (maxResults != null && maxResults > 0 && maxResults <= 5000)
                ? maxResults
                : 1000;

        List<LocalDate> allDates = generateAllOccurrences(template, limit);
        return paginateDates(allDates, page, pageSize);
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

    public RecurrencyTemplateDTO getTemplateById(Long templateId) {
        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        return recurrencyTemplateMapper.mapToDTO(template);
    }

    public RecurrencySummaryDTO getRecurrencySummary(Long templateId) {
        RecurringTemplate template = recurrencyTemplateRepository.findByIdOptional(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        List<LocalDate> allDates = generateAllOccurrences(template, 5000);

        int totalOccurrences = allDates.size();
        int timesProcessed = template.getTimesProcessed() != null ? template.getTimesProcessed() : 0;
        int remaining = totalOccurrences - timesProcessed;

        BigDecimal totalAmount = template.getAmount().multiply(new BigDecimal(totalOccurrences));
        BigDecimal paidAmount = template.getAmount().multiply(new BigDecimal(timesProcessed));
        BigDecimal remainingAmount = template.getAmount().multiply(new BigDecimal(remaining));

        return new RecurrencySummaryDTO(
                template.getId(),
                template.getAmount(),
                template.getFrequency(),
                totalOccurrences,
                remaining,
                totalAmount,
                paidAmount,
                remainingAmount,
                allDates.isEmpty() ? null : allDates.getFirst(),
                allDates.isEmpty() ? null : allDates.getLast(),
                template.getNextOccurrence(),
                template.getIsActive()
        );
    }
    // TODO: Implementar com SecurityContext/JWT
    private Long getCurrentUserId() {
        return 1L; // mock por enquanto
    }
}