package com.lamdevops.controller;

import com.google.gson.Gson;
import com.lamdevops.backend.domain.Message;
import com.lamdevops.backend.domain.User;
import com.lamdevops.backend.domain.BaseUser;
import com.lamdevops.backend.service.PasswordService;
import com.lamdevops.backend.service.UserService;
import com.lamdevops.enums.MessageEnum;
import com.lamdevops.enums.RolesEnum;
import com.lamdevops.exception.Exception;
import com.lamdevops.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by lamdevops on 8/15/17.
 */
@RestController
@RequestMapping(UserController.USER_V1_URI)
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String USER_V1_URI = "/v1/user";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DefaultTokenServices tokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;


    /**
     * Gets user info of current user login.
     *
     * @param principal
     * @return principal store info user info.
     */
    @GetMapping(value = "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @GetMapping("/revoke")
    public void revoke(Principal principal) {
        OAuth2Authentication auth2Authentication = (OAuth2Authentication) principal;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth2Authentication.getDetails();
        tokenServices.setTokenEnhancer(jwtTokenEnhancer);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.revokeToken(details.getTokenValue());
    }

    @GetMapping("/access-token")
    public ResponseEntity<Object> getAccessToken(Principal principal) {

        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientIdAndUserName("browser", principal.getName());
        System.out.println("Number access tokens:"  + accessTokens.size());
        return new ResponseEntity<>(accessTokens.toString(), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('server') or #oauth2.hasScope('openid')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {

        // reset id make sure the id is right format id
        user.set_id(null);

        //TODO check duplicated email
        if (userService.duplicatedEmail(user.getEmail())) {
            throw new Exception(new Message(MessageEnum.USER_EMAIL_INVALID, user.getEmail()));
        }

        //TODO check duplicated username
        if (userService.duplicatedUsername(user.getUsername())) {
            throw new Exception(new Message(MessageEnum.USER_USERNAME_INVALID, user.getUsername()));
        }

        //TODO if roles is null set default role is CUSTOMER
        if (user.getRoles().isEmpty()) user.setRole(RolesEnum.CUSTOMER);

        //TODO throw null exception if password is null
        if (user.getPassword() == null) {
            throw new Exception(new Message(MessageEnum.USER_PASSWORD_NOT_EMPTY));
        }

        try {
            // Create new user
            userService.createUser(user);

            LOGGER.info("Created user {}", user.toString());

            return new ResponseEntity<>(new BaseUser(user), HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("{}", e);
        }

        return new ResponseEntity<>(new Message(MessageEnum.USER_CREATE_ERROR),
                HttpStatus.BAD_REQUEST);
    }


    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@RequestBody User user, Locale locale) {

        User localUser = userService.getOne(user.get_id());

        // TODO checking user was not found
        if (localUser == null)
            throw new NotFoundException(new Message(MessageEnum.USER_ID_NOT_FOUND, user.get_id()));

        updateUser(localUser, user, locale);

        return new ResponseEntity<>(new Message(MessageEnum.USER_UPDATED_SUCCESSFULLY, user.get_id()),
                HttpStatus.OK);
    }


    @PreAuthorize("#oauth2.hasScope('server') or #oauth2.hasScope('openid')")
    @PutMapping(value = "/by/username", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateByUsername(@RequestBody User user, Locale locale) {

        User localUser = Optional.ofNullable(userService.getByUsername(user.getUsername()))
                .orElseThrow(() ->
                    new NotFoundException(
                            new Message(MessageEnum.USER_UPDATE_ERROR, user.getUsername())));


        updateUser(localUser, user, locale);

        return new ResponseEntity<>(new Message(MessageEnum.USER_UPDATED_SUCCESSFULLY), HttpStatus.OK);
    }


    @PreAuthorize("#oauth2.hasScope('server') or #oauth2.hasScope('openid')")
    @GetMapping("/by/username/{username:.+}")
    public ResponseEntity<Object> getByUsername(@PathVariable String username) {
        User user = Optional.ofNullable(userService.getByUsername(username))
                .orElseThrow(() -> new NotFoundException(new Message(MessageEnum.USER_USERNAME_NOT_FOUND, username)));

        return new ResponseEntity<>(new BaseUser(user), HttpStatus.OK);
    }


    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping(value = "/by/email")
    public ResponseEntity<Object> getByEmail(@RequestParam("email") String email, Locale locale) {
        User user = Optional.ofNullable(userService.getByEmail(email))
                .orElseThrow(() -> new NotFoundException(new Message(MessageEnum.USER_EMAIL_NOT_FOUND, email)));

        return new ResponseEntity<>(new BaseUser(user), HttpStatus.OK);
    }


    private void updateUser(User localUser, User user, Locale locale) {
        //TODO check duplicated email
        if ((user.getEmail() != null) && !user.getEmail().equals(localUser.getEmail())
                && userService.duplicatedEmail(user.getEmail())) {
            throw new Exception(new Message(MessageEnum.USER_USERNAME_INVALID, user.getEmail()));
        }

        // TODO change password
        if (user.getPassword() != null) {
            long timeUsedOldPass = passwordService.existedPassword(localUser.get_id(), user.getPassword());
            if (!passwordService.matches(user.getPassword(), localUser.getPassword()) && timeUsedOldPass > 0) {
                int dayUsedPass = LocalDateTime.now().minusNanos(timeUsedOldPass).getDayOfYear();
                throw new Exception(new Message(MessageEnum.USER_PASSWORD_HAS_BEEN_USED, String.valueOf(dayUsedPass)));
            }
        }

        localUser.setEnabled(user.isEnabled());
        localUser.setRoles(user.getRoles());

        try {
            userService.updateUser(localUser);
        } catch (Exception e) {
            LOGGER.error("{}", e);
            throw new Exception(new Message(MessageEnum.USER_UPDATE_ERROR));
        }
    }
}
