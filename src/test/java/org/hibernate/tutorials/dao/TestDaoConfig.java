package org.hibernate.tutorials.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import static java.util.Optional.ofNullable;

/**
 * Required to perform tests against real database instead of embedded H2/HSQL
 */
@ComponentScan({"org.hibernate.tutorials.model"})
@TestConfiguration
public class TestDaoConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.datasource.url"))
                .driverClassName(env.getProperty("spring.datasource.driver-class-name"))
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("telecom.model");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.jdbc.lob.non_contextual_creation", true);
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        String currentContextClass =
                ofNullable(env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"))
                .orElse("org.springframework.orm.hibernate5.SpringSessionContext");
        jpaProperties.put("hibernate.current_session_context_class", currentContextClass);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emfb) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emfb.getObject());
        return transactionManager;
    }
}
