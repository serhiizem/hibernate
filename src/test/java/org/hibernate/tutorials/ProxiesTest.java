package org.hibernate.tutorials;

import org.hibernate.LazyInitializationException;
import org.hibernate.stat.Statistics;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.User;
import org.hibernate.tutorials.model.embeddable.Comment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hibernate.proxy.HibernateProxyHelper.getClassWithoutInitializingProxy;
import static org.hibernate.utils.Constants.REQUEST_ID;
import static org.hibernate.utils.Constants.USER_ID;
import static org.junit.jupiter.api.Assertions.*;

public class ProxiesTest extends AbstractDaoTest {

    @Before
    public void init() {
        sessionFactory.getStatistics().clear();
    }

    @Test
    public void shouldNotInitializeProxyIfOnlyIdWasRequested() {
        DeliveryRequest dr = em.getReference(DeliveryRequest.class, REQUEST_ID);

        //noinspection ResultOfMethodCallIgnored
        dr.getId();

        assertFalse(hiberUtil.isLoaded(dr));
    }

    @Test
    public void shouldInitializeProxyWhenNonIdPropertyIsRequested() {
        DeliveryRequest dr = em.getReference(DeliveryRequest.class, REQUEST_ID);

        //noinspection ResultOfMethodCallIgnored
        dr.getStatus();

        assertTrue(hiberUtil.isLoaded(dr));
    }

    @Test
    public void shouldGenerateProxySubclassWhenGetReferenceIsCalled() {
        DeliveryRequest dr = em.getReference(DeliveryRequest.class, REQUEST_ID);

        Class<?> originalClass = getClassWithoutInitializingProxy(dr);

        assertNotEquals(DeliveryRequest.class, dr.getClass());
        assertEquals(DeliveryRequest.class, originalClass);
    }

    @Test
    public void shouldCreateUserOrderWithoutAnySelectStatementExecuted() {
        User user = em.getReference(User.class, USER_ID);

        Order order = new Order();
        order.setName("Order #9135");

        order.setUser(user);

        em.persist(order);
        em.flush();

        Statistics statistics = sessionFactory.getStatistics();
        long entityLoadCount = statistics.getEntityLoadCount();

        assertEquals(0, entityLoadCount);
    }

    @Test(expected = LazyInitializationException.class)
    public void shouldThrowExceptionIfLazyCollectionOfLoadedEntityWasNotInitializedDuringUnitOfWork() {
        DeliveryRequest detachedDr = hiberUtil.executeInTransactionAndReturnResult(entityManager ->
                entityManager.find(DeliveryRequest.class, REQUEST_ID));

        List<Comment> comments = detachedDr.getComments();
        //noinspection ResultOfMethodCallIgnored
        comments.forEach(Comment::getAuthor);
    }

    @Test(expected = LazyInitializationException.class)
    public void shouldThrowLazyInitializationExceptionIfProxyIsNotInitializedDuringUnitOfWork() {
        DeliveryRequest dr = hiberUtil.executeInTransactionAndReturnResult(entityManager ->
                entityManager.getReference(DeliveryRequest.class, REQUEST_ID));

        //noinspection ResultOfMethodCallIgnored
        dr.getCreationDate();
    }
}
