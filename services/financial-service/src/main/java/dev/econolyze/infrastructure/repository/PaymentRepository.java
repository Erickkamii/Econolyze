package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Payment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<Payment> {
    public List<Payment> findByTransactionId(Long transactionId) {
        return find("transaction.id", transactionId).list();
    }
}
