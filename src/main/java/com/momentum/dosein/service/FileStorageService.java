package com.momentum.dosein.service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileStorageService<T> {
    private final Path dataDir = Paths.get("data");

    public FileStorageService() {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectory(dataDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(String fileName, List<T> dataList) {
        Path path = dataDir.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(path))) {
            oos.writeObject(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> loadData(String fileName) {
        Path path = dataDir.resolve(fileName);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(path))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
