package com.momentum.dosein.service;

import com.momentum.dosein.model.MedicineReminder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderService {
    // File where reminders are stored
    private static final String REMINDER_FILE = "medicines.dat";

    /** Load all reminders from disk, or return empty list if none. */
    @SuppressWarnings("unchecked")
    public List<MedicineReminder> getAllReminders() {
        File f = new File(REMINDER_FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<MedicineReminder>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** Add a reminder and persist the entire list. */
    public void addReminder(MedicineReminder reminder) {
        List<MedicineReminder> all = getAllReminders();
        all.add(reminder);
        saveAll(all);
    }

    /** Delete all reminders matching the given medicine key (name + dosage). */
    public void deleteByMedicine(String medicineKey) {
        List<MedicineReminder> all = getAllReminders();
        all.removeIf(r -> (r.getMedicineName() + " " + r.getDosage()).equals(medicineKey));
        saveAll(all);
    }

    /** Persist the full list back to disk. */
    private void saveAll(List<MedicineReminder> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(REMINDER_FILE))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
