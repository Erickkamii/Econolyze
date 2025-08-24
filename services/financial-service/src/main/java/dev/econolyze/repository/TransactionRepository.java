package dev.econolyze.repository;

import dev.econolyze.entity.Transaction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    public Transaction findTransactionById(Long id){
        return find("id", id).firstResult();
    }

    public List<Transaction> findAllTransactionsByUserId(Long userId){
        return list("userId", userId);
    }

    public List<Transaction> findAllTransactionsByUserIdAndCategory(Long userId, String category){
        return list("userId", userId, "category", category);
    }
}
