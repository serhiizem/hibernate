package org.hibernate.tutorials.model.inheritance.single_table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BILLING_DETAILS")
@DiscriminatorColumn(name = "BD_TYPE")
@OptimisticLocking(type = OptimisticLockType.ALL)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BillingDetails {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String owner;
}
