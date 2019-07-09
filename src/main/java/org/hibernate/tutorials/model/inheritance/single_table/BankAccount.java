package org.hibernate.tutorials.model.inheritance.single_table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "BA")
@EqualsAndHashCode(callSuper = true)
public class BankAccount extends BillingDetails {
    @NotNull
    private String account;
    @NotNull
    private String bankName;
    @NotNull
    private String swift;

    public BankAccount(BankAccount bankAccount) {
        super(bankAccount.getId(), bankAccount.getOwner());
        this.account = bankAccount.getAccount();
        this.bankName = bankAccount.getBankName();
        this.swift = bankAccount.getSwift();
    }
}
