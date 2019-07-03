package org.hibernate.tutorials.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Date date;
}
