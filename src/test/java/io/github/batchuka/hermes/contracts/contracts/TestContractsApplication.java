package io.github.batchuka.hermes.contracts.contracts;

import org.springframework.boot.SpringApplication;

public class TestContractsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContractsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
