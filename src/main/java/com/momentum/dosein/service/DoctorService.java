package com.momentum.dosein.service;

import com.momentum.dosein.model.DoctorContact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private static final String FILE = "doctors.dat";

    @SuppressWarnings("unchecked")
    public List<DoctorContact> getAllContacts() {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<DoctorContact>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void addContact(DoctorContact dc) {
        List<DoctorContact> all = getAllContacts();
        all.add(dc);
        saveAll(all);
    }

    public void deleteContact(DoctorContact dc) {
        List<DoctorContact> all = getAllContacts();
        all.removeIf(d -> d.getName().equals(dc.getName())
                && d.getPhone().equals(dc.getPhone()));
        saveAll(all);
    }

    private void saveAll(List<DoctorContact> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
