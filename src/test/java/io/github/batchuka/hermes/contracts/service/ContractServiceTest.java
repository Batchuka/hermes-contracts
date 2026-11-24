package io.github.batchuka.hermes.contracts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.model.Installment;
import io.github.batchuka.hermes.contracts.repository.ContractRepository;

class ContractServiceTest {

	ContractRepository repository = mock(ContractRepository.class);
	ContractService service = new ContractService(repository);

	private Contract newContract() {
		Contract contract = new Contract();
		contract.setFarmer("Joao");
		contract.setAmount(new BigDecimal("10000"));
		contract.setTermMonths(12);
		contract.setMonthlyRate(new BigDecimal("0.02"));
		return contract;
	}

	@Test
	void previewSavesNothing() {
		service.preview(newContract());

		verifyNoInteractions(repository);
	}

	@Test
	void previewReturnsTheSameScheduleThatIssueStores() {
		given(repository.save(any(Contract.class))).willAnswer(call -> call.getArgument(0));

		Contract previewed = service.preview(newContract());
		Contract issued = service.issue(newContract());

		assertThat(previewed.getInstallment()).isEqualByComparingTo(issued.getInstallment());
		assertThat(previewed.getSchedule()).hasSameSizeAs(issued.getSchedule());
		for (int i = 0; i < previewed.getSchedule().size(); i++) {
			Installment p = previewed.getSchedule().get(i);
			Installment s = issued.getSchedule().get(i);
			assertThat(p.getNumber()).isEqualTo(s.getNumber());
			assertThat(p.getDueDate()).isEqualTo(s.getDueDate());
			assertThat(p.getInterest()).isEqualByComparingTo(s.getInterest());
			assertThat(p.getPrincipal()).isEqualByComparingTo(s.getPrincipal());
			assertThat(p.getBalance()).isEqualByComparingTo(s.getBalance());
		}
	}
}