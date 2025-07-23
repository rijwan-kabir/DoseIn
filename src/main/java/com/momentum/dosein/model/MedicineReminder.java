package com.momentum.dosein.model;

import java.io.Serializable;
import java.time.LocalTime;

public class MedicineReminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String    medicineName;
    private String    dosage;
    private LocalTime time;
    private String    note;

    public MedicineReminder() {}

    public MedicineReminder(String medicineName, String dosage,
                            LocalTime time, String note) {
        this.medicineName = medicineName;
        this.dosage       = dosage;
        this.time         = time;
        this.note         = note;
    }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String m) { this.medicineName = m; }

    public String getDosage() { return dosage; }
    public void setDosage(String d) { this.dosage = d; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime t) { this.time = t; }

    public String getNote() { return note; }
    public void setNote(String n) { this.note = n; }
}
