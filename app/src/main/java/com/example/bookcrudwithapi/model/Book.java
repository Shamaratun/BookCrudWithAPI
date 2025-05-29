package com.example.bookcrudwithapi.model;

public class Book {
    private int id;
    private String name;
    private String author_name;
    private double price;

    public Book() {
    }

    public Book(int id, String name, String author_name, double price) {
        this.id = id;
        this.name = name;
        this.author_name = author_name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

