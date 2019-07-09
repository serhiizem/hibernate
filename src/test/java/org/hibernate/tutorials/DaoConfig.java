package org.hibernate.tutorials;

import org.hibernate.utils.ConcurrencyUtils;
import org.hibernate.utils.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

@TestConfiguration
public class DaoConfig {

    @Autowired
    private EntityManagerFactory emf;

    @Bean
    public ConcurrencyUtils concurrencyUtils() {
        return new ConcurrencyUtils();
    }

    @Bean
    public HibernateUtil hibernateUtil() {
    return new HibernateUtil(emf);
    }
}
