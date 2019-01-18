package org.hibernate.tutorials.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.tutorials.model.payments.MonetaryAmount;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Properties;

public class MonetaryAmountUserType implements CompositeUserType, DynamicParameterizedType {

    protected Currency convertTo;

    @Override
    public String[] getPropertyNames() {
        return new String[0];
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[0];
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        return null;
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {

    }

    @Override
    public Class returnedClass() {
        return MonetaryAmount.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null || y == null) return false;
        if (x == y) return true;
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, StandardBasicTypes.BIG_DECIMAL.sqlType());
            st.setNull(index + 1, StandardBasicTypes.CURRENCY.sqlType());
        } else {
            MonetaryAmount ma = (MonetaryAmount) value;
            MonetaryAmount convertedAmount = this.convert(ma, convertTo);
            st.setBigDecimal(index, convertedAmount.getValue());
            st.setString(index + 1, convertTo.getCurrencyCode());
        }
    }

    private MonetaryAmount convert(MonetaryAmount monetaryAmount, Currency toCurrency) {
        return new MonetaryAmount(
                monetaryAmount.getValue().multiply(new BigDecimal(2)),
                toCurrency);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
        return value.toString();
    }

    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return MonetaryAmount.fromString((String) cached);
    }

    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        String convertToParameter = parameters.getProperty("convertTo");
        this.convertTo = Currency.getInstance(convertToParameter);
    }
}
