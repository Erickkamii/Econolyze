package dev.econolyze.application.services;

import dev.econolyze.application.dto.CdiRateDTO;
import dev.econolyze.infrastructure.config.BancoCentralConfig;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CdiServiceTest {

    @Mock
    BancoCentralConfig bancoCentralConfig;

    @InjectMocks
    CdiService cdiService;

    @Test
    void getCurrentCdiRateAsync_recoversOnFailure() {
        when(bancoCentralConfig.getLatestCdiRate(1, "json")).thenReturn(Uni.createFrom().failure(new RuntimeException("down")));
        Uni<BigDecimal> uni = cdiService.getCurrentCdiRateAsync();
        BigDecimal result = uni.await().indefinitely();
        assertNotNull(result);
    }

    @Test
    void getCdiRateInfo_returnsDto() {
        CdiRateDTO dto = cdiService.getCdiRateInfo();
        assertNotNull(dto);
        assertEquals(cdiService.getCurrentCdiRate(), dto.getCurrentRate());
    }
}

