package org.hibernate.tutorials.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "REQUESTS")
@NoArgsConstructor
public class Request extends PersistentEntity {

    private String description;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

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

    public Request(String description, RequestStatus status,
                   Address fromAddress, Address deliveryAddress) {
        this.description = description;
        this.status = status;
        this.fromAddress = fromAddress;
        this.deliveryAddress = deliveryAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
