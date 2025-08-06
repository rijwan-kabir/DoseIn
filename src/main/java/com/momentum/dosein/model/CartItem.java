package com.momentum.dosein.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Medicine medicine;
    private int quantity;

    public CartItem() {}

    public CartItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return medicine.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return medicine.getName() + " x" + quantity + " - $" + String.format("%.2f", getTotalPrice());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return medicine != null ? medicine.equals(cartItem.medicine) : cartItem.medicine == null;
    }

    @Override
    public int hashCode() {
        return medicine != null ? medicine.hashCode() : 0;
    }
}