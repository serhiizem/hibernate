package org.hibernate.tutorials.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "SYS_USER")
public class User extends PersistentEntity {
    private String userName;
}
