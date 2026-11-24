package io.github.batchuka.hermes.contracts.service;

public class ContractNotFoundException extends RuntimeException {

	public ContractNotFoundException(Long id) {
		super("Contract " + id + " not found");
	}

}