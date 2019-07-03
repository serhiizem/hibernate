package org.hibernate.tutorials.model.embeddable;

import lombok.AllArgsConstructor;
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
                column = @Column(name = "WEIGHT_NAME")
        ),
        @AttributeOverride(
                name = "symbol",
                column = @Column(name = "WEIGHT_SYMBOL")
        ),
})
public class Weight extends Measurement {
    @NotNull
    @Column(name = "WEIGHT")
    private BigDecimal value;

    public Weight(@NotNull String name, @NotNull String symbol, @NotNull BigDecimal value) {
        super(name, symbol);
        this.value = value;
    }
}
