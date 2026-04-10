package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Payment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<Payment> {
    public Uni<List<Payment>> findByTransactionId(Long transactionId) {
        return find("transaction.id", transactionId).list();
    }
}
