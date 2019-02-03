package org.hibernate.tutorials;

import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.embeddable.Address;
import org.hibernate.tutorials.model.embeddable.Dimensions;
import org.hibernate.tutorials.model.embeddable.Measurement;
import org.hibernate.tutorials.model.embeddable.Weight;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmbeddableComponentsTest extends AbstractDaoTest {

    @Test
    public void addressOverriddenEmbeddedAttributeShouldCorrespondToAnotherSetOfDBColumns() {
        em.persist(deliveryRequest);
        em.flush();

        DeliveryRequest savedRequest = em.find(DeliveryRequest.class, deliveryRequest.getId());

        Address savedFromAddress = savedRequest.getFromAddress();
        assertNotNull(savedFromAddress.getCity());

        Address savedDeliveryAddress = savedRequest.getDeliveryAddress();
        assertNotNull(savedDeliveryAddress.getCity());

        assertNotEquals(savedFromAddress.getCity(), savedDeliveryAddress.getCity());
    }

    @Test
    public void dimensionsMeasurementColumnNameAndSymbolShouldBeOverridden() {
        em.persist(deliveryRequest);
        em.flush();

        Measurement measurement = jdbcTemplate.queryForObject(
                "SELECT DIMENSIONS_NAME, DIMENSIONS_SYMBOL " +
                        "FROM REQUESTS r " +
                        "WHERE r.id = ?",
                new Object[]{deliveryRequest.getId()},
                (rs, rowNum) -> {
                    String name = rs.getString(1);
                    String symbol = rs.getString(2);
                    return new Measurement(name, symbol);
                });

        Dimensions dimensions = deliveryRequest.getDimensions();
        assertNotNull(measurement);
        assertEquals(dimensions.getName(), measurement.getName());
        assertEquals(dimensions.getSymbol(), measurement.getSymbol());
    }

    @Test
    public void weightMeasurementColumnNameAndSymbolShouldBeOverridden() {
        em.persist(deliveryRequest);
        em.flush();

        Measurement measurement = jdbcTemplate.queryForObject(
                "SELECT WEIGHT_NAME, WEIGHT_SYMBOL " +
                        "FROM REQUESTS r " +
                        "WHERE r.id = ?",
                new Object[]{deliveryRequest.getId()},
                (rs, rowNum) -> {
                    String name = rs.getString(1);
                    String symbol = rs.getString(2);
                    return new Measurement(name, symbol);
                });

        Weight weight = deliveryRequest.getWeight();
        assertNotNull(measurement);
        assertEquals(weight.getName(), measurement.getName());
        assertEquals(weight.getSymbol(), measurement.getSymbol());
    }
}
