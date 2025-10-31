package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    public Transaction findTransactionById(Long id){
        return find("id", id).firstResult();
    }

    public List<Transaction> findAllTransactionsByUserIdAndType(Long userId, TransactionType type){
        return list("userId = ?1 and type =? 2", userId, type);
    }

    public PanacheQuery<Transaction> findPagedByUserId(Long userId, int page, int size) {
        return find("userId", userId).page(Page.of(page, size));
    }

    public PanacheQuery<Transaction> findPagedByUserIdAndCategory(Long userId, Category category, int page, int size) {
        return find("userId = ?1 and category = ?2", userId, category).page(Page.of(page, size));
    }

    public PanacheQuery<Transaction> findPagedByUserIdAndType(Long userId, TransactionType type, int page, int size ) {
        return find("userId = ?1 and type = ?2", userId, type).page(Page.of(page, size));
    }

    public PanacheQuery<Transaction> findPagedByRecurringTemplateId(Long recurringTemplateId, int page, int size) {
        return find("recurringTemplateId = ?1", recurringTemplateId).page(Page.of(page, size));
    }
}
