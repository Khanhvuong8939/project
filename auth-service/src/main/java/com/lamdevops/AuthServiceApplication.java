package com.lamdevops;

import com.lamdevops.backend.domain.User;
import com.lamdevops.backend.service.UserService;
import com.lamdevops.enums.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableDiscoveryClient
@SpringBootApplication
public class AuthServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Autowired
	private UserService userService;

	@Override
	public void run(String... strings) throws Exception {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("123456");
		user.setEnabled(true);
		user.setRole(RolesEnum.ADMIN);

		userService.createUser(user);
	}
}
