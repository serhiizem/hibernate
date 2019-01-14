package org.hibernate;

import org.hibernate.tutorials.model.Address;
import org.hibernate.tutorials.model.Request;
import org.hibernate.tutorials.model.User;
import org.springframework.jdbc.core.RowMapper;

import static org.hibernate.tutorials.model.RequestStatus.PROCESSING;

public final class TestConstants {

    public static final Address DEFAULT_DELIVERY_ADDRESS =
            new Address("Street #2222", "5326", "City #22");
    public static final Address DEFAULT_FROM_ADDRESS =
            new Address("Street #1111", "6235", "City #11");

    public static final Request DEFAULT_TEST_REQUEST =
            new Request("Test description", PROCESSING,
                    DEFAULT_FROM_ADDRESS, DEFAULT_DELIVERY_ADDRESS);

    public interface JdbcUtils {

        String GET_USER_BY_ID_STATEMENT = "SELECT USERNAME,CARD_NUMBER FROM USERS WHERE ID = ?";
        String GET_REQUEST_STATUS_BY_REQUEST_ID_STATEMENT = "SELECT STATUS FROM REQUESTS WHERE ID = ?";

        RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
            String userName = rs.getString("USERNAME");
            String cardNumber = rs.getString("CARD_NUMBER");
            return new User(userName, cardNumber);
        };
    }
}
