package com.example.coursework.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double CurrentDollarCurrency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentDollarCurrency() {
        return CurrentDollarCurrency;
    }

    public void setCurrentDollarCurrency(double currentDollarCurrency) {
        CurrentDollarCurrency = currentDollarCurrency;
    }

    public Currency(String name, double CurrentDollarCurrency) {
        this.name = name;
        this.CurrentDollarCurrency = CurrentDollarCurrency;
    }

    public Currency() {
    }
}
