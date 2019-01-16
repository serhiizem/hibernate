package org.hibernate;

import org.hibernate.tutorials.model.User;
import org.springframework.jdbc.core.RowMapper;

public interface JdbcUtils {

    String GET_USER_BY_ID_STATEMENT = "SELECT USER_NAME,CARD_NUMBER FROM USERS WHERE ID = ?";
    String GET_REQUEST_STATUS_BY_REQUEST_ID_STATEMENT = "SELECT STATUS FROM REQUESTS WHERE ID = ?";
    String GET_REQUEST_PRICE_BY_REQUEST_ID_STATEMENT = "SELECT PRICE FROM REQUESTS WHERE ID = ?";

    RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
        String userName = rs.getString("USER_NAME");
        String cardNumber = rs.getString("CARD_NUMBER");
        return new User(userName, cardNumber);
    };
}
