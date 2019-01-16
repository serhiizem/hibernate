package org.hibernate.tutorials;

import org.junit.Test;

import static org.hibernate.JdbcUtils.GET_REQUEST_PRICE_BY_REQUEST_ID_STATEMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversionTest extends AbstractDaoTest {

    @Test
    public void shouldStoreMonetaryAmountAttributeUsingConversionToString() {
        em.persist(request);
        em.flush();

        String priceStringFromDB = jdbcTemplate.queryForObject(
                GET_REQUEST_PRICE_BY_REQUEST_ID_STATEMENT,
                new Object[]{request.getId()},
                String.class);

        assertEquals(request.getPrice().toString(), priceStringFromDB);
    }
}