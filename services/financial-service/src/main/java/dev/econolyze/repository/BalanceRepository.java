package dev.econolyze.repository;

import dev.econolyze.entity.Balance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {

}
