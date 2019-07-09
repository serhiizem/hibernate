package org.hibernate.utils;

import org.hibernate.tutorials.model.DeliveryRequest;
import org.hibernate.tutorials.model.embeddable.Address;
import org.hibernate.tutorials.model.embeddable.Dimensions;
import org.hibernate.tutorials.model.embeddable.Weight;
import org.hibernate.tutorials.model.payments.MonetaryAmount;

import java.math.BigDecimal;
import java.util.Currency;

import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;

public class Constants {

    private Constants() {
    }

    private static final Address DEFAULT_DELIVERY_ADDRESS =
            new Address("Street #2222", "5326", "City #22");
    private static final Address DEFAULT_FROM_ADDRESS =
            new Address("Street #1111", "6235", "City #11");
    private static final MonetaryAmount PRICE = new MonetaryAmount(
            new BigDecimal("15.0"), Currency.getInstance("USD"));

    private static final Dimensions DEFAULT_DIMENSIONS =
            new Dimensions("centimetre", "cm", new BigDecimal("15.0"),
                    new BigDecimal("30.0"), new BigDecimal("20.0"));
    private static final Weight DEFAULT_WEIGHT =
            new Weight("pound", "lb", new BigDecimal("74.0"));

    public static final DeliveryRequest DEFAULT_TEST_REQUEST =
            new DeliveryRequest("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS, PRICE, DEFAULT_DIMENSIONS, DEFAULT_WEIGHT);

    public static final Long REQUEST_ID = 1179L;
    public static final Long USER_ID = 1L;
    public static final Long ORDER_ID = 1L;
    public static final Long BANK_ACCOUNT_ID = 1L;
}
