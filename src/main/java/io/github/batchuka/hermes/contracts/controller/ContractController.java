package io.github.batchuka.hermes.contracts.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.service.ContractService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contracts")
public class ContractController {

	private final ContractService service;

	public ContractController(ContractService service) {
		this.service = service;
	}

	@PostMapping
	public Contract create(@Valid @RequestBody ContractRequest request) {
		Contract contract = new Contract();
		contract.setFarmer(request.farmer());
		contract.setAmount(request.amount());
		contract.setTermMonths(request.termMonths());
		contract.setMonthlyRate(request.monthlyRate());
		return service.issue(contract);
	}

}