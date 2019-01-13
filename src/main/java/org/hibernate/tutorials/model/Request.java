package org.hibernate.tutorials.model;

import lombok.Getter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "REQUESTS")
public class Request extends PersistentEntity {

    private String description;

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

    public Request(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
