package org.hibernate.tutorials.model.inheritance.joined;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "JOINED_BILLING_DETAILS")
@Inheritance(strategy = InheritanceType.JOINED)
public class BillingDetails {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String owner;
}
