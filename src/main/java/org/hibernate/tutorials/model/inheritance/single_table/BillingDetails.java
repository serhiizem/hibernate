package org.hibernate.tutorials.model.inheritance.single_table;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@DiscriminatorColumn(name = "BD_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BillingDetails {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String owner;
}
