package org.hibernate.tutorials;

import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.RequestStatus;
import org.hibernate.tutorials.model.User;
import org.hibernate.utils.UserNotFoundException;
import org.junit.Test;

import javax.persistence.FlushModeType;
import java.util.Optional;

import static org.hibernate.tutorials.model.RequestStatus.CONFIRMED;
import static org.hibernate.utils.Constants.REQUEST_ID;
import static org.hibernate.utils.Constants.USER_ID;
import static org.hibernate.utils.JdbcUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class EntityManagerTest extends AbstractDaoTest {

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionIfNotNullFieldIsNotStoredBeforePersist() {
        executeAndRethrowCauseIfErrored(entityManager -> {
            User user = new User();
            user.setCreditCardNumber("TEST NUMBER");
            entityManager.persist(user);
            user.setUserName("My Username");
            entityManager.flush();
        });
    }

    @Test
    public void shouldGuaranteeRepeatableReadWithinSamePersistenceContext() {
        DeliveryRequest deliveryRequest1 = em.find(DeliveryRequest.class, REQUEST_ID);
        DeliveryRequest deliveryRequest2 = em.find(DeliveryRequest.class, REQUEST_ID);

        assertSame(deliveryRequest1, deliveryRequest2);
    }

    @Test
    public void shouldOverrideStateOfManagedObjectIfRefreshed() {
        DeliveryRequest dr = em.find(DeliveryRequest.class, REQUEST_ID);

        RequestStatus initialStatus = getCurrentRequestStatus(dr.getId());

        jdbcTemplate.update(
                SET_STATUS_OF_REQUEST,
                CONFIRMED.toString(), dr.getId());

        em.refresh(dr);
        RequestStatus currentStatus = dr.getStatus();

        assertNotEquals(initialStatus, currentStatus);
    }

    private RequestStatus getCurrentRequestStatus(Long requestId) {
        String requestStatusStr = jdbcTemplate.queryForObject(
                GET_REQUEST_STATUS_BY_REQUEST_ID,
                new Object[]{requestId},
                String.class);

        return Optional.ofNullable(requestStatusStr)
                .map(RequestStatus::fromString)
                .orElse(RequestStatus.PROCESSING);
    }

    @Test
    public void shouldNotExecuteUpdateIfContextIsMarkedReadOnly() {
        Session session = getSession();
        session.setDefaultReadOnly(true);
        String originalUsername = getUsernameOfUser(USER_ID);
        User user = session.find(User.class, USER_ID);
        user.setUserName("Should not update");

        session.flush();
        String usernameAfterUpdate = getUsernameOfUser(USER_ID);

        assertEquals(originalUsername, usernameAfterUpdate);
    }

    private String getUsernameOfUser(@SuppressWarnings("SameParameterValue") Long userId) {
        User user = jdbcTemplate.queryForObject(
                GET_USER_BY_ID,
                new Object[]{userId},
                USER_MAPPER);

        return Optional.ofNullable(user)
                .map(User::getUserName)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Test
    public void shouldNotExecuteUpdateIfEntityObjectIsMarkedReadOnly() {
        String originalUsername = getUsernameOfUser(USER_ID);
        Session session = getSession();
        User user = session.find(User.class, USER_ID);
        session.setReadOnly(user, true);
        user.setUserName("Should not update");

        session.flush();
        String usernameAfterUpdate = getUsernameOfUser(USER_ID);

        assertEquals(originalUsername, usernameAfterUpdate);
    }

    @Test
    public void shouldNotExecuteSelectIfQueryIsSetReadOnly() {
        String originalUsername = getUsernameOfUser(USER_ID);

        Session session = getSession();
        User user = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", USER_ID)
                .setReadOnly(true)
                .getSingleResult();
        user.setUserName("Should not update");
        session.flush();

        String usernameAfterUpdate = getUsernameOfUser(USER_ID);
        assertEquals(originalUsername, usernameAfterUpdate);
    }

    @Test
    public void shouldNotExecuteUpdateIfReadOnlyHintIsSpecified() {
        String originalUsername = getUsernameOfUser(USER_ID);

        Session session = getSession();
        User user = session
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", USER_ID)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
        user.setUserName("Should not update");
        session.flush();

        String usernameAfterUpdate = getUsernameOfUser(USER_ID);
        assertEquals(originalUsername, usernameAfterUpdate);
    }

    @Test
    public void shouldSynchronizeStateWithDbBeforeQueryExecution() {
        String originalUsername = getUsernameOfUser(USER_ID);

        User user = em.find(User.class, USER_ID);
        String usernameChange = "Changed name";
        user.setUserName(usernameChange);

        User queriedUser = em
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", USER_ID)
                .getSingleResult();

        String userNameAfterQuery = getUsernameOfUser(USER_ID);

        assertNotEquals(originalUsername, userNameAfterQuery);
        assertEquals(userNameAfterQuery, usernameChange);
    }

    @Test
    public void shouldNotPerformUpdateBeforeQueryIfFlushModeCommitIsEnabled() {
        String originalUsername = getUsernameOfUser(USER_ID);
        User user = em.find(User.class, USER_ID);

        user.setUserName("Changed name");
        em.setFlushMode(FlushModeType.COMMIT);

        User queriedUser = em
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", USER_ID)
                .getSingleResult();

        String userNameAfterQuery = getUsernameOfUser(USER_ID);

        assertEquals(originalUsername, userNameAfterQuery);
    }
}