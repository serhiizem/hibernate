package org.hibernate.tutorials.model;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
public abstract class PersistentEntity {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
}
