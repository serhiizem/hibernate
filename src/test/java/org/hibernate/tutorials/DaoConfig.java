package org.hibernate.tutorials;

import org.hibernate.utils.ConcurrencyUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DaoConfig {

    @Bean
    public ConcurrencyUtils concurrencyUtils() {
        return new ConcurrencyUtils();
    }
}
