package org.hibernate.tutorials.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.tutorials.model.converters.MonetaryAmountConverter;
import org.hibernate.tutorials.model.payments.MonetaryAmount;

import javax.persistence.*;
import java.util.Date;

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

    public DeliveryRequest(String description, RequestStatus status,
                           Address fromAddress, Address deliveryAddress,
                           MonetaryAmount price) {
        this.description = description;
        this.status = status;
        this.fromAddress = fromAddress;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
    }

    public DeliveryRequest(DeliveryRequest deliveryRequest) {
        this(deliveryRequest.getDescription(), deliveryRequest.getStatus(), deliveryRequest.getFromAddress(),
                deliveryRequest.getDeliveryAddress(), deliveryRequest.getPrice());
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
