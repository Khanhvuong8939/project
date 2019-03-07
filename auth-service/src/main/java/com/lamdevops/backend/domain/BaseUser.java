package com.lamdevops.backend.domain;

import com.lamdevops.backend.domain.master.BaseModel;
import com.lamdevops.enums.RolesEnum;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BaseUser extends BaseModel {

    @Email
    @NotNull
    @Size(min = 5, max = 100)
    @Indexed(unique=true)
    protected String email;


    @NotNull
    @Size(min = 4, max = 50)
    @Indexed(unique=true)
    protected String username;

    @ElementCollection(targetClass = RolesEnum.class)
    @Enumerated(javax.persistence.EnumType.STRING)
    @CollectionTable(name = "user_role")
    @Column(name = "role")
    protected Set<RolesEnum> roles = new HashSet<>();

    public BaseUser() {
    }

    public BaseUser(User user) {
        Optional.of(user).ifPresent(u -> {
            this._id = user.get_id();
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.roles = user.getRoles();
            this.createdDate = user.getCreatedDate();
            this.modifiedDate = user.getModifiedDate();
            this.createdBy = user.getCreatedBy();
            this.modifiedBy = user.getModifiedBy();
        });
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RolesEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesEnum> roles) {
        this.roles = roles;
    }
}
