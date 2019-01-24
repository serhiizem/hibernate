@GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(
                        name = "sequence_name",
                        value = "hibernate_tutorial_seq"
                ),
                @Parameter(
                        name = "initial_value",
                        value = "1000"
                )
        })

@TypeDefs({
        @TypeDef(
                name = "mi_distance",
                typeClass = Distance.class,
                parameters = {
                        @Parameter(
                                name = "convertTo",
                                value = "mi"
                        )
                }
        ),
        @TypeDef(
                name = "km_distance",
                typeClass = Distance.class,
                parameters = {@Parameter(name = "convertTo", value = "km")}
        )}
)
package org.hibernate.tutorials.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;