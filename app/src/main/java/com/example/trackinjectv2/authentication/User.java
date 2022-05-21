package com.example.trackinjectv2.authentication;

import com.example.trackinjectv2.Injections.Injections;
import com.example.trackinjectv2.medicine.Medicine;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName,lastName,email,password;
    private List<Medicine> medicine;
    private List<Injections> injections;

    public User(String firstName,String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.medicine = new ArrayList<>();
        this.injections = new ArrayList<>();
    }

    public User() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setInjections(List<Injections> injections) {
        this.injections = injections;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Medicine> getMedicine() {
        return medicine;
    }

    public List<Injections> getInjections() {
        return injections;
    }

    public void setMedicine(List<Medicine> medicine) {
        this.medicine = medicine;
    }


}
