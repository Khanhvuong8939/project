package com.venky.authservicerefactor1.backend.domain.master;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String _id;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    protected Date modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private boolean enabled;

    public String get_id() {
        return _id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj || getClass() != obj.getClass()) return false;

        BaseModel baseModel = (BaseModel) obj;

        return _id == baseModel._id;
    }
}
