package org.hibernate.tutorials;

import org.hibernate.tutorials.model.User;
import org.hibernate.tutorials.model.inheritance.joined.BillingDetails;
import org.hibernate.tutorials.model.inheritance.joined.CreditCard;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolymorphicManyToOneTest extends AbstractDaoTest {

    @Test
    public void realBillingDetailClassShouldDifferFromTheAnyExistingBillingSubclass() {
        User user = em.find(User.class, 1L);

        BillingDetails defaultBillingDetails = user.getDefaultBillingDetails();

        assertNotNull(defaultBillingDetails);
        assertFalse(defaultBillingDetails instanceof CreditCard);
    }

    @Test
    public void proxyGeneratedByHibernateCannotBeCastToBillingDetailsSubclass() {
        assertThrows(ClassCastException.class, () -> {
            User user = em.find(User.class, 1L);
            BillingDetails defaultBillingDetails = user.getDefaultBillingDetails();
            CreditCard cc = (CreditCard) defaultBillingDetails;
        });
    }

    @Test
    public void shouldNotGenerateProxyIfJoinFetchIsUsed() {
        User user = em.createQuery(
                "select u from User u " +
                        "left join fetch u.defaultBillingDetails " +
                        "where u.id = :id", User.class)
                .setParameter("id", 1L)
                .getSingleResult();

        BillingDetails defaultBillingDetails = user.getDefaultBillingDetails();

        assertNotNull(defaultBillingDetails);
        assertTrue(defaultBillingDetails instanceof CreditCard);
    }
}
