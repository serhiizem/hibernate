package org.hibernate.tutorials.model.converters;

import org.hibernate.tutorials.model.payments.MonetaryAmount;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        return attribute.toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        return MonetaryAmount.fromString(dbData);
    }
}
