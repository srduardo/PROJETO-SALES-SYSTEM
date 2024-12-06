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

        String host = System.getenv("DATABASE_HOST");
        String port = System.getenv("DATABASE_PORT");
        String name = System.getenv("DATABASE_NAME");
        String user = System.getenv("DATABASE_USER");
        String password = System.getenv("DATABASE_PASSWORD");

        if (host != null && port != null && name != null && user != null && password != null) {

            String jdbcUrl = String.format("jdbc:postgres://%s:%s/%s", host, port, name);

            return DataSourceBuilder.create().url(jdbcUrl).username(user).password(password).build();
        } else {
            throw new RuntimeException("Configuração do banco de dados não encontrada.");
        }
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
