package org.hibernate.tutorials.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull //ignored for DDL generation
    @Column(nullable = false)
    private String street;

    @NotNull
    @Column(nullable = false)
    private String zipCode;

    @NotNull
    @Column(nullable = false)
    private String city;
}
