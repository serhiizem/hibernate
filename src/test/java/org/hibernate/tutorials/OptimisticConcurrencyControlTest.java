package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.inheritance.single_table.BankAccount;
import org.junit.Test;

import javax.persistence.OptimisticLockException;

import static org.hibernate.utils.Constants.BANK_ACCOUNT_ID;
import static org.hibernate.utils.Constants.ORDER_ID;

public class OptimisticConcurrencyControlTest extends AbstractDaoTest {

    @Test(expected = OptimisticLockException.class)
    public void shouldThrowExceptionIfVersionOfCurrentlyUpdatingEntityDoesNotMatch() {
        Order order = em.find(Order.class, ORDER_ID);
        Order originalOrder = new Order(order);

        updateOrderNameInSeparateTransaction();
        order.setName("Updated");

        flushWithCleanup(em, () -> revertOrderToOriginalState(originalOrder));
    }

    @Test(expected = OptimisticLockException.class)
    public void shouldThrowExceptionIfHibernateOptimisticLockingIsViolated() {
        BankAccount bankAccount = em.find(BankAccount.class, BANK_ACCOUNT_ID);
        BankAccount originalBankAccount = new BankAccount(bankAccount);

        updateBankNameInSeparateTransaction();
        bankAccount.setBankName("Bank has changed");

        flushWithCleanup(em, () -> revertBankAccountToOriginalState(originalBankAccount));
    }
}
