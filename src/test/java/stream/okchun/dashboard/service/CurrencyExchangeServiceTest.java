package stream.okchun.dashboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CurrencyExchangeServiceTest {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        // Ensure the service is not null before each test
        assertNotNull(currencyExchangeService);
    }

    @Test
    void testExchangeUsdToUsd() {
        BigDecimal fromAmount = new BigDecimal("100.00");
        BigDecimal expectedAmount = new BigDecimal("100.00");
        BigDecimal actualAmount = currencyExchangeService.exchange("USD", "USD", fromAmount);
        assertEquals(expectedAmount.setScale(2, RoundingMode.HALF_EVEN), actualAmount.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void testExchangeUsdToKrw() {
        BigDecimal fromAmount = new BigDecimal("10.00");
        // Using the internal rate 1 USD = 1472 KRW
        BigDecimal expectedAmount = fromAmount.multiply(new BigDecimal("1472.00"));
        BigDecimal actualAmount = currencyExchangeService.exchange("USD", "KRW", fromAmount);
        assertEquals(expectedAmount.setScale(2, RoundingMode.HALF_EVEN), actualAmount.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void testExchangeKrwToUsd() {
        BigDecimal fromAmount = new BigDecimal("14720.00");
        // Using the internal rate 1472 KRW = 1 USD
        BigDecimal expectedAmount = fromAmount.divide(new BigDecimal("1472.00"), RoundingMode.HALF_EVEN);
        BigDecimal actualAmount = currencyExchangeService.exchange("KRW", "USD", fromAmount);
        assertEquals(expectedAmount.setScale(2, RoundingMode.HALF_EVEN), actualAmount.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void testExchangeWithDifferentScale() {
        BigDecimal fromAmount = new BigDecimal("10.12345");
        BigDecimal expectedAmount = fromAmount.multiply(new BigDecimal("1472.00"));
        BigDecimal actualAmount = currencyExchangeService.exchange("USD", "KRW", fromAmount);
        assertEquals(expectedAmount.setScale(5, RoundingMode.HALF_EVEN), actualAmount.setScale(5, RoundingMode.HALF_EVEN));
    }
}
