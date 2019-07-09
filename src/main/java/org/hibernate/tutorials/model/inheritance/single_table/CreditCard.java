package org.hibernate.tutorials.model.inheritance.single_table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
@DynamicUpdate
@DiscriminatorValue(value = "CC")
@EqualsAndHashCode(callSuper = true)
public class CreditCard extends BillingDetails {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expMonth;
    @NotNull
    private String expYear;
}
