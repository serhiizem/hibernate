package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FileName {
    @NotNull
    @Column(nullable = false)
    private String fileName;
    @NotNull
    @Column(nullable = false)
    private String fileExtension;
}
