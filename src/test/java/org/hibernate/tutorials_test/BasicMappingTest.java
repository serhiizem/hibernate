package org.hibernate.tutorials_test;

import org.hibernate.tutorials.model.Order;
import org.hibernate.tutorials.model.Request;
import org.hibernate.tutorials.model.User;
import org.junit.Test;

import java.util.Date;

import static org.hibernate.TestConstants.JdbcUtils.GET_USER_BY_ID_STATEMENT;
import static org.hibernate.TestConstants.JdbcUtils.USER_MAPPER;
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
        Request request = new Request("Test Request Description");

        em.persist(request);
        em.flush();

        assertNotNull(request.getCreationDate());
    }

    @Test
    public void shouldUpdateLastModifiedDateOnDescriptionUpdate() {
        Request request = new Request("Test description");

        em.persist(request);
        em.flush();
        Date modifiedDateBeforeDescriptionModification = request.getLastModifiedDate();

        request.setDescription("Modified description");
        em.persist(request);
        em.flush();

        assertNull(modifiedDateBeforeDescriptionModification);
        assertNotNull(request.getLastModifiedDate());
    }
}
