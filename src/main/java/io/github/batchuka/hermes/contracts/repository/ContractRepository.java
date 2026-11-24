package io.github.batchuka.hermes.contracts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.batchuka.hermes.contracts.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}