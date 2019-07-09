package org.hibernate.tutorials;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.embeddable.Address;
import org.hibernate.tutorials.model.embeddable.Dimensions;
import org.hibernate.tutorials.model.embeddable.Weight;
import org.hibernate.tutorials.model.payments.MonetaryAmount;
import org.hibernate.utils.ConcurrencyUtils;
import org.hibernate.utils.ErrorableExecution;
import org.hibernate.utils.HibernateUtil;
import org.hibernate.utils.VoidSupplier;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Currency;

import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;

@Slf4j
@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DaoConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractDaoTest {

    private static final Address DEFAULT_DELIVERY_ADDRESS =
            new Address("Street #2222", "5326", "City #22");
    private static final Address DEFAULT_FROM_ADDRESS =
            new Address("Street #1111", "6235", "City #11");
    private static final MonetaryAmount PRICE = new MonetaryAmount(
            new BigDecimal("15.0"), Currency.getInstance("USD"));

    private static final Dimensions DEFAULT_DIMENSIONS =
            new Dimensions("centimetre", "cm", new BigDecimal("15.0"), new BigDecimal("30.0"), new BigDecimal("20.0"));
    private static final Weight DEFAULT_WEIGHT =
            new Weight("pound", "lb", new BigDecimal("74.0"));

    private static final DeliveryRequest DEFAULT_TEST_REQUEST =
            new DeliveryRequest("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS, PRICE, DEFAULT_DIMENSIONS, DEFAULT_WEIGHT);

    DeliveryRequest deliveryRequest;

    @PersistenceContext
    EntityManager em;

    SessionFactory sessionFactory;
    JdbcTemplate jdbcTemplate; //for direct db access without hibernate
    HibernateUtil hiberUtil;
    @SuppressWarnings("WeakerAccess")
    ConcurrencyUtils concurrencyUtils;

    @Autowired
    public void initAbstractDaoTest(EntityManagerFactory emf,
                                    ConcurrencyUtils concurrencyUtils,
                                    JdbcTemplate jdbcTemplate,
                                    HibernateUtil hibernateUtil) {
        this.sessionFactory = emf.unwrap(SessionFactory.class);
        this.concurrencyUtils = concurrencyUtils;
        this.jdbcTemplate = jdbcTemplate;
        this.hiberUtil = hibernateUtil;
    }

    @Before
    public void init() {
        deliveryRequest = new DeliveryRequest(DEFAULT_TEST_REQUEST);
    }

    protected Session getSession() {
        return em.unwrap(Session.class);
    }

    @SneakyThrows
    protected void executeAndRethrowCauseIfErrored(ErrorableExecution execution) {
        try {
            execution.accept(em);
        } catch (Exception e) {
            throw e.getCause();
        }
    }

    @SneakyThrows
    protected void flushWithCleanup(EntityManager entityManager,
                                    VoidSupplier tearDownFunction) {
        try {
            entityManager.flush();
        } catch (Exception e) {
            tearDownFunction.get();
            throw e;
        }
    }
}
