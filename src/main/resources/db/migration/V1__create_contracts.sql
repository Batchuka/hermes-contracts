CREATE TABLE contracts (
  id bigint NOT NULL AUTO_INCREMENT,
  farmer varchar(255) NOT NULL,
  amount decimal(38,2) NOT NULL,
  term_months int NOT NULL,
  monthly_rate decimal(10,6) NOT NULL,
  installment decimal(38,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE installments (
  id bigint NOT NULL AUTO_INCREMENT,
  contract_id bigint NOT NULL,
  number int NOT NULL,
  due_date date NOT NULL,
  interest decimal(38,2) NOT NULL,
  principal decimal(38,2) NOT NULL,
  balance decimal(38,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (contract_id) REFERENCES contracts (id)
);