package io.github.batchuka.hermes.contracts.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.model.Installment;
import io.github.batchuka.hermes.contracts.repository.ContractRepository;
import io.github.batchuka.hermes.contracts.service.ScheduleCalculator;

@RestController
@RequestMapping("/contracts")
public class ContractController {

	private final ContractRepository repository;

	public ContractController(ContractRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public Contract create(@RequestBody Contract contract) {
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
		return repository.save(contract);
	}

}