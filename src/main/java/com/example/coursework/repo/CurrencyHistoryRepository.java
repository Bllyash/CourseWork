package com.example.coursework.repo;

import com.example.coursework.Models.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    @Modifying
    @Query("DELETE FROM CurrencyHistory e WHERE e.currencyCode = ?1")
    void deleteFieldsByName(String fieldName);

    List<CurrencyHistory> findByCurrencyCode(String currencyCode);
    List<CurrencyHistory> findByDate (LocalDate changeDate);
    Optional<CurrencyHistory> findByCurrencyCodeAndDate(String currencyCode, LocalDate time);


}
