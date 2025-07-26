package com.momentum.dosein.model;

import java.io.Serializable;

public class DoctorContact implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String specialty;
    private final String phone;
    private final String email;
    private final String location;
    private final String note;

    public DoctorContact(String name,
                         String specialty,
                         String phone,
                         String email,
                         String location,
                         String note) {
        this.name       = name;
        this.specialty  = specialty;
        this.phone      = phone;
        this.email      = email;
        this.location   = location;
        this.note       = note;
    }

    public String getName()       { return name; }
    public String getSpecialty()  { return specialty; }
    public String getPhone()      { return phone; }
    public String getEmail()      { return email; }
    public String getLocation()   { return location; }
    public String getNote()       { return note; }
}
