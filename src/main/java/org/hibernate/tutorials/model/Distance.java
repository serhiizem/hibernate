package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Distance {
    private double quantity;
    private LengthUnit unit;

    public static Distance fromString(String value) {
        if (value == null) {
            return null;
        }
        String[] parts = value.split(" ");
        if (parts.length > 1) {
            double quantity = Double.valueOf(parts[0]);
            LengthUnit lengthUnit = LengthUnit.fromString(parts[1]);

            return new Distance(quantity, lengthUnit);
        }
        return null;
    }
}
