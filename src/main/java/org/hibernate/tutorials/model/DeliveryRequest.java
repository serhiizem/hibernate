package org.hibernate.tutorials.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.OrderBy;
import org.hibernate.tutorials.model.converters.MonetaryAmountConverter;
import org.hibernate.tutorials.model.embeddable.Address;
import org.hibernate.tutorials.model.embeddable.Comment;
import org.hibernate.tutorials.model.embeddable.Dimensions;
import org.hibernate.tutorials.model.embeddable.Weight;
import org.hibernate.tutorials.model.payments.MonetaryAmount;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Getter
@Entity
@Table(name = "REQUESTS")
@NoArgsConstructor
public class DeliveryRequest extends PersistentEntity {

    private String description;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @Type(type = "mi_distance")
    @Columns(columns = {
            @Column(name = "AIR_DISTANCE"),
            @Column(name = "AIR_DISTANCE_UNIT")
    })
    private Distance airDistance;

    @Type(type = "km_distance")
    @Columns(columns = {
            @Column(name = "LAND_DISTANCE"),
            @Column(name = "LAND_DISTANCE_UNIT")
    })
    private Distance landDistance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE", insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    private Date lastModifiedDate; //old java.util.Date is used only for sake of studying @Temporal

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE",
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_DATE NOT NULL")
    @Generated(GenerationTime.INSERT)
    private Date creationDate; //old java.util.Date is used only for sake of studying @Temporal

    @AttributeOverrides({
            @AttributeOverride(
                    name = "street",
                    column = @Column(name = "FROM_STREET", nullable = false)),
            @AttributeOverride(
                    name = "zipCode",
                    column = @Column(name = "FROM_ZIP_CODE", nullable = false)),
            @AttributeOverride(
                    name = "city",
                    column = @Column(name = "FROM_CITY", nullable = false))
    })
    private Address fromAddress;

    private Address deliveryAddress;

    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount price;

    private Dimensions dimensions;
    private Weight weight;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "comments",
            joinColumns = {
                    @JoinColumn(name = "request_id")
            })
    @OrderBy(clause = "DATE ASC")
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "notification_recipients",
            joinColumns = {
                    @JoinColumn(name = "request_id")
            })
    @Column(name = "email")
    private Set<String> notificationRecipients = new HashSet<>();

    public DeliveryRequest(String description, RequestStatus status,
                           Address fromAddress, Address deliveryAddress,
                           MonetaryAmount price, Dimensions dimensions, Weight weight) {
        this.description = description;
        this.status = status;
        this.fromAddress = fromAddress;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public DeliveryRequest(DeliveryRequest deliveryRequest) {
        this(deliveryRequest.getDescription(), deliveryRequest.getStatus(), deliveryRequest.getFromAddress(),
                deliveryRequest.getDeliveryAddress(), deliveryRequest.getPrice(), deliveryRequest.getDimensions(),
                deliveryRequest.getWeight());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAirDistance(Distance airDistance) {
        this.airDistance = airDistance;
    }

    public void setLandDistance(Distance landDistance) {
        this.landDistance = landDistance;
    }
}
