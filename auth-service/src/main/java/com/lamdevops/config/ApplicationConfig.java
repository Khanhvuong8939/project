package com.lamdevops.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.lamdevops.backend.repositories")
@EnableMongoAuditing
public class ApplicationConfig {


}
