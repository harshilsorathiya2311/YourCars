package com.example.yourcars.model;

public class UserModel {
    private String email;
    private String contactnumber;

    public UserModel( String email, String contactnumber) {
        this.email = email;
        this.contactnumber = contactnumber;
    }

    // getters...
    public String getEmail() { return email; }
    public String getContactnumber() { return contactnumber; }

}

