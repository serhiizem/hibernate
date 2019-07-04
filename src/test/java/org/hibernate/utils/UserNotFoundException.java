package org.hibernate.utils;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id %s was not found in the database", userId));
    }
}
