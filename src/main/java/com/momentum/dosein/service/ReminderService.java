package com.momentum.dosein.service;

import com.momentum.dosein.model.MedicineReminder;
import java.util.List;

public class ReminderService {
    private static final String REMINDER_FILE = "medicines.ser";
    private final FileStorageService<MedicineReminder> storage =
            new FileStorageService<>();

    /** Load all reminders */
    public List<MedicineReminder> getAllReminders() {
        return storage.loadData(REMINDER_FILE);
    }

    /** Add a single reminder and persist */
    public void addReminder(MedicineReminder reminder) {
        List<MedicineReminder> all = storage.loadData(REMINDER_FILE);
        all.add(reminder);
        storage.saveData(REMINDER_FILE, all);
    }
}
