package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.payments.MonetaryAmount;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Currency;

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
            new BigDecimal("15.0"), Currency.getInstance("USD"));

    private static final DeliveryRequest DEFAULT_TEST_REQUEST =
            new DeliveryRequest("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS, PRICE);

    DeliveryRequest deliveryRequest;

    @PersistenceContext
    EntityManager em;

    @Autowired
    protected JdbcTemplate jdbcTemplate; //for direct db access without hibernate

    @Before
    public void init() {
        deliveryRequest = new DeliveryRequest(DEFAULT_TEST_REQUEST);
    }
}
