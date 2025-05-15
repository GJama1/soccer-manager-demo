package com.example.orbitaldemo.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.constants.players-generation")
@Configuration
public class PlayerGenerationProperties {

    private int goalKeepers;

    private int defenders;

    private int midfielders;

    private int attackers;

    private int minAge;

    private int maxAge;

    private BigDecimal marketValue;

}
