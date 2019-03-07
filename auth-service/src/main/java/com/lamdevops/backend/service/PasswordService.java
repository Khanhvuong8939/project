package com.lamdevops.backend.service;

import com.lamdevops.backend.domain.Password;
import com.lamdevops.backend.domain.User;
import com.lamdevops.backend.repositories.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Service
public class PasswordService {

    @Autowired
    private PasswordRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Checking exist password
     * @param userId
     * @param password
     * @return true if password exist or null if not
     */
    public long existedPassword(String userId, String password){
        for (Password pwd : repository.findByUserId(userId)) {
            if(passwordEncoder.matches(password, pwd.getPassword()))
                return  pwd.getCreatedDate();
        }
        return 0;
    }


    /**
     * Create new password by user and new password
     * @param userId
     * @param newPassword
     * @return a password after created
     */
    public Password createNewPassword(String userId, String newPassword) {
        Password password = new Password(newPassword, LocalDateTime.now().getNano(), userId);

        return repository.save(password);
    }

    public boolean matches(String rawPass, String encryptPass) {
        return passwordEncoder.matches(rawPass, encryptPass);
    }
}
