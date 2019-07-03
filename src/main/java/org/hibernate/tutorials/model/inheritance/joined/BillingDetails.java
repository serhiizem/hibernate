package org.hibernate.tutorials.model.inheritance.joined;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "JOINED_BILLING_DETAILS")
public class BillingDetails {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String owner;
}
