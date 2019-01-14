package org.hibernate.tutorials_test;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.Request;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;

@JdbcTest
@ContextConfiguration(classes = TestDaoConfig.class)
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractDaoTest {

    private static final Address DEFAULT_DELIVERY_ADDRESS =
            new Address("Street #2222", "5326", "City #22");
    private static final Address DEFAULT_FROM_ADDRESS =
            new Address("Street #1111", "6235", "City #11");

    private static final Request DEFAULT_TEST_REQUEST =
            new Request("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS);

    protected Request request;

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected JdbcTemplate jdbcTemplate; //for direct db access without hibernate

    @Before
    public void init() {
        request = em.merge(DEFAULT_TEST_REQUEST);
    }
}
