package com.lamdevops.backend.service;

import com.lamdevops.backend.domain.User;
import com.lamdevops.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return Optional.of(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
