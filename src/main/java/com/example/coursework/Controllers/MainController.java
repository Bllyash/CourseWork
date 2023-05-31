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
import java.util.*;

//ЗРОБЛЕНО БЛЛЯШОМ, ЩОБ НЕ ВИЛЕТІТИ З КПІ
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
        //ДАТА
        LocalDate time = LocalDate.now();
        double DollarCurrencyDouble = Double.parseDouble(DollarCurrency);
        Currency currency = new Currency(moneyName, DollarCurrencyDouble);
        currencyRepository.save(currency);
        CurrencyHistory currencyHistory = new CurrencyHistory(moneyName, DollarCurrencyDouble, time);
        currencyHistoryRepository.save(currencyHistory);
        CurrencyHistory currencyHistoryDates = new CurrencyHistory();
        for(int i = 1; ; i++){
            currencyHistoryDates.setDate(time.minusDays(i));
            currencyHistoryDates.setRate(DollarCurrencyDouble);
            currencyHistoryDates.setCurrencyCode(moneyName);
            if(Objects.equals(currencyHistoryDates.getDate(), LocalDate.of(2023, 05, 9))){
                break;
            }
            currencyHistoryRepository.save(currencyHistoryDates);
            currencyHistoryDates = new CurrencyHistory();

        }
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
        LocalDate time = LocalDate.now();
        Currency currency = currencyRepository.findById(id).orElseThrow();
        currency.setName(moneyName);
        currency.setCurrentDollarCurrency(Double.parseDouble(DollarCurrency));
        currencyRepository.save(currency);
        Optional<Currency> currencyOpt = currencyRepository.findById(id);
        Optional<CurrencyHistory> currencyHistoryOpt = currencyHistoryRepository.findByCurrencyCodeAndDate(currency.getName(), time);
        if (currencyHistoryOpt.isPresent()) {
            CurrencyHistory currencyHistory = currencyHistoryOpt.get();
            currencyHistory.setRate(Double.parseDouble(DollarCurrency));
            currencyHistoryRepository.save(currencyHistory);
        } else {
            CurrencyHistory currencyHistory = new CurrencyHistory();
            currencyHistory.setCurrencyCode(currency.getName());
            currencyHistory.setRate(Double.parseDouble(DollarCurrency));
            currencyHistory.setDate(time);
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
        String currencyName = currency.getName();
        Iterable<Currency> curs = currencyRepository.findAll();
        Iterable<CurrencyHistory> cursHist = currencyHistoryRepository.findAll();
        filteredData.sort(Comparator.comparing(CurrencyHistory::getDate));
        model.addAttribute("cursName", currencyName);
        model.addAttribute("otherValues", curs);
        model.addAttribute("otherValuesHist", cursHist);
        model.addAttribute("filteredData", filteredData);
        return "MoneyExchangeHistory";
    }
}