package com.lamdevops.backend.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

/**
 * Created by Mrs Hoang on 12/15/2016.
 */
@Document(collection = "passwords")
public class Password {

    @Id
    private String id;

    private String password;

    private boolean enabled = true;

    @CreatedDate
    private long createdDate;

    private String userId;

    public Password() {
    }

    public Password(String password, long createdDate, String userId) {
        this.password = password;
        this.createdDate = createdDate;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;

        return id != null ? id.equals(password.id) : password.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", createdDate=" + createdDate +
                ", user=" + userId +
                '}';
    }
}
