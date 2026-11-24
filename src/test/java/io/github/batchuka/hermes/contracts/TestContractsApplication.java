package io.github.batchuka.hermes.contracts;

import org.springframework.boot.SpringApplication;

import io.github.batchuka.hermes.contracts.ContractsApplication;

public class TestContractsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContractsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
