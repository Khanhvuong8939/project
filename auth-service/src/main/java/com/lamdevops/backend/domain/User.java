package com.lamdevops.backend.domain;

import com.lamdevops.enums.RolesEnum;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mr Lam on 12/14/2016.
 */
@Entity
@Document(collection = "users")
public class User extends BaseUser implements UserDetails {


    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    public User() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(RolesEnum role) {
        this.roles.add(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.forEach(ur -> authorities.add(new Authority(ur.getName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return _id == user._id;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", roles=" + roles +
                '}';
    }
}
