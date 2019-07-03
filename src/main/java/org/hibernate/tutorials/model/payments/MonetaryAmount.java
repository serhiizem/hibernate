package org.hibernate.tutorials.model.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@AllArgsConstructor
public class MonetaryAmount {
    private BigDecimal value;
    private Currency currency;

    public static MonetaryAmount fromString(String input) {
        String[] parts = input.split(" ");
        BigDecimal value = new BigDecimal(parts[0]);
        String currencyName = parts[1];
        return new MonetaryAmount(
                value,
                Currency.getInstance(currencyName)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonetaryAmount)) return false;

        MonetaryAmount ma = (MonetaryAmount) o;
        if (!this.value.equals(ma.value)) return false;

        return this.currency.equals(ma.currency);
    }

    @Override
    public String toString() {
        return value + " " + currency;
    }
}
