package io.github.batchuka.hermes.contracts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping
	public List<Contract> list() {
		return service.list();
	}

	@GetMapping("/{id}")
	public Contract get(@PathVariable Long id) {
		return service.find(id);
	}

	@PostMapping
	public Contract create(@Valid @RequestBody ContractRequest request) {
		return service.issue(toContract(request));
	}

	@PostMapping("/preview")
	public Contract preview(@Valid @RequestBody ContractRequest request) {
		return service.preview(toContract(request));
	}

	private Contract toContract(ContractRequest request) {
		Contract contract = new Contract();
		contract.setFarmer(request.farmer());
		contract.setAmount(request.amount());
		contract.setTermMonths(request.termMonths());
		contract.setMonthlyRate(request.monthlyRate());
		return contract;
	}

}