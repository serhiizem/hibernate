package org.hibernate.tutorials.model;

public enum RequestStatus {
    PROCESSING,
    CONFIRMED,
    DELIVERING,
    DELIVERED;

    public static RequestStatus fromString(String name) {
        for (RequestStatus requestStatus : RequestStatus.values()) {
            if (requestStatus.toString().equalsIgnoreCase(name)) {
                return requestStatus;
            }
        }
        return null;
    }
}
