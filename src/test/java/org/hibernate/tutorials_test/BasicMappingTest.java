package org.hibernate.tutorials_test;

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

        //noinspection ConstantConditions
        assertNotEquals(initialCardNumber, userFromJbdc.getCreditCardNumber());
    }

    @Test
    public void shouldGenerateCreationDateTimestamp() {
        em.persist(request);
        em.flush();

        assertNotNull(request.getCreationDate());
    }

    @Test
    public void shouldUpdateLastModifiedDateOnDescriptionUpdate() {
        em.persist(request);
        em.flush();
        Date modifiedDateBeforeDescriptionModification = request.getLastModifiedDate();

        request.setDescription("Modified description");
        em.persist(request);
        em.flush();

        assertNull(modifiedDateBeforeDescriptionModification);
        assertNotNull(request.getLastModifiedDate());
    }

    @Test
    public void shouldStoreEnumValueAsString() {
        em.persist(request);
        em.flush();

        String statusAsString = jdbcTemplate.queryForObject(
                GET_REQUEST_STATUS_BY_REQUEST_ID_STATEMENT,
                new Object[]{request.getId()},
                String.class);

        //noinspection ConstantConditions
        assertEquals(request.getStatus().toString(), statusAsString);
    }
}
