package com.novara.novara_demo.model.entity;


import com.novara.novara_demo.model.entity.enums.UserRoleEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;


// UNUSED FOR NOW - OPTED FOR THE @ElementCollection + @CollectionTable approach as roles are very simple
//@Entity
//@Table(name = "roles")
public class UserRole {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public UUID getId() {
        return id;
    }

    public UserRole setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public UserRole setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}
