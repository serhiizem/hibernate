package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.tutorials.model.inheritance.joined.BillingDetails;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
public class User extends PersistentEntity {
    private String userName;

    @Column(name = "CARD_NUMBER", columnDefinition = "bytea")
    @ColumnTransformer(
            read = "pgp_sym_decrypt(CARD_NUMBER, 'mySecretKey')",
            write = "pgp_sym_encrypt(?, 'mySecretKey')"
    )
    private String creditCardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private BillingDetails defaultBillingDetails;

    @ElementCollection
    @Column(name = "value")
    @MapKeyClass(ContactMethod.class)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "method_type")
    @CollectionTable(name = "contact_methods")
    private Map<ContactMethod, String> contactMethods;

    public User(String userName, String creditCardNumber) {
        this.userName = userName;
        this.creditCardNumber = creditCardNumber;
    }
}
