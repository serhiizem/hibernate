package org.hibernate.tutorials.model.advanced;

import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Getter
@Immutable
@Subselect(value =
        //@formatter:off
        "SELECT u.USERNAME AS USERNAME, COUNT(o.ID) AS NUMBEROFORDERS" +
        "FROM sys_user u LEFT JOIN orders o " +
        "ON u.id = o.user_id " +
        "GROUP BY username"
        //@formatter:on
)
@Synchronize({"User", "Order"})
public class UserOrderSummary {
    private String username;
    private int numberOfOrders;
}
