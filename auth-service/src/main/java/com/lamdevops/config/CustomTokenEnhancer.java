package com.lamdevops.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Value("${jwt.organization.name:devopslam}")
    private String organization;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(organization, authentication.getName() + UUID.randomUUID().toString().substring(0, 4));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
