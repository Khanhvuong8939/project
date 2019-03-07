package com.venky.authservicerefactor1.backend.domain;

import com.venky.authservicerefactor1.backend.domain.master.BaseModel;
import com.venky.authservicerefactor1.enums.RolesEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BaseUser extends BaseModel {

    @Email
    @NotNull
    @Size(min=3, max = 100)
    @Column(unique = true)
    protected String email;

    @NotNull
    @Size(min=3, max = 100)
    @Column(unique = true)
    protected String username;

    @ManyToMany
    protected Set<Role> roles = new HashSet<>();

    public BaseUser() {
    }

    public BaseUser(@Email @NotNull @Size(min = 3, max = 100) String email, @NotNull @Size(min = 3, max = 100) String username, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
