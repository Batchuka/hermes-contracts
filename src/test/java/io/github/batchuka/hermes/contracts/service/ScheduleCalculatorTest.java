package io.github.batchuka.hermes.contracts.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.batchuka.hermes.contracts.model.Installment;

class ScheduleCalculatorTest {

	private static final BigDecimal AMOUNT = new BigDecimal("10000");
	private static final BigDecimal RATE = new BigDecimal("0.02");

	@Test
	void theInstallmentFollowsThePriceTable() {
		BigDecimal installment = ScheduleCalculator.installment(AMOUNT, RATE, 12);

		assertThat(installment).isEqualByComparingTo("945.60");
	}

	@Test
	void theScheduleHasOneInstallmentForEachMonthOfTheTerm() {
		List<Installment> schedule = ScheduleCalculator.schedule(AMOUNT, RATE, 12);

		assertThat(schedule).hasSize(12);
		assertThat(schedule.get(0).getNumber()).isEqualTo(1);
		assertThat(schedule.get(11).getNumber()).isEqualTo(12);
	}

	@Test
	void theFirstInstallmentPaysTheInterestOnTheWholeAmount() {
		Installment first = ScheduleCalculator.schedule(AMOUNT, RATE, 12).get(0);

		assertThat(first.getInterest()).isEqualByComparingTo("200.00");
		assertThat(first.getPrincipal()).isEqualByComparingTo("745.60");
		assertThat(first.getBalance()).isEqualByComparingTo("9254.40");
	}

	@Test
	void theLastInstallmentClosesTheBalanceAtZero() {
		Installment last = ScheduleCalculator.schedule(AMOUNT, RATE, 12).get(11);

		assertThat(last.getBalance()).isEqualByComparingTo("0.00");
		assertThat(last.getPrincipal()).isEqualByComparingTo("927.01");
	}

	@ParameterizedTest
	@CsvSource({
			"10000, 0.02, 12",
			"1234.56, 0.0173, 7",
			"250000, 0.011, 60"
	})
	void thePrincipalAlwaysAddsUpToTheAmount(String amount, String rate, int months) {
		BigDecimal total = ScheduleCalculator.schedule(new BigDecimal(amount), new BigDecimal(rate), months)
				.stream()
				.map(Installment::getPrincipal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		assertThat(total).isEqualByComparingTo(amount);
	}
}