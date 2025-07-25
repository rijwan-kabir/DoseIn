package com.momentum.dosein.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class MedicineReminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String medicineName;
    private final String dosage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime time;
    private final String note;

    public MedicineReminder(String medicineName,
                            String dosage,
                            LocalDate startDate,
                            LocalDate endDate,
                            LocalTime time,
                            String note) {
        this.medicineName = medicineName;
        this.dosage      = dosage;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.time        = time;
        this.note        = note;
    }

    // Convenience constructor if you want to omit dates (e.g. older code)
    public MedicineReminder(String medicineName,
                            String dosage,
                            LocalTime time,
                            String note) {
        this(medicineName, dosage,
                LocalDate.now(),       // default start = today
                LocalDate.now().plusDays(1), // default end = tomorrow
                time, note);
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }
}
