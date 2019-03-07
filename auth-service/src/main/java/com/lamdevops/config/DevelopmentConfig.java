package com.lamdevops.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Configuration
@Profile("dev")
@PropertySource("classpath:bootstrap-dev.yml")
public class DevelopmentConfig {

}
