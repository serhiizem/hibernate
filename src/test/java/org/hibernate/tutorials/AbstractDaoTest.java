package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.Request;
import org.hibernate.tutorials.model.payments.Currency;
import org.hibernate.tutorials.model.payments.MonetaryAmount;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractDaoTest {

    private static final Address DEFAULT_DELIVERY_ADDRESS =
            new Address("Street #2222", "5326", "City #22");
    private static final Address DEFAULT_FROM_ADDRESS =
            new Address("Street #1111", "6235", "City #11");
    private static final MonetaryAmount PRICE = new MonetaryAmount(
            new BigDecimal("15.0"), Currency.EUR);

    private static final Request DEFAULT_TEST_REQUEST =
            new Request("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS, PRICE);

    Request request;

    @PersistenceContext
    EntityManager em;

    @Autowired
    protected JdbcTemplate jdbcTemplate; //for direct db access without hibernate

    @Before
    public void init() {
        request = new Request(DEFAULT_TEST_REQUEST);
    }
}
