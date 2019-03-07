package com.lamdevops.backend.service;

import com.lamdevops.backend.domain.User;
import com.lamdevops.enums.RolesEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by lam.nm on 8/14/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Before
    public void init() throws Exception {

    }

    @Test
    public void createUser() throws Exception {

        User user = createSampleUser();
    }

    @Test
    public void updateUser() throws Exception {
        Set<RolesEnum> rolesEnums = new HashSet<>();
        rolesEnums.add(RolesEnum.ADMIN);
        User user = createSampleUser();
        user.setRoles(rolesEnums);
        userService.updateUser(user);
        System.out.println(user);
    }

    @Test
    public void matchingPassword() throws Exception {
        User user = userService.checkingPassword("lam.nm","12345678");
        assertNotNull(user);
    }

    @Test
    public void duplicatedEmail() throws Exception {
        String email = "nguyenlamit@gmail.com";
        boolean result = userService.duplicatedEmail(email);
        assertEquals(true, result);
    }

    @Test
    public void duplicatedUsername() throws Exception {
        String username = "lamnguyenit";
        boolean result = userService.duplicatedUsername(username);
        assertEquals(true, result);
    }


    public User createSampleUser() throws Exception{
        Set<RolesEnum> rolesEnums = new HashSet<>();
        rolesEnums.add(RolesEnum.ADMIN);
        rolesEnums.add(RolesEnum.CUSTOMER);

        User user = new User();
        user.set_id(null);
        user.setUsername("lam.nmai6");
        user.setPassword("12345678");
        user.setEmail("nguyenlam6@gmail.com");
        user.setRoles(rolesEnums);

        user = userService.createUser(user);
        System.out.println(user.toString());
        return user;
    }

}