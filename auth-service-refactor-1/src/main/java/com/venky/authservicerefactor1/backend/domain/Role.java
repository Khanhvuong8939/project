package com.venky.authservicerefactor1.backend.domain;

import com.venky.authservicerefactor1.backend.domain.master.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Role {

    private int id;
    private String name;
    private String description;
    private int priority;

    @ManyToMany(mappedBy = "roles")
    List<User> users = new ArrayList<>();

    Role(int id, String name, int priority, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

}
