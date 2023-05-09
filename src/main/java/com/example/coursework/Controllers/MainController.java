package com.example.coursework.Controllers;

import com.example.coursework.Models.Currency;
import com.example.coursework.Models.CurrencyHistory;
import com.example.coursework.repo.CurrencyHistoryRepository;
import com.example.coursework.repo.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CurrencyHistoryRepository currencyHistoryRepository;
    @GetMapping("/")
    public String MainPage(Model model){
        Iterable<Currency> curs = currencyRepository.findAll();
        model.addAttribute("curs", curs);
        return "MainPage";
    }
    @GetMapping("/MoneyCourse/add")
    public String MoneyCourseAdd(Model model){
        return "MoneyAdd";
    }

    @GetMapping("/LoginPage")
    public String LoginPage(Model model){
        return "LoginPage";
    }

    @GetMapping("/MoneyValuePage/{id}")
    public String MoneyValuePage(@PathVariable(value = "id") long id, Model model ){
        Optional<Currency> currency = currencyRepository.findById(id);
        ArrayList<Currency> res = new ArrayList<>();
        currency.ifPresent(res::add);
        model.addAttribute("curs", res);
        Iterable<Currency> curs = currencyRepository.findAll();
        model.addAttribute("OtherValues", curs);
        return "MoneyValuePage";
    }

    @PostMapping("/MoneyCourse/add")
    public String MoneyExchangeAdd(@RequestParam String moneyName, @RequestParam String DollarCurrency, Model model){
        LocalDate time = LocalDate.now();
        double DollarCurrencyDouble = Double.parseDouble(DollarCurrency);
        Currency currency = new Currency(moneyName, DollarCurrencyDouble);
        currencyRepository.save(currency);
        CurrencyHistory currencyHistory = new CurrencyHistory(moneyName, DollarCurrencyDouble, time);
        currencyHistoryRepository.save(currencyHistory);
        return "redirect:/";
    }
    @GetMapping("/MoneyValuePage/{id}/edit")
    public String editCurrency(@PathVariable(value = "id") long id, Model model ){
        Optional<Currency> currency = currencyRepository.findById(id);
        ArrayList<Currency> res = new ArrayList<>();
        currency.ifPresent(res::add);
        model.addAttribute("curs", res);
        Iterable<Currency> curs = currencyRepository.findAll();
        model.addAttribute("OtherValues", curs);
        return "EditCurrency";
    }
    @PostMapping("/MoneyValuePage/{id}/edit")
    public String MoneyExchangeEdit(@RequestParam String moneyName,@RequestParam String DollarCurrency, Model model, @PathVariable(value = "id") long id) {
        Currency currency = currencyRepository.findById(id).orElseThrow();
        currency.setName(moneyName);
        currency.setCurrentDollarCurrency(Double.parseDouble(DollarCurrency));
        currencyRepository.save(currency);
        Optional<Currency> currencyOpt = currencyRepository.findById(id);
        if (currencyOpt.isPresent()) {
            LocalDate time = LocalDate.now();
            currency = currencyOpt.get();
            // Створення нового запису в таблиці з історією курсу цієї валюти
            CurrencyHistory currencyHistory = new CurrencyHistory();
            currencyHistory.setId(currency.getId());
            currencyHistory.setCurrencyCode(currency.getName());
            currencyHistory.setRate(Double.parseDouble(DollarCurrency));
            currencyHistory.setDate(time);
            // Додавання нового запису до таблиці з історією курсу цієї валюти
            currencyHistoryRepository.save(currencyHistory);
        }
        return "redirect:/";
    }

    @Transactional
    @PostMapping("/MoneyValuePage/{id}/remove")
    public String MoneyExchangeDelete(Model model, @PathVariable(value = "id") long id){
        Currency currency = currencyRepository.findById(id).orElseThrow();
        currencyHistoryRepository.deleteFieldsByName(currency.getName());
        currencyRepository.delete(currency);
        return "redirect:/";
    }
    @GetMapping("/MoneyValuePage/{id}/history")
    public String MoneyExchangeHistory(Model model, @PathVariable Long id){
        Currency currency = currencyRepository.findById(id).orElseThrow();
        List<CurrencyHistory> filteredData = currencyHistoryRepository.findByCurrencyCode(currency.getName());
        System.out.println(filteredData);
        model.addAttribute("filteredData", filteredData);
        return "MoneyExchangeHistory";
    }

}


