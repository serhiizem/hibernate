package org.hibernate.tutorials.model.advanced;

import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Immutable
@Subselect(value =
        //@formatter:off
        "SELECT u.ID AS ID, u.USER_NAME AS USER_NAME, COUNT(o.ID) AS NUMBER_OF_ORDERS " +
        "FROM users u LEFT JOIN orders o " +
        "ON u.id = o.user_id " +
        "GROUP BY u.ID, u.USER_NAME"
        //@formatter:on
)
@Synchronize({"User", "Order"})
public class UserOrderSummary {
    @Id
    private Long id;
    private String userName;
    private int numberOfOrders;
}
