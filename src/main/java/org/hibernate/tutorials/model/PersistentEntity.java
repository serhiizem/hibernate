package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersistentEntity {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
}
