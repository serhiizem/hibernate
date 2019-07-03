package org.hibernate.tutorials.model.inheritance.table_per_class;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "TABLE_PER_CLASS_CC")
@EqualsAndHashCode(callSuper = true)
public class CreditCard extends BillingDetails {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expMonth;
    @NotNull
    private String expYear;
    @NotNull
    private String ccOwner;
}
