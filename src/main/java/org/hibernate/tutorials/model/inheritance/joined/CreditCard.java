package org.hibernate.tutorials.model.inheritance.joined;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "JOINED_CREDIT_CARD")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "CC_ID")
public class CreditCard extends BillingDetails {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expMonth;
    @NotNull
    private String expYear;
}
