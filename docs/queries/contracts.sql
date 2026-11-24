-- hermes-contracts · queries to inspect the contracts database
-- Connection: localhost:3306, schema `contracts`, user `contracts`.
-- Run one statement at a time (Ctrl+Enter in DBeaver).

-- 1. The tables exist (Flyway V1)
SHOW TABLES;

-- 2. All contracts, newest first
SELECT * FROM contracts ORDER BY id DESC;

-- 3. The schedule of one contract (change the id)
SELECT number, due_date, interest, principal, balance
FROM installments
WHERE contract_id = 1
ORDER BY number;

-- 4. v1 defect: does the principal add up to the amount?
-- The difference must be 0.00. In v1 it is not, because each value
-- is rounded to cents on its own. v2 (hermes-contracts#1) fixes it.
SELECT c.id,
       c.amount,
       SUM(i.principal)            AS principal_sum,
       SUM(i.principal) - c.amount AS difference
FROM contracts c
JOIN installments i ON i.contract_id = c.id
GROUP BY c.id, c.amount;

-- 5. Contracts without installments (must always return 0 rows, extension 4a)
SELECT c.id
FROM contracts c
LEFT JOIN installments i ON i.contract_id = c.id
WHERE i.id IS NULL;

-- 6. Clean the data while testing (careful: it deletes everything)
-- DELETE FROM installments;
-- DELETE FROM contracts;