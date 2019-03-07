package com.lamdevops.backend.service;

import com.lamdevops.backend.domain.User;
import com.lamdevops.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DefaultTokenServices tokenService;

    /**
     * Create a user
     *
     * @param user
     */
    public User createUser(User user) {

        user = Optional.ofNullable(user)
                .map(u -> {
                    String encodedPassword = passwordEncoder.encode(u.getPassword());
                    u.setPassword(encodedPassword);

                    userRepository.insert(u);
                    passwordService.createNewPassword(u.get_id(), encodedPassword);

                    return u;
                }).orElse(null);

        return user;
    }


    /**
     * Find user by id
     *
     * @param id given by user
     * @return a user if null if not found
     */
    public User getOne(String id) {
        return userRepository.findOne(id);
    }


    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    /**
     * Update user
     *
     * @param user
     * @return user after update
     */
    public User updateUser(User user) {

        boolean isReAuthorize = false;
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            isReAuthorize = true;
        }

        user = userRepository.save(user);

        if (isReAuthorize) {
            Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName("browser", user.getUsername());
            tokens.stream().forEach(token -> tokenService.revokeToken(token.getValue()));
        }

        return user;
    }

    /**
     * Checking user matches password.
     *
     * @param username
     * @param currentPassword
     * @return a user if matching or null
     */
    public User checkingPassword(String username, String currentPassword) {

        User user = Optional.ofNullable(userRepository.findByUsername(username))
                    .filter(u -> passwordEncoder.matches(currentPassword, u.getPassword())).orElse(null);
        return user;
    }

    public void changePassword(String username, String newPassword) {
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getByRole(String roleName, Pageable pageable) {
        return userRepository.findByRoles_Name(roleName, pageable);
    }

    public Page<User> getByRoles(List<String> roleNames, Pageable pageable) {
        return userRepository.findUserDistinctByRoles_NameIn(roleNames, pageable);
    }

    public boolean duplicatedEmail(String email) {

        if(null != userRepository.findByEmail(email))
            return true;
        return false;
    }

    public boolean duplicatedUsername(String username) {

        if(null != userRepository.findByUsername(username))
            return true;
        return false;
    }

    public boolean matchingPassword(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    public User getByUsernameAndPassword(String username, String password) {
        return Optional.of(userRepository.findByUsername(username))
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(null);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
