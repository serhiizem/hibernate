package org.hibernate;

import org.hibernate.tutorials.model.User;
import org.springframework.jdbc.core.RowMapper;

public final class TestConstants {

    public interface JdbcUtils {

        String GET_USER_BY_ID_STATEMENT = "SELECT USERNAME,CARD_NUMBER FROM USERS WHERE ID = ?";

        RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
            String userName = rs.getString("USERNAME");
            String cardNumber = rs.getString("CARD_NUMBER");
            return new User(userName, cardNumber);
        };
    }
}
