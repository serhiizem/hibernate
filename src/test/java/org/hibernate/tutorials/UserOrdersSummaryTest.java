package org.hibernate.tutorials;

import org.hibernate.tutorials.model.advanced.UserOrderSummary;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UserOrdersSummaryTest extends AbstractDaoTest {

    @Test
    public void shouldFindUserOrdersSummary() {
        //given
        UserOrderSummary userOrders = em.find(UserOrderSummary.class, 1L);

        //when
        int numberOfOrders = userOrders.getNumberOfOrders();

        //then
        Assertions.assertEquals(3, numberOfOrders);
    }
}
