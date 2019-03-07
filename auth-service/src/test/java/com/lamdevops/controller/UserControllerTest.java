package com.lamdevops.controller;

import com.lamdevops.BaseTest;
import com.lamdevops.backend.domain.User;
import com.lamdevops.enums.RolesEnum;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserControllerTest extends BaseTest {
    @Test
    public void getUser() throws Exception {
    }

    @Test
    public void create() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void updateByUsername() throws Exception {

        User user = new User();
        user.set_id("599ef0cc787b870bd83b1573");
        user.setUsername("lam.nm");
        user.setPassword("12345678");
        user.setEmail("lam.nm@gmail.com");
        user.setRole(RolesEnum.CUSTOMER);
        user.setRole(RolesEnum.ADMIN);
        User result = new User();
        restTemplate.put("http://localhost:"+ randomServerPort +"/uua/user/by/username", user, result);

        System.out.println(result);

    }

    @Test
    public void getByEmail() throws Exception {
        String email = "lam.nm@gmail.com";
        Object result = restTemplate.getForEntity("http://localhost:" + randomServerPort + "/uua/user/by/email?email={email}", User.class, email);

        assertNotNull(result);
    }

    @Test
    public void createUser() throws Exception {
        User user = new User();

        Object result = restTemplate.postForEntity("http://localhost:"+ randomServerPort +"/uua/v1/user", user, User.class);

        System.out.println(result);
    }

    @Test
    public void getByUserName1() throws Exception {
        String username = "lam.nm";
        Object result = restTemplate.getForEntity("http://localhost:" + randomServerPort + "/uua/user/by/username/{username}", User.class, username);

        assertNotNull(result);
    }

    @Test
    public void getByUserName2() throws Exception {
        String username = "lam.nm1";
        Object result = restTemplate.getForEntity("http://localhost:" + randomServerPort + "/uua/user/by/username/{username}", User.class, username);

        assertNull(result);
    }



}