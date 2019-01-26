package org.hibernate.tutorials;

import org.hibernate.HibernateUtil;
import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.Distance;
import org.hibernate.tutorials.model.LengthUnit;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static org.hibernate.HibernateUtil.getAndCast;
import static org.hibernate.JdbcUtils.*;

public class DistanceUserTypeTest extends AbstractDaoTest {

    private final HibernateUtil<DeliveryRequest> testUtil = new DistanceUserTypeUtil();

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
        Assertions.assertEquals(distanceInMiles, parseDouble(airDistanceResult));
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
        Assertions.assertEquals(airDistanceUnitResult, LengthUnit.MILE);
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
        Assertions.assertEquals(distanceInKm, parseDouble(landDistanceResult));
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
        Assertions.assertEquals(landDistanceUnitResult, LengthUnit.KILOMETER);
    }

    @Test
    public void shouldFindByQuantityPropertyOfDistanceCustomUserType() {
        testUtil.executeInit();

        List<DeliveryRequest> requests =
                getAndCast(em.createQuery(
                        "from DeliveryRequest d " +
                                "where d.airDistance.quantity = 15 or d.landDistance.quantity = 15")
                        .getResultList());

        Assertions.assertEquals(requests.size(), 2);
    }

    private class DistanceUserTypeUtil implements HibernateUtil<DeliveryRequest> {

        @Override
        public List<DeliveryRequest> prepareTestData() {
            List<DeliveryRequest> requests = new ArrayList<>();

            DeliveryRequest dr1 = new DeliveryRequest(deliveryRequest);
            dr1.setLandDistance(Distance.fromString("15 km"));
            DeliveryRequest dr2 = new DeliveryRequest(deliveryRequest);
            dr2.setAirDistance(Distance.fromString("15 mi"));

            requests.add(dr1);
            requests.add(dr2);

            return requests;
        }

        @Override
        public void storeTestData(List<DeliveryRequest> requests) {
            for (DeliveryRequest request : requests) {
                em.persist(request);
            }
            em.flush();
        }
    }
}
