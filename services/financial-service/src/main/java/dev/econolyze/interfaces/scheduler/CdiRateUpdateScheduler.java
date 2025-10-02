package dev.econolyze.interfaces.scheduler;

import dev.econolyze.application.services.CdiService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CdiRateUpdateScheduler {
    @Inject
    CdiService cdiService;

    // Atualização matinal (início do dia)
    @Scheduled(cron = "0 0 8 ? * MON-FRI")
    void morningUpdate() {
        cdiService.updateCdiRate();
    }

    // Atualização meio-dia
    @Scheduled(cron = "0 0 12 ? * MON-FRI")
    void noonUpdate() {
        cdiService.updateCdiRate();
    }

    // Atualização final do dia
    @Scheduled(cron = "0 0 18 ? * MON-FRI")
    void eveningUpdate() {
        cdiService.updateCdiRate();
    }

    // Backup 1x por dia (caso falhe durante o dia)
    @Scheduled(cron = "0 0 23 * * ?")
    void dailyBackup() {
        cdiService.updateCdiRate();
    }
}
