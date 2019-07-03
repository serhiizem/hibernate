package org.hibernate.tutorials.model.inheritance.table_per_class;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "TABLE_PER_CLASS_BA")
@EqualsAndHashCode(callSuper = true)
public class BankAccount extends BillingDetails {
    @NotNull
    private String account;
    @NotNull
    private String bankName;
    @NotNull
    private String swift;
    @NotNull
    private String owner;
}
