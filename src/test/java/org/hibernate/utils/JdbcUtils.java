package org.hibernate.utils;

import org.hibernate.tutorials.model.User;
import org.springframework.jdbc.core.RowMapper;

public interface JdbcUtils {

    String GET_USER_BY_ID_STATEMENT = "SELECT USER_NAME,CARD_NUMBER FROM USERS WHERE ID = ?";
    String GET_REQUEST_STATUS_BY_REQUEST_ID_STATEMENT = "SELECT STATUS FROM REQUESTS WHERE ID = ?";
    String GET_REQUEST_PRICE_BY_REQUEST_ID_STATEMENT = "SELECT PRICE FROM REQUESTS WHERE ID = ?";
    String GET_AIR_DISTANCE = "SELECT AIR_DISTANCE FROM REQUESTS WHERE ID = ?";
    String GET_AIR_DISTANCE_UNIT = "SELECT AIR_DISTANCE_UNIT FROM REQUESTS WHERE ID = ?";
    String GET_LAND_DISTANCE = "SELECT LAND_DISTANCE FROM REQUESTS WHERE ID = ?";
    String GET_LAND_DISTANCE_UNIT = "SELECT LAND_DISTANCE_UNIT FROM REQUESTS WHERE ID = ?";

    RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
        String userName = rs.getString("USER_NAME");
        String cardNumber = rs.getString("CARD_NUMBER");
        return new User(userName, cardNumber);
    };
}
