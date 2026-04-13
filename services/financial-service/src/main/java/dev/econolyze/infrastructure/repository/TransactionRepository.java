package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.TransactionType;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    public Uni<Transaction> findTransactionById(Long id){
        return find("SELECT t FROM Transaction t LEFT JOIN FETCH t.payments WHERE t.id = ?1", id).firstResult();
    }

    public Uni<List<Transaction>> findAllTransactionsByUserIdAndType(Long userId, TransactionType type){
        return find("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.payments WHERE t.userId = ?1 AND t.type = ?2", userId, type).list();
    }

    public Uni<List<Transaction>> findPagedByUserId(Long userId, int page, int size, Sort sort) {
        return find("userId = ?1", sort, userId).page(Page.of(page, size)).list();
    }

    public Uni<List<Transaction>> findFiltered(Long userId, Sort sort, TransactionType type, Category category, int page, int size) {
        if (type != null && category != null) {
            return find("userId = ?1 AND type = ?2 AND category = ?3", sort, userId, type, category).page(Page.of(page,size)).list();
        } else if (type != null) {
            return find("userId = ?1 AND type = ?2", sort, userId, type).page(Page.of(page,size)).list();
        } else if (category != null) {
            return find("userId = ?1 AND category = ?2", sort, userId, category).page(Page.of(page,size)).list();
        } else {
            return find("userId = ?1", sort, userId).page(Page.of(page,size)).list();
        }
    }

    public Uni<Long> countFiltered(Long userId, TransactionType type, Category category) {
        if (type != null && category != null) {
            return count("userId = ?1 AND type = ?2 AND category = ?3", userId, type, category);
        } else if (type != null) {
            return count("userId = ?1 AND type = ?2", userId, type);
        } else if (category != null) {
            return count("userId = ?1 AND category = ?2", userId, category);
        } else {
            return count("userId = ?1", userId);
        }
    }

    public Uni<List<Transaction>> findPagedByRecurringTemplateId(Long recurringTemplateId, int page, int size) {
        return find("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.payments WHERE t.recurringTemplateId = ?1", recurringTemplateId)
                .page(Page.of(page, size)).list();
    }

    public Uni<List<Transaction>> findByIdsWithPayments(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Uni.createFrom().item(List.of());
        }
        return find("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.payments WHERE t.id IN ?1", ids)
                .list()
                .map(unsortedHydrated -> {
                    java.util.Map<Long, Transaction> map = unsortedHydrated.stream()
                            .collect(java.util.stream.Collectors.toMap(Transaction::getId, t -> t));

                    return ids.stream().map(map::get).toList();
                });
    }
}