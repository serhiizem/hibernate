package org.hibernate.tutorials.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Immutable
@Subselect(value = "")
@Synchronize({"User", "Order"})
public class UserOrderSummary {
}
