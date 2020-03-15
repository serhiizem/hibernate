package org.hibernate.tutorials.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BILLS")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Bill extends PersistentEntity {

    @NotNull
    private String title;

    @NotNull
    private BigDecimal amount;
}
