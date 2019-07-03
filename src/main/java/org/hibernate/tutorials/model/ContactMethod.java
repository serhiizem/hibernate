package org.hibernate.tutorials.model;

public enum ContactMethod {
    EMAIL,
    PHONE,
    SKYPE;

    public static ContactMethod fromString(String input) {
        for (ContactMethod contactMethod : ContactMethod.values()) {
            if (contactMethod.name().equals(input)) return contactMethod;
        }
        return null;
    }
}
