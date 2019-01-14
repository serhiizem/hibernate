package org.hibernate.tutorials_test;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.Request;
import org.junit.Test;

import static org.hibernate.TestConstants.DEFAULT_DELIVERY_ADDRESS;
import static org.hibernate.TestConstants.DEFAULT_FROM_ADDRESS;
import static org.hibernate.TestConstants.DEFAULT_TEST_REQUEST;
import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddableComponentsTest extends AbstractDaoTest {

    @Test
    public void overriddenEmbeddedAttributeShouldCorrespondToAnotherSetOfDBColumns() {
        Request request = DEFAULT_TEST_REQUEST;

        em.persist(request);
        em.flush();

        Request savedRequest = em.find(Request.class, request.getId());

        Address savedFromAddress = savedRequest.getFromAddress();
        assertNotNull(savedFromAddress.getCity());

        Address savedDeliveryAddress = savedRequest.getDeliveryAddress();
        assertNotNull(savedDeliveryAddress.getCity());

        assertNotEquals(savedFromAddress.getCity(), savedDeliveryAddress.getCity());
    }
}
