package com.project.flowerms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Roles")
@Getter
@Setter
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "role_id",nullable = false)
    private Integer roleId;
    @Column( name = "role_name",nullable = false)
    private String roleName;
}
