package org.hibernate.tutorials;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.tutorials.model.DeliveryRequest;
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

import static org.hibernate.utils.Constants.DEFAULT_TEST_REQUEST;

@Slf4j
@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DaoConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractDaoTest {

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

    protected void flushWithCleanup(VoidSupplier tearDownFunction) {
        try {
            em.flush();
        } finally {
            tearDownFunction.get();
        }
    }
}
