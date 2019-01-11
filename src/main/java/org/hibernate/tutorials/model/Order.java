package org.hibernate.tutorials.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "ORDERS")
public class Order extends PersistentEntity {
    private String name;
}
