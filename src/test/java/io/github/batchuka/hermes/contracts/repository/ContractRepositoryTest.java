package io.github.batchuka.hermes.contracts.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.model.Installment;
import io.github.batchuka.hermes.contracts.service.ScheduleCalculator;
import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // we're using a real instance
@Testcontainers
class ContractRepositoryTest {

	@Container
	@ServiceConnection
	static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

	@Autowired
	ContractRepository repository;

	@Autowired
	EntityManager entityManager;

	@Test
	void savesAContractTogetherWithItsInstallments() {
		var contract = new Contract();
		contract.setFarmer("Joao");
		contract.setAmount(new BigDecimal("10000.00"));
		contract.setTermMonths(12);
		contract.setMonthlyRate(new BigDecimal("0.02"));
		contract.setInstallment(new BigDecimal("945.60"));
		for (Installment item : ScheduleCalculator.schedule(
				contract.getAmount(), contract.getMonthlyRate(), contract.getTermMonths())) {
			item.setContract(contract);
			contract.getSchedule().add(item);
		}

		var saved = repository.save(contract);
		entityManager.flush();
		entityManager.clear();
		var found = repository.findById(saved.getId()).orElseThrow();

		assertThat(found.getFarmer()).isEqualTo("Joao");
		assertThat(found.getInstallment()).isEqualByComparingTo("945.60");
		assertThat(found.getSchedule()).hasSize(12);
		assertThat(found.getSchedule().get(0).getContract().getId()).isEqualTo(saved.getId());
	}
}