package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.Request;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddableComponentsTest extends AbstractDaoTest {

    @Test
    public void overriddenEmbeddedAttributeShouldCorrespondToAnotherSetOfDBColumns() {
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
