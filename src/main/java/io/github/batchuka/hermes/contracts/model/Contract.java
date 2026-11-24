package io.github.batchuka.hermes.contracts.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "contracts")
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String farmer;
	private BigDecimal amount;
	private Integer termMonths;
	private BigDecimal monthlyRate;
	private BigDecimal installment;

	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
	private List<Installment> schedule = new ArrayList<>();

	public Long getId() { return id; }

	public String getFarmer() { return farmer; }
	public void setFarmer(String farmer) { this.farmer = farmer; }

	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }

	public Integer getTermMonths() { return termMonths; }
	public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }

	public BigDecimal getMonthlyRate() { return monthlyRate; }
	public void setMonthlyRate(BigDecimal monthlyRate) { this.monthlyRate = monthlyRate; }

	public BigDecimal getInstallment() { return installment; }
	public void setInstallment(BigDecimal installment) { this.installment = installment; }

	public List<Installment> getSchedule() { return schedule; }

}