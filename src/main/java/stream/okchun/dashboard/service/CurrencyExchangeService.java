package stream.okchun.dashboard.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyExchangeService {
	private final HashMap<String, BigDecimal> exchangeRate;

	@PostConstruct
	public void init(){
		exchangeRate.put("USD", BigDecimal.ONE);
		exchangeRate.put("KRW", BigDecimal.ONE.divide(BigDecimal.valueOf(1472), 10, RoundingMode.HALF_EVEN));
	}
	public CurrencyExchangeService() {
		exchangeRate = new HashMap<>();
	}

	private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
		// KRWP = KRW-Point. 이런식으로 4자리가 있으면 3자리로 짜르면 됨. (포인트와 금액을 구분하기 위함)
		String from = fromCurrency.trim().toUpperCase().substring(0, 3);
		String to = toCurrency.trim().toUpperCase().substring(0, 3);

		// 실제로 서비스 할 때는 Currency Exchange Rate API를 들고와야 함. 지금은 적당히 USD - KRW만 둠.
		return exchangeRate.get(from).divide(exchangeRate.get(to), RoundingMode.HALF_EVEN);
	}

	public BigDecimal exchange(String fromCurrency, String toCurrency,BigDecimal fromAmount) {
		BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);

		return fromAmount.multiply(rate);
	}

	public BigDecimal exchange(String toCurrency,
							   HashMap<String, BigDecimal> fromHashMap) {
		BigDecimal amount = BigDecimal.ZERO;

		for (Map.Entry<String, BigDecimal> v :  fromHashMap.entrySet()) {
			amount.add(this.exchange(v.getKey(), toCurrency, v.getValue()));
		}

		return amount;
	}
}
