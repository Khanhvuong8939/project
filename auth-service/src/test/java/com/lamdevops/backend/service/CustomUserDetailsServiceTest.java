package com.lamdevops.backend.service;

import com.lamdevops.BaseTest;
import com.lamdevops.backend.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CustomUserDetailsServiceTest extends BaseTest{

    @Autowired
    CustomUserDetailsService detailsService;

    @Test
    public void loadUserByUsername() throws Exception {

        User user = (User) detailsService.loadUserByUsername("lam.nm");
        System.out.println(user);

    }

}