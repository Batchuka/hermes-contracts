package io.github.batchuka.hermes.contracts.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.github.batchuka.hermes.contracts.model.Installment;

public class ScheduleCalculator {

	public static BigDecimal installment(BigDecimal amount, BigDecimal rate, int months) {
		BigDecimal factor = BigDecimal.ONE.add(rate).pow(months);
		BigDecimal payment = amount.multiply(rate).multiply(factor)
				.divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
		return payment;
	}

	public static List<Installment> schedule(BigDecimal amount, BigDecimal monthlyRate, int months) {
		BigDecimal rate = monthlyRate;
		BigDecimal payment = installment(amount, rate, months);
		BigDecimal balance = amount;

		List<Installment> schedule = new ArrayList<>();
		for (int n = 1; n <= months; n++) {
			BigDecimal interest = balance.multiply(rate).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principal = payment.subtract(interest);
			if (n == months) {
				principal = balance;
			}
			balance = balance.subtract(principal);

			Installment item = new Installment();
			item.setNumber(n);
			item.setDueDate(LocalDate.now().plusMonths(n));
			item.setInterest(interest);
			item.setPrincipal(principal);
			item.setBalance(balance);
			schedule.add(item);
		}
		return schedule;
	}

}