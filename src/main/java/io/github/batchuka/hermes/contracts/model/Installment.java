package io.github.batchuka.hermes.contracts.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "installments")
public class Installment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "contract_id")
	@JsonIgnore
	private Contract contract;

	private Integer number;
	private LocalDate dueDate;
	private BigDecimal interest;
	private BigDecimal principal;
	private BigDecimal balance;

	public Long getId() { return id; }

	public Contract getContract() { return contract; }
	public void setContract(Contract contract) { this.contract = contract; }

	public Integer getNumber() { return number; }
	public void setNumber(Integer number) { this.number = number; }

	public LocalDate getDueDate() { return dueDate; }
	public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

	public BigDecimal getInterest() { return interest; }
	public void setInterest(BigDecimal interest) { this.interest = interest; }

	public BigDecimal getPrincipal() { return principal; }
	public void setPrincipal(BigDecimal principal) { this.principal = principal; }

	public BigDecimal getBalance() { return balance; }
	public void setBalance(BigDecimal balance) { this.balance = balance; }

}