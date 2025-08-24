package com.novara.novara_demo.model.dto;

public class NewUserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public NewUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public NewUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public NewUserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public NewUserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public NewUserDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
