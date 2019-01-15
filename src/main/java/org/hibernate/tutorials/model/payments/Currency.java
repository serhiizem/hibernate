package org.hibernate.tutorials.model.payments;

public enum Currency {
    EUR,
    USD;

    public static Currency fromString(String input) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equals(input)) {
                return currency;
            }
        }
        return null;
    }
}
