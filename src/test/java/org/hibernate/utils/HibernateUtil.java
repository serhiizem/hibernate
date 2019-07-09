package org.hibernate.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.inheritance.single_table.BankAccount;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.hibernate.utils.Constants.BANK_ACCOUNT_ID;
import static org.hibernate.utils.Constants.ORDER_ID;

@Slf4j
@AllArgsConstructor
public class HibernateUtil {

    private EntityManagerFactory emf;

    public static <T> List<T> getAndCast(List list) {
        //noinspection unchecked
        return (List<T>) list;
    }

    public void updateOrderNameInSeparateTransaction() {
        executeInTransaction(entityManager -> {
            Order order = entityManager.find(Order.class, ORDER_ID);
            order.setName("Updated in other thread");
        });
    }

    public void revertOrderToOriginalState(Order order) {
        executeInTransaction(entityManager -> {
            Long orderId = order.getId();
            String originalName = order.getName();
            Order currentOrder = entityManager.find(Order.class, orderId);
            currentOrder.setName(originalName);
        });
    }

    public void updateBankNameInSeparateTransaction() {
        executeInTransaction(entityManager -> {
            BankAccount bankAccount = entityManager.find(BankAccount.class, BANK_ACCOUNT_ID);
            bankAccount.setBankName("Other bank");
        });
    }

    public void revertBankAccountToOriginalState(BankAccount originalBankAccount) {
        executeInTransaction(entityManager -> {
            Long accountId = originalBankAccount.getId();
            String originalBankName = originalBankAccount.getBankName();
            BankAccount bankAccount = entityManager.find(BankAccount.class, accountId);
            bankAccount.setBankName(originalBankName);
        });
    }

    private void executeInTransaction(Consumer<EntityManager> function) {
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
