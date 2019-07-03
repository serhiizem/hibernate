package org.hibernate.tutorials.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @NotNull
    private String name;
    @NotNull
    private String symbol;
}
