package com.novara.novara_demo.model.dto;

public class ShowUserDTO {

    private String email;
    private String firstName;
    private String lastName;

    public ShowUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public ShowUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ShowUserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ShowUserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
