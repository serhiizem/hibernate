package org.hibernate.tutorials.model.inheritance.mixed;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SecondaryTable(
        name = "MIXED_CREDIT_CARD",
        pkJoinColumns = {
                @PrimaryKeyJoinColumn(name = "CC_ID")
        })
public class CreditCard extends BillingDetails {
    @NotNull
    @Column(table = "MIXED_CREDIT_CARD", nullable = false)
    private String cardNumber;
    @NotNull
    @Column(table = "MIXED_CREDIT_CARD", nullable = false)
    private String expMonth;
    @NotNull
    @Column(table = "MIXED_CREDIT_CARD", nullable = false)
    private String expYear;
}
