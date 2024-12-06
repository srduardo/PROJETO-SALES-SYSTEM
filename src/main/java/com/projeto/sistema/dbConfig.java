package com.projeto.sistema;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class dbConfig {
    @Bean
    public DataSource dataSource() {

        String url = System.getenv("DATABASE_URL");
        String user = System.getenv("DATABASE_USER");
        String password = System.getenv("DATABASE_PASSWORD");

        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(false); // Mostrar o sql no console
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        adapter.setPrepareConnection(true);

        return adapter;
    }
}
