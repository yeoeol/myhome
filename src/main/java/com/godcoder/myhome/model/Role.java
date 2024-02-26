package com.godcoder.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class Role {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
