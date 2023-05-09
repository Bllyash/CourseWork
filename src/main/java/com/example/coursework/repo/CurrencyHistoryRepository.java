package com.example.coursework.repo;

import com.example.coursework.Models.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Long> {
    @Modifying
    @Query("DELETE FROM CurrencyHistory e WHERE e.currencyCode = ?1")
    void deleteFieldsByName(String fieldName);

    List<CurrencyHistory> findByCurrencyCode(String currencyCode);

}
