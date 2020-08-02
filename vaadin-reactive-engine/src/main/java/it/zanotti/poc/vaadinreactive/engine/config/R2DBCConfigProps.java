package it.zanotti.poc.vaadinreactive.engine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * @author Michele Zanotti on 02/08/20
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "r2dbc")
public class R2DBCConfigProps {
    @NotBlank
    private String host;
    @NotBlank
    private String port;
    @NotBlank
    private String dbName;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
