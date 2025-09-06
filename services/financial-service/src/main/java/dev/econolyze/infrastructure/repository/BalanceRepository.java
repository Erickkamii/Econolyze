package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Balance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {

}
