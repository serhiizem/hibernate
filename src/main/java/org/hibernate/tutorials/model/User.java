package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "SYS_USER")
@NoArgsConstructor
@AllArgsConstructor
public class User extends PersistentEntity {
    private String userName;

    @Column(name = "CARD_NUMBER")
    @ColumnTransformer(
            write = "pgp_sym_encrypt(?, 'mySecretKey')"
    )
    private String creditCardNumber;
}
