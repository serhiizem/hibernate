package org.hibernate.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tutorials.model.inheritance.single_table.BankAccount;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.hibernate.utils.Constants.BANK_ACCOUNT_ID;
import static org.hibernate.utils.RevertStrategy.HIBERNATE;
import static org.hibernate.utils.RevertStrategy.JDBC;

@Slf4j
@AllArgsConstructor
public class HibernateUtil {

    private EntityManagerFactory emf;
    private JdbcTemplate jdbcTemplate;

    public static <T> List<T> getAndCast(List list) {
        //noinspection unchecked
        return (List<T>) list;
    }

    public void updateBankNameInSeparateTransaction() {
        executeInTransaction(entityManager -> {
            BankAccount bankAccount = entityManager.find(BankAccount.class, BANK_ACCOUNT_ID);
            bankAccount.setBankName("Other bank");
        });
    }

    public void revertBankAccountToOriginalState(BankAccount originalBankAccount) {
        revertBankAccountToOriginalState(originalBankAccount, HIBERNATE);
    }

    public void revertBankAccountToOriginalState(BankAccount originalBankAccount,
                                                 RevertStrategy strategy) {
        if (strategy.equals(HIBERNATE)) {
            executeInTransaction(entityManager -> {
                Long accountId = originalBankAccount.getId();
                String originalBankName = originalBankAccount.getBankName();
                BankAccount bankAccount = entityManager.find(BankAccount.class, accountId);
                bankAccount.setBankName(originalBankName);
            });
        }
        if (strategy.equals(JDBC)) {
            jdbcTemplate.update("UPDATE billing_details SET bank_name = ? WHERE id = ?",
                    originalBankAccount.getBankName(), originalBankAccount.getId());
        }
    }

    public void executeInTransaction(Consumer<EntityManager> function) {
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = emf.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();
            function.accept(entityManager);
            if (!txn.getRollbackOnly()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.isActive()) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
            throw t;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public <T> T executeInTransactionAndReturnResult(Function<EntityManager, T> function) {
        T result;
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = emf.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();
            result = function.apply(entityManager);
            if (!txn.getRollbackOnly()) {
                txn.commit();
            } else {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
        } catch (Throwable t) {
            if (txn != null && txn.isActive()) {
                try {
                    txn.rollback();
                } catch (Exception e) {
                    log.error("Rollback failure", e);
                }
            }
            throw t;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return result;
    }
}
