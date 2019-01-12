package org.hibernate.tutorials.model;

import lombok.Getter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "ORDERS")
public class Order extends PersistentEntity {
    private String name;
    @Formula("SUBSTRING(DESCRIPTION,0,10) || '...'")
    private String shortDescription;
}
