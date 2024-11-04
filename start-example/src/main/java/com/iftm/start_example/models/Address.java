package com.iftm.start_example.models;

import java.util.Objects;

public class Address {
    private String street;
    private int number;

    public Address() {
    }

    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }
}
