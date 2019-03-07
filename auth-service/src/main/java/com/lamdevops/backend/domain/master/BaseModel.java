package com.lamdevops.backend.domain.master;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;

public class BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    protected String _id;

    @Version
    private Long version;

    @LastModifiedDate
    protected long modifiedDate;

    @Column(updatable = false)
    @CreatedDate
    protected long createdDate;

    @Column(updatable = false)
    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String modifiedBy;

    protected boolean enabled = true;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getVersion() {
        return version;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public long getCreatedDate() { return createdDate; }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id=" + _id +
                ", modifiedDate=" + modifiedDate +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        return _id == baseModel._id;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }
}
