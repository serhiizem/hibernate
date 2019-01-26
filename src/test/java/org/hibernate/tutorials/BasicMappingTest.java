package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.User;
import org.junit.Test;

import java.util.Date;

import static org.hibernate.JdbcUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class BasicMappingTest extends AbstractDaoTest {

    @Test
    public void shouldRetrieveShortOrderDescription() {
        Order order = em.find(Order.class, 1L);

        String shortDescription = order.getShortDescription();

        assertEquals("Lorem ips...", shortDescription);
    }

    @Test
    public void sensitiveInformationShouldBeStoredUsingEncryption() {
        String initialCardNumber = "555 555";
        User user = new User("Test Card Holder", initialCardNumber);
        em.persist(user);
        em.flush();

        User userFromJbdc = jdbcTemplate.queryForObject(
                GET_USER_BY_ID_STATEMENT,
                new Object[]{user.getId()},
                USER_MAPPER);

        assertNotNull(userFromJbdc);
        assertNotEquals(initialCardNumber, userFromJbdc.getCreditCardNumber());
    }

    @Test
    public void shouldGenerateCreationDateTimestamp() {
        em.persist(deliveryRequest);
        em.flush();

        assertNotNull(deliveryRequest.getCreationDate());
    }

    @Test
    public void shouldUpdateLastModifiedDateOnDescriptionUpdate() {
        em.persist(deliveryRequest);
        em.flush();
        Date modifiedDateBeforeDescriptionModification = deliveryRequest.getLastModifiedDate();

        deliveryRequest.setDescription("Modified description");
        em.persist(deliveryRequest);
        em.flush();

        assertNull(modifiedDateBeforeDescriptionModification);
        assertNotNull(deliveryRequest.getLastModifiedDate());
    }

    @Test
    public void shouldStoreEnumValueAsString() {
        em.persist(deliveryRequest);
        em.flush();

        String statusAsString = jdbcTemplate.queryForObject(
                GET_REQUEST_STATUS_BY_REQUEST_ID_STATEMENT,
                new Object[]{deliveryRequest.getId()},
                String.class);

        assertNotNull(statusAsString);
        assertEquals(deliveryRequest.getStatus().toString(), statusAsString);
    }
}
