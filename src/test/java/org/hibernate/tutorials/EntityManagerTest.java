package org.hibernate.tutorials;

import org.hibernate.LazyInitializationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.User;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hibernate.utils.Constants.REQUEST_ID;
import static org.junit.Assert.assertThat;

public class EntityManagerTest extends AbstractDaoTest {

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionIfNotNullFieldIsNotStoredBeforePersist() {
        executeAndRethrowCauseIfErrored(entityManager -> {
            User user = new User();
            user.setCreditCardNumber("TEST NUMBER");
            entityManager.persist(user);
            user.setUserName("My Username");
            entityManager.flush();
        });
    }

    @Test
    public void shouldGuaranteeRepeatableReadWithinSamePersistenceContext() {
        DeliveryRequest deliveryRequest1 = em.find(DeliveryRequest.class, REQUEST_ID);
        DeliveryRequest deliveryRequest2 = em.find(DeliveryRequest.class, REQUEST_ID);

        assertThat(deliveryRequest1 == deliveryRequest2, is(true));
    }


    @Test(expected = LazyInitializationException.class)
    public void shouldThrowLazyInitializationExceptionIfProxyIsNotInitializedDuringUnitOfWork() {
        DeliveryRequest dr = executeInTransactionAndReturnResult(entityManager ->
                em.getReference(DeliveryRequest.class, REQUEST_ID));

        //noinspection ResultOfMethodCallIgnored
        dr.getCreationDate();
    }
}
