package com.shrappz.contactsharer.Models;

/**
 * Created on 09-10-2016.
 */

public class Item {
    String name, number;

    public Item(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
