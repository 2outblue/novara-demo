package com.novara.novara_demo.model.entity;


import com.novara.novara_demo.model.entity.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.sql.Types;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Indexed
public class User {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @NotNull
    @FullTextField(analyzer = "edgeNgram-english")
    private String email;
    @FullTextField(analyzer = "ngram-english")
    private String firstName;
    @FullTextField(analyzer = "ngram-english")
    private String lastName;
    @NotNull
    private String password;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(joinColumns = @JoinColumn(name= "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;


    public User() {
    }

    public User(UUID id, String email, String firstName, String lastName, String password, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public User setEmail(@NotNull String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public User setPassword(@NotNull String password) {
        this.password = password;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public User setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
}
