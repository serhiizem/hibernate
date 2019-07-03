package org.hibernate.tutorials.model;

import lombok.Getter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Entity
@Table(name = "ORDERS")
public class Order extends PersistentEntity {
    private String name;
    @Formula("SUBSTRING(DESCRIPTION,0,10) || '...'")
    private String shortDescription;

    @ElementCollection
    @CollectionId(
            type = @Type(type = "long"),
            columns = {@Column(name = "IMAGE_ID")},
            generator = "ID_GENERATOR"
    )
    @CollectionTable(name = "IMAGES")
    private Collection<Image> orderImages = new ArrayList<>();
}
