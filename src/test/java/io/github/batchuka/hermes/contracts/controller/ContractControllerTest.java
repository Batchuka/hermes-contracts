package io.github.batchuka.hermes.contracts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.batchuka.hermes.contracts.model.Contract;
import io.github.batchuka.hermes.contracts.service.ContractNotFoundException;
import io.github.batchuka.hermes.contracts.service.ContractService;

@WebMvcTest(ContractController.class)
class ContractControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	ContractService service;

	@Test
	void issuesAContractFromValidData() throws Exception {
		given(service.issue(any(Contract.class))).willAnswer(call -> call.getArgument(0));

		mockMvc.perform(post("/contracts").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"farmer":"Joao","amount":10000,"termMonths":12,"monthlyRate":0.02}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.farmer").value("Joao"))
				.andExpect(jsonPath("$.termMonths").value(12));
	}

	@Test
	void refusesAContractWithoutAnAmount() throws Exception {
		mockMvc.perform(post("/contracts").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"farmer":"Joao","termMonths":12,"monthlyRate":0.02}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fields.amount").exists());

		verifyNoInteractions(service);
	}

	@Test
	void refusesAMonthlyRateOfZero() throws Exception {
		mockMvc.perform(post("/contracts").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"farmer":"Joao","amount":10000,"termMonths":12,"monthlyRate":0}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fields.monthlyRate").exists());

		verifyNoInteractions(service);
	}

	@Test
	void refusesATermOfZero() throws Exception {
		mockMvc.perform(post("/contracts").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"farmer":"Joao","amount":10000,"termMonths":0,"monthlyRate":0.02}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fields.termMonths").exists());

		verifyNoInteractions(service);
	}

	@Test
	void listsTheContracts() throws Exception {
		var contract = new Contract();
		contract.setFarmer("Joao");
		given(service.list()).willReturn(List.of(contract));

		mockMvc.perform(get("/contracts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].farmer").value("Joao"));
	}

	@Test
	void answersNotFoundForAContractThatDoesNotExist() throws Exception {
		given(service.find(999L)).willThrow(new ContractNotFoundException(999L));

		mockMvc.perform(get("/contracts/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Contract 999 not found"));
	}
}