package org.hibernate;

import org.hibernate.tutorials.model.Request;
import org.hibernate.tutorials.model.RequestStatus;
import org.hibernate.tutorials.model.User;
import org.springframework.jdbc.core.RowMapper;

public final class TestConstants {

    public interface JdbcUtils {

        String GET_USER_BY_ID_STATEMENT = "SELECT USERNAME,CARD_NUMBER FROM USERS WHERE ID = ?";
        String GET_REQUEST_BY_ID_STATEMENT = "SELECT DESCRIPTION,STATUS FROM REQUESTS WHERE ID = ?";

        RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
            String userName = rs.getString("USERNAME");
            String cardNumber = rs.getString("CARD_NUMBER");
            return new User(userName, cardNumber);
        };

        RowMapper<Request> REQUEST_MAPPER = (rs, rowNum) -> {
            String description = rs.getString("DESCRIPTION");
            String status = rs.getString("STATUS");
            return new Request(description, RequestStatus.fromString(status));
        };
    }
}
