package io.github.batchuka.hermes.contracts.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.github.batchuka.hermes.contracts.model.Installment;

public class ScheduleCalculator {

	public static double installment(double amount, double rate, int months) {
		return amount * rate / (1 - Math.pow(1 + rate, -months));
	}

	public static List<Installment> schedule(BigDecimal amount, BigDecimal monthlyRate, int months) {
		double rate = monthlyRate.doubleValue();
		double payment = installment(amount.doubleValue(), rate, months);
		double balance = amount.doubleValue();

		List<Installment> schedule = new ArrayList<>();
		for (int n = 1; n <= months; n++) {
			double interest = balance * rate;
			double principal = payment - interest;
			balance -= principal;

			Installment item = new Installment();
			item.setNumber(n);
			item.setDueDate(LocalDate.now().plusMonths(n));
			item.setInterest(BigDecimal.valueOf(interest));
			item.setPrincipal(BigDecimal.valueOf(principal));
			item.setBalance(BigDecimal.valueOf(balance));
			schedule.add(item);
		}
		return schedule;
	}

}