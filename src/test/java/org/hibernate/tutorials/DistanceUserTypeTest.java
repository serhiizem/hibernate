package org.hibernate.tutorials;

import org.hibernate.tutorials.model.Distance;
import org.hibernate.tutorials.model.LengthUnit;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Commit;

import static java.lang.Double.parseDouble;
import static org.hibernate.JdbcUtils.*;

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
    @Commit
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
}
