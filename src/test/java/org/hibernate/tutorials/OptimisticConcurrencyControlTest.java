package org.hibernate.tutorials;

import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.RequestStatus;
import org.hibernate.tutorials.model.User;
import org.hibernate.tutorials.model.inheritance.single_table.BankAccount;
import org.hibernate.tutorials.model.payments.MonetaryAmount;
import org.hibernate.utils.CauseRethrowingExecutable;
import org.hibernate.utils.RevertStrategy;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.math.BigDecimal.valueOf;
import static org.hibernate.tutorials.model.RequestStatus.CONFIRMED;
import static org.hibernate.tutorials.model.RequestStatus.DELIVERED;
import static org.hibernate.utils.Constants.*;
import static org.hibernate.utils.JdbcUtils.GET_LARGEST_ORDER_ID;
import static org.hibernate.utils.JdbcUtils.GET_REQUESTS_WITH_STATUS;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("CodeBlock2Expr")
public class OptimisticConcurrencyControlTest extends AbstractDaoTest {

    @Test
    public void shouldThrowExceptionIfTimestampOfCurrentlyUpdatingOrderDoesNotMatch() {
        assertThrows(OptimisticLockException.class, (CauseRethrowingExecutable) () -> {
            Order order = em.find(Order.class, ORDER_ID);

            hiberUtil.executeInTransaction(entityManager -> {
                Order other = entityManager.find(Order.class, ORDER_ID);
                other.setName("Updated in other transaction");
            });

            order.setName("Updated");
        });
    }

    @Test(expected = OptimisticLockException.class)
    public void shouldThrowExceptionIfHibernateOptimisticControlIsViolated() {
        BankAccount bankAccount = em.find(BankAccount.class, BANK_ACCOUNT_ID);
        BankAccount originalBankAccount = new BankAccount(bankAccount);

        hiberUtil.updateBankNameInSeparateTransaction();
        bankAccount.setBankName("Bank has changed");

        flushWithCleanup(() -> hiberUtil.revertBankAccountToOriginalState(originalBankAccount));
    }

    @Test
    @Commit
    public void shouldNotPerformHibernateOptimisticControlForMergedEntities() {
        BankAccount detachedBankAccount = hiberUtil.executeInTransactionAndReturnResult(
                entityManager -> entityManager.find(BankAccount.class, BANK_ACCOUNT_ID));
        BankAccount originalBankAccount = new BankAccount(detachedBankAccount);

        hiberUtil.updateBankNameInSeparateTransaction();
        detachedBankAccount.setBankName("Merged bank account");
        em.merge(detachedBankAccount);

        flushWithCleanup(() ->
                hiberUtil.revertBankAccountToOriginalState(originalBankAccount, RevertStrategy.JDBC));
    }

    @Test
    @Commit
    public void shouldThrowExceptionIfEntriesReturnedByQueryInOptimisticLockingModeAreModifiedByAnotherTransaction() {
        List<Long> affectedRequests = getIdsOfConfirmedRequests();

        assertThrows(OptimisticLockException.class, (CauseRethrowingExecutable) () -> {
            hiberUtil.executeInTransaction(entityManager -> {
                for (RequestStatus status : RequestStatus.values()) {
                    List<DeliveryRequest> requests = getRequestsWithOptimisticLock(status, entityManager);

                    BigDecimal total = BigDecimal.ZERO;
                    for (DeliveryRequest request : requests) {
                        MonetaryAmount price = request.getPrice();
                        BigDecimal value = price.getValue();
                        total = total.add(value);

                        changeConfirmedRequestToDeliveredSoThatTotalPriceIsCalculatedTwice(request);
                    }
                }
            });
        });

        revertRequestsToConfirmedState(affectedRequests);
    }

    @Test
    public void shouldThrowExceptionIfChildAssociationWasModifiedByAnotherTransaction() {
        assertThrows(OptimisticLockException.class, (CauseRethrowingExecutable) () -> {
            hiberUtil.executeInTransaction(entityManager -> {

                User user = entityManager.find(User.class, USER_ID,
                        LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                Set<Order> orders = user.getOrders();

                meanwhileAddNewOrder();

                long discount = calculateDiscount(orders);
                Order newOrder = createOrder();
                billUserForOrder(newOrder, discount);

                user.addOrder(newOrder);
            });
        });
        deleteLastCreatedOrder();
    }

    private void meanwhileAddNewOrder() {
        hiberUtil.executeInTransaction(entityManager -> {

            User user = entityManager.find(User.class, USER_ID,
                    LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Set<Order> orders = user.getOrders();

            long discount = calculateDiscount(orders);
            Order newOrder = createOrder();
            billUserForOrder(newOrder, discount);

            user.addOrder(newOrder);
        });
    }

    private long calculateDiscount(Set<Order> orders) {
        BigDecimal discount = BigDecimal.ZERO;

        for (int i = 0; i < orders.size(); i++) {
            discount = discount.add(valueOf(0.00d));
        }

        return discount.longValue();
    }

    private Order createOrder() {
        Order order = new Order();
        order.setName("Order #562");
        return order;
    }

    private void billUserForOrder(Order newOrder, long discount) {
        //mock implementation
    }

    private void deleteLastCreatedOrder() {
        hiberUtil.executeInTransaction(entityManager -> {
            Long lastGeneratedId = entityManager.createQuery(GET_LARGEST_ORDER_ID, Long.class)
                    .setMaxResults(1)
                    .getSingleResult();

            Order lastOrder = entityManager.find(Order.class, lastGeneratedId);

            entityManager.remove(lastOrder);
        });
    }

    private List<Long> getIdsOfConfirmedRequests() {
        return em.createQuery("SELECT dr.id FROM DeliveryRequest dr WHERE dr.status = :status", Long.class)
                .setParameter("status", CONFIRMED)
                .getResultList();
    }

    private void revertRequestsToConfirmedState(List<Long> affectedRequests) {
        em.createQuery("UPDATE DeliveryRequest dr SET dr.status = :status WHERE dr.id IN :ids")
                .setParameter("status", CONFIRMED)
                .setParameter("ids", affectedRequests)
                .executeUpdate();
    }

    private void changeConfirmedRequestToDeliveredSoThatTotalPriceIsCalculatedTwice(DeliveryRequest request) {
        if (request.getStatus().equals(CONFIRMED)) {
            Long requestId = request.getId();
            changeRequestStatus(requestId, DELIVERED);
        }
    }

    private List<DeliveryRequest> getRequestsWithOptimisticLock(RequestStatus status, EntityManager entityManager) {
        return entityManager
                .createQuery(GET_REQUESTS_WITH_STATUS, DeliveryRequest.class)
                .setLockMode(LockModeType.OPTIMISTIC)
                .setParameter("status", status)
                .getResultList();
    }

    private void changeRequestStatus(Long requestId, RequestStatus newStatus) {
        hiberUtil.executeInTransaction(entityManager -> {
            DeliveryRequest dr = entityManager.find(DeliveryRequest.class, requestId);
            dr.setStatus(newStatus);
        });
    }
}
