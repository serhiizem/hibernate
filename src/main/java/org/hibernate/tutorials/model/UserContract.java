package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Date;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserContract {
    private Date generationDate;
    private Date signingDate;
}
