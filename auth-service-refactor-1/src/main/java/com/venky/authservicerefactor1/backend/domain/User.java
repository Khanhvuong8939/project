package com.venky.authservicerefactor1.backend.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class User extends BaseUser implements UserDetails {

    @NotNull
    @Size(min = 3, max = 100)
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities = roles.stream().map(i-> new SimpleGrantedAuthority(i.getName())).collect(Collectors.toSet());

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.getUsername();
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
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return null != _id ? _id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return this._id == ((User) obj)._id;
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
