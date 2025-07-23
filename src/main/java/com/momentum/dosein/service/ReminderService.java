package com.momentum.dosein.service;

import com.momentum.dosein.model.MedicineReminder;
import java.util.List;

public class ReminderService {
    private static final String REMINDER_FILE = "medicines.ser";
    private final FileStorageService<MedicineReminder> storage =
            new FileStorageService<>();

    /** Load all reminders (today’s filtering can be added later). */
    public List<MedicineReminder> getAllReminders() {
        return storage.loadData(REMINDER_FILE);
    }
}
