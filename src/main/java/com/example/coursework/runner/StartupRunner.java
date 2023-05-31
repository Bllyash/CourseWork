package com.example.coursework.runner;

import com.example.coursework.Models.CurrencyHistory;
import com.example.coursework.repo.CurrencyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void run(String... args) throws Exception {
        //ДАТА
        LocalDate today = LocalDate.now();
        LocalDate previousDay = today.minusDays(1);

        List<CurrencyHistory> previousDayHistory = currencyHistoryRepository.findByDate(previousDay);
        List<CurrencyHistory> todayDayHistory = currencyHistoryRepository.findByDate(today);
        if (!previousDayHistory.contains(previousDay) && todayDayHistory.size()==0) {
            List<CurrencyHistory> previousDayHistoryCurrency = currencyHistoryRepository.findByDate(previousDay);
            for (CurrencyHistory prev: previousDayHistoryCurrency.stream().toList()) {
                CurrencyHistory currencyHistory = new CurrencyHistory(prev.getCurrencyCode(), prev.getRate(), today);
                currencyHistoryRepository.save(currencyHistory);
            }
        }
    }
}

