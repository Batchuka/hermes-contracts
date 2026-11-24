package io.github.batchuka.hermes.contracts.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.model.Installment;
import io.github.batchuka.hermes.contracts.repository.ContractRepository;

@Service
public class ContractService {

	private final ContractRepository repository;

	public ContractService(ContractRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public Contract issue(Contract contract) {
		BigDecimal payment = ScheduleCalculator.installment(
				contract.getAmount(),
				contract.getMonthlyRate(),
				contract.getTermMonths());
		contract.setInstallment(payment);

		for (Installment item : ScheduleCalculator.schedule(
				contract.getAmount(), contract.getMonthlyRate(), contract.getTermMonths())) {
			item.setContract(contract);
			contract.getSchedule().add(item);
		}

		Contract saved = repository.save(contract);
		return saved;
	}

}