package com.egtdigitaltask.gateway.repository;

import com.egtdigitaltask.gateway.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currency, Long>
{

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO currencies (ticker, full_name) VALUES (:ticker, :fullName) " +
                   "ON DUPLICATE KEY UPDATE full_name = VALUES(full_name)", nativeQuery = true)
    void upsert(String ticker, String fullName);

    @Query(value = "SELECT ticker from currencies", nativeQuery = true)
    List<String> findAllTickers();
}
