package com.example.coursework.repo;

import org.springframework.data.repository.CrudRepository;

import com.example.coursework.Models.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

}
