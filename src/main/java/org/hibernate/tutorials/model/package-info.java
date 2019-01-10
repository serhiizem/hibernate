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
package org.hibernate.tutorials.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;