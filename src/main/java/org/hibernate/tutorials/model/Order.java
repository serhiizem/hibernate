package org.hibernate.tutorials.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    private String name;
}
