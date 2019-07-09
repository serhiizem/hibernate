package org.hibernate.tutorials;

import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.Distance;
import org.hibernate.tutorials.model.LengthUnit;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static org.hibernate.utils.JdbcUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceUserTypeTest extends AbstractDaoTest {

    @Test
    public void shouldConvertAirDistanceFromKilometersToMiles() {
        Distance distanceInKm = Distance.fromString("15 km");
        double initialDistanceInKm = distanceInKm.getQuantity();
        deliveryRequest.setAirDistance(distanceInKm);

        em.persist(deliveryRequest);
        em.flush();

        String airDistanceResult = jdbcTemplate.queryForObject(
                GET_AIR_DISTANCE,
                new Object[]{deliveryRequest.getId()},
                String.class);


        double distanceInMiles = initialDistanceInKm / 1.61D;

        Assertions.assertNotNull(airDistanceResult);
        assertEquals(distanceInMiles, parseDouble(airDistanceResult));
    }

    @Test
    public void shouldStoreAirDistanceInMiles() {
        Distance distanceInKm = Distance.fromString("15 km");
        deliveryRequest.setAirDistance(distanceInKm);

        em.persist(deliveryRequest);
        em.flush();

        LengthUnit airDistanceUnitResult = LengthUnit.fromString(
                jdbcTemplate.queryForObject(
                        GET_AIR_DISTANCE_UNIT,
                        new Object[]{deliveryRequest.getId()},
                        String.class));

        Assertions.assertNotNull(airDistanceUnitResult);
        assertEquals(airDistanceUnitResult, LengthUnit.MILE);
    }

    @Test
    public void shouldConvertLandDistanceFromMilesToKilometers() {
        Distance distanceInMi = Distance.fromString("15 mi");
        double initialDistanceInMi = distanceInMi.getQuantity();
        deliveryRequest.setLandDistance(distanceInMi);

        em.persist(deliveryRequest);
        em.flush();

        String landDistanceResult = jdbcTemplate.queryForObject(
                GET_LAND_DISTANCE,
                new Object[]{deliveryRequest.getId()},
                String.class);


        double distanceInKm = initialDistanceInMi * 1.61D;

        Assertions.assertNotNull(landDistanceResult);
        assertEquals(distanceInKm, parseDouble(landDistanceResult));
    }

    @Test
    public void shouldStoreLandDistanceInKm() {
        Distance distanceInMi = Distance.fromString("15 mi");
        deliveryRequest.setLandDistance(distanceInMi);

        em.persist(deliveryRequest);
        em.flush();

        LengthUnit landDistanceUnitResult = LengthUnit.fromString(
                jdbcTemplate.queryForObject(
                        GET_LAND_DISTANCE_UNIT,
                        new Object[]{deliveryRequest.getId()},
                        String.class));

        Assertions.assertNotNull(landDistanceUnitResult);
        assertEquals(landDistanceUnitResult, LengthUnit.KILOMETER);
    }

    @Test
    public void shouldFindByQuantityPropertyOfDistanceCustomUserType() {
        createTestRequests();

        List<DeliveryRequest> requests = em.createQuery(
                "from DeliveryRequest d " +
                        "where d.airDistance.quantity = 15 or d.landDistance.quantity = 15", DeliveryRequest.class)
                .getResultList();

        assertEquals(requests.size(), 2);
    }

    private void createTestRequests() {
        List<DeliveryRequest> deliveryRequests = prepareRequests();
        storeRequests(deliveryRequests);
    }

    public List<DeliveryRequest> prepareRequests() {
        List<DeliveryRequest> requests = new ArrayList<>();

        DeliveryRequest dr1 = new DeliveryRequest(deliveryRequest);
        dr1.setLandDistance(Distance.fromString("15 km"));
        DeliveryRequest dr2 = new DeliveryRequest(deliveryRequest);
        dr2.setAirDistance(Distance.fromString("15 mi"));

        requests.add(dr1);
        requests.add(dr2);

        return requests;
    }

    public void storeRequests(List<DeliveryRequest> requests) {
        for (DeliveryRequest request : requests) {
            em.persist(request);
        }
        em.flush();
    }
}
