package com.lamdevops.config;

import com.lamdevops.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Configuration
public class Oauth2Config {

    private static final String SALT = "wjfaj9*($*#_$)#($)#$#$";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Configuration
    @EnableWebSecurity
    @Profile(value = {"dev", "prod"})
    protected static class webSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Configuration
    @EnableResourceServer
    @Profile(value = {"dev", "prod"})
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private DefaultTokenServices tokenServices;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resource) throws Exception {
            resource
                    .tokenServices(tokenServices);
        }
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableAuthorizationServer
    @Profile(value = {"dev", "prod"})
    protected static class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private CustomUserDetailsService detailsService;

        @Autowired
        private Environment env;

        @Autowired
        private TokenStore tokenStore;


        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        private TokenEnhancer jwtTokenEnhancer;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            clients.inMemory()
                    .withClient("browser")
                    .secret("61e1906e-c95c-4d80-ad73-30de5897f5bf")
                    .authorizedGrantTypes("password", "refresh_token", "authorization_code")
                    .scopes("openid")
                    .autoApprove(".*")
                  .and()
                    .withClient("account-service")
                    .secret(env.getProperty("service.password.account-service"))
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server");

        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            //set token enhancer chain
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));

            endpoints
                    .tokenStore(tokenStore)
                    .tokenEnhancer(tokenEnhancerChain)
                    .authenticationManager(authenticationManager)
                    .userDetailsService(detailsService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }

    }
}
