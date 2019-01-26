package org.hibernate.tutorials.model.converters;

import org.hibernate.tutorials.model.Distance;
import org.hibernate.tutorials.model.LengthUnit;

import static org.hibernate.tutorials.model.LengthUnit.KILOMETER;
import static org.hibernate.tutorials.model.LengthUnit.MILE;

public final class DistanceConverter {

    private DistanceConverter() {
    }

    public static Distance convert(Distance distance, LengthUnit convertTo) {
        double quantity = distance.getQuantity();
        LengthUnit lengthUnit = distance.getUnit();
        if (KILOMETER.equals(lengthUnit) && KILOMETER.equals(convertTo)) {
            return distance;
        }
        if (KILOMETER.equals(lengthUnit) && MILE.equals(convertTo)) {
            double inMiles = quantity / 1.61D;
            distance.setQuantity(inMiles);
            distance.setUnit(LengthUnit.MILE);
            return distance;
        }
        if (MILE.equals(lengthUnit) && MILE.equals(convertTo)) {
            return distance;
        } else {
            double inKilometers = quantity * 1.61D;
            distance.setQuantity(inKilometers);
            distance.setUnit(LengthUnit.KILOMETER);
            return distance;
        }
    }
}
