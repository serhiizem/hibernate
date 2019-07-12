package org.hibernate.tutorials.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
public class Order extends PersistentEntity {

    @Version
    @Type(type = "dbtimestamp")
    private Date version;

    @Setter
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

    public Order(Order order) {
        super(order.getId());
        this.version = order.getVersion();
        this.name = order.getName();
        this.orderImages = order.getOrderImages();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (!(other instanceof Order)) return false;
        Order order = (Order) other;

        return getName().equals(order.getName());
    }

    @Override
    public int hashCode() {
        return 31 * getName().hashCode();
    }
}
