package com.cinema_app.models;

/**
 * Created by locall on 2/14/2018.
 */

public class user {
    private String email;
    private String password;

    private String name;
    private String phone;

    public user(){}




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getPassword() {
        return password;
    }



    public String getEmail() {
        return email;
    }



    public void setPassword(String password) {
        this.password = password;
    }



    public void setEmail(String username) {
        this.email = username;
    }
}
