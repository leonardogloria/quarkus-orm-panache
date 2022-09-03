package com.example.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import java.util.Optional;

@Entity
public class Task extends PanacheEntity {
    public String description;
    public String project;
    public String developer;
    public static Optional<Task> findByDeveloper(String developer){
        return find("developer",developer).firstResultOptional();
    }

}
