package com.venky.authservicerefactor1.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Password {

    @Id
    @Column(unique = true)
    private String id;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    private String userId;

    public Password(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return null == id ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || getClass() != o.getClass()) {
            return false;
        }
        Password password = (Password) o;
        return password.id == this.id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
