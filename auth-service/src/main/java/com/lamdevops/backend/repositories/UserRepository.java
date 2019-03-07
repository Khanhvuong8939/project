package com.lamdevops.backend.repositories;

import com.lamdevops.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lam.nm on 8/14/2017.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    User findByUsername(String username);

    Page<User> findByRoles_Name(String roleName, Pageable pageable);

    Page<User> findUserDistinctByRoles_NameIn(List<String> roleNames, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);
}
