package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LengthUnit {
    KILOMETER("km"),
    MILE("mi");

    private String name;

    public static LengthUnit fromString(String name) {
        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.name.equalsIgnoreCase(name)) {
                return unit;
            }
        }
        return null;
    }
}
