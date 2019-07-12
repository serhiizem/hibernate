package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.User;
import org.junit.Test;

import static org.hibernate.utils.Constants.ORDER_ID;
import static org.hibernate.utils.Constants.USER_ID;
import static org.junit.jupiter.api.Assertions.*;

public class DetachedInstancesTest extends AbstractDaoTest {

    @Test
    public void shouldNotProvideIdentityGuaranteesForObjectsFromDifferentPersistentContexts() {
        User user1_1 = em.find(User.class, USER_ID);
        User user1_2 = em.find(User.class, USER_ID);

        User userFromAnotherContext = hiberUtil.executeInTransactionAndReturnResult(entityManager ->
                entityManager.find(User.class, USER_ID));

        assertSame(user1_1, user1_2);
        assertUsersHaveSameDatabaseIdentity(user1_1, user1_2);

        //because equals is intentionally not overwritten for User class
        assertNotSame(user1_1, userFromAnotherContext);
        assertUsersHaveSameDatabaseIdentity(user1_1, userFromAnotherContext);
    }

    private void assertUsersHaveSameDatabaseIdentity(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
    }

    @Test
    public void shouldYieldEqualityForObjectsWithDatabaseIdentityButDifferentContextsIfEqualsIsOverwritten() {
        Order order = em.find(Order.class, ORDER_ID);

        Order orderFromAnotherContext = hiberUtil.executeInTransactionAndReturnResult(entityManager ->
                entityManager.find(Order.class, ORDER_ID));

        assertNotSame(order, orderFromAnotherContext);
        assertEquals(order, orderFromAnotherContext);
        assertEquals(order.getId(), orderFromAnotherContext.getId());
    }

    @Test
    public void shouldCreateNewPersistentObjectAfterCallingMerge() {
        User detachedUser = hiberUtil.executeInTransactionAndReturnResult(entityManager ->
                entityManager.find(User.class, USER_ID));

        User mergedUser = em.merge(detachedUser);

        boolean isDetachedUserInContext = em.contains(detachedUser);
        boolean isMergedUserInContext = em.contains(mergedUser);

        assertFalse(isDetachedUserInContext);
        assertTrue(isMergedUserInContext);
    }
}