package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddableComponentsTest extends AbstractDaoTest {

    @Test
    public void overriddenEmbeddedAttributeShouldCorrespondToAnotherSetOfDBColumns() {
        em.persist(deliveryRequest);
        em.flush();

        DeliveryRequest savedRequest = em.find(DeliveryRequest.class, deliveryRequest.getId());

        Address savedFromAddress = savedRequest.getFromAddress();
        assertNotNull(savedFromAddress.getCity());

        Address savedDeliveryAddress = savedRequest.getDeliveryAddress();
        assertNotNull(savedDeliveryAddress.getCity());

        assertNotEquals(savedFromAddress.getCity(), savedDeliveryAddress.getCity());
    }
}
