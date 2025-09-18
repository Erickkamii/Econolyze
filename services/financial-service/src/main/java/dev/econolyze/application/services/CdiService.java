package dev.econolyze.application.services;

import dev.econolyze.application.dto.CdiRateDTO;
import dev.econolyze.infrastructure.config.BancoCentralConfig;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@ApplicationScoped
public class CdiService {

    @Inject
    @RestClient
    BancoCentralConfig bancoCentralConfig;

    private volatile BigDecimal cachedCdiRate = BigDecimal.valueOf(14.90);
    @Getter
    private volatile LocalDate lastUpdate = LocalDate.now();

    public Uni<BigDecimal> getCurrentCdiRateAsync(){
        return bancoCentralConfig.getLatestCdiRate(1, "json")
                .onItem().transform(this::extractCdiRate)
                .onFailure().recoverWithItem(cachedCdiRate);
    }

    public BigDecimal getCurrentCdiRate(){
        return cachedCdiRate;
    }

    public void updateCdiRate(){
        try{
            List<Map<String, Object>> response = bancoCentralConfig.getLatestCdiRateSync(1, "json");
            if (nonNull(response) && !response.isEmpty()) {
                BigDecimal newRate = extractCdiRate(response);
                this.cachedCdiRate = newRate;
                this.lastUpdate = LocalDate.now();
                System.out.println("CDI atualizado para: " + newRate + "%");
            } else {
                System.out.println("Resposta vazia da API do Banco Central");
            }
        } catch (Exception e){
            System.err.println("Erro ao atualizar CDI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BigDecimal extractCdiRate(List<Map<String, Object>> response){
        if(nonNull(response) && !response.isEmpty()){
            Map<String, Object> latestRate = response.getFirst();
            double rate = Double.parseDouble(latestRate.get("valor").toString());
            return BigDecimal.valueOf(rate);
        }
        return cachedCdiRate;
    }

    public CdiRateDTO getCdiRateInfo(){
        return CdiRateDTO.builder()
                .currentRate(this.cachedCdiRate)
                .lastUpdate(this.lastUpdate)
                .source("Banco Central do Brasil")
                .build();
    }
}