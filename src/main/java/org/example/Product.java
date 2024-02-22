package org.example;

public enum Product {
    WATER(5),
    COCA(7),
    TWIX(10),
    BUENO(12);

    private final int price;

    Product(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
