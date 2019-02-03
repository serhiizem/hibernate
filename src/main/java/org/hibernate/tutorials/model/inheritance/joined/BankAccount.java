package org.hibernate.tutorials.model.inheritance.joined;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "J_BANK_ACCOUNT")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "BA_ID")
public class BankAccount extends BillingDetails {
    @NotNull
    private String account;
    @NotNull
    private String bankName;
    @NotNull
    private String swift;
}
