package org.hibernate.tutorials.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.tutorials.model.converters.DistanceConverter;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.hibernate.tutorials.model.LengthUnit.KILOMETER;
import static org.hibernate.type.StandardBasicTypes.DOUBLE;
import static org.hibernate.type.StandardBasicTypes.STRING;

public class DistanceUserType implements DynamicParameterizedType, CompositeUserType {

    private LengthUnit convertTo;

    @Override
    public void setParameterValues(Properties parameters) {
        String lengthUnitName = parameters.getProperty("convertTo");
        LengthUnit lengthUnit = LengthUnit.fromString(lengthUnitName);
        this.convertTo = lengthUnit != null ? lengthUnit : KILOMETER;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"name", "value"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{DOUBLE, STRING};
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        Distance distance = (Distance) component;
        if (property == 0) {
            return distance.getQuantity();
        } else {
            return distance.getUnit();
        }
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        Distance distance = (Distance) component;
        if (property == 0) {
            Double quantity = (Double) value;
            distance.setQuantity(quantity);
        } else {
            LengthUnit lengthUnit = LengthUnit.fromString((String) value);
            distance.setUnit(lengthUnit);
        }
    }

    @Override
    public Class returnedClass() {
        return Distance.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session,
                              Object owner) throws HibernateException, SQLException {
        double quantity = rs.getDouble(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        String unitName = rs.getString(names[1]);
        LengthUnit lengthUnit = LengthUnit.fromString(unitName);

        return new Distance(quantity, lengthUnit);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, DOUBLE.sqlType());
            st.setNull(index + 1, STRING.sqlType());
        } else {
            Distance distance = (Distance) value;
            Distance result = DistanceConverter.convert(distance, this.convertTo);
            st.setDouble(index, result.getQuantity());
            st.setString(index + 1, result.getUnit().getName());
        }
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
        return Distance.fromString((String) cached);
    }

    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return original;
    }
}
