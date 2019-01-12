package org.hibernate.tutorials_test;

import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.User;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicMappingTest extends AbstractDaoTest {

    @Test
    public void shouldRetrieveShortOrderDescription() {
        Order order = em.find(Order.class, 1L);

        String shortDescription = order.getShortDescription();

        assertEquals("Lorem ips...", shortDescription);
    }

    @Test
    public void should() {
        User testUser = new User("test_card_holder", "555 555");

        em.persist(testUser);

        System.out.println("Test");
    }
}
