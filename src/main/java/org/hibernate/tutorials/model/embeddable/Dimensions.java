package org.hibernate.tutorials.model.embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Embeddable
@NoArgsConstructor
@AttributeOverrides(value = {
        @AttributeOverride(
                name = "name",
                column = @Column(name = "DIMENSIONS_NAME")
        ),
        @AttributeOverride(
                name = "symbol",
                column = @Column(name = "DIMENSIONS_SYMBOL")
        ),
})
public class Dimensions extends Measurement {
    @NotNull
    private BigDecimal height;
    @NotNull
    private BigDecimal width;
    @NotNull
    private BigDecimal length;

    public Dimensions(@NotNull String name, @NotNull String symbol, @NotNull BigDecimal height,
                      @NotNull BigDecimal width, @NotNull BigDecimal length) {
        super(name, symbol);
        this.height = height;
        this.width = width;
        this.length = length;
    }
}
