package org.hibernate.tutorials.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.util.Date;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String author;
    private String text;
    private Date date;
}
