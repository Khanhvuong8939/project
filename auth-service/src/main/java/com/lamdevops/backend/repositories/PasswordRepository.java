package com.lamdevops.backend.repositories;

import com.lamdevops.backend.domain.Password;
import com.lamdevops.backend.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Repository
public interface PasswordRepository extends CrudRepository<Password, Long> {

    /**
     * Returns Passowrd a given by username or null if not found
     * @param user a given by user
     * @return A passowrd a given by username or null if not found
     */
    public List<Password> findByUserAndEnabled(User user, Boolean enabled);


    /**
     * Find list password by user
     * @param user
     * @return A list password or null if not exists
     */
    public List<Password> findByUserId(String userId);
}
