package it.zanotti.poc.vaadinreactive.engine.config;

import io.r2dbc.spi.ConnectionFactory;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @author Michele Zanotti on 02/08/20
 **/
@Configuration
@EnableR2dbcRepositories("it.zanotti.poc.vaadinreactive.engine.db.repositories")
public class AppConfig extends AbstractR2dbcConfiguration {
    private final R2DBCConfigProps configProps;

    @Autowired
    public AppConfig(R2DBCConfigProps configProps) {
        this.configProps = configProps;
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        MariadbConnectionConfiguration config = MariadbConnectionConfiguration.builder()
                .database(configProps.getDbName())
                .host(configProps.getHost())
                .password(configProps.getPassword())
                .username(configProps.getUsername())
                .port(Integer.parseInt(configProps.getPort()))
                .build();
        return new MariadbConnectionFactory(config);
    }
}
