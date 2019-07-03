package org.hibernate.tutorials.model.inheritance.mixed;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "MIXED_BILLING_DETAILS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BillingDetails {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String owner;
}
