package org.hibernate.tutorials.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "SYS_USER")
public class User {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    private String userName;
}
