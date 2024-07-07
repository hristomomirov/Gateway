package com.egtdigitaltask.gateway.repository;

import com.egtdigitaltask.gateway.model.ExchangeRate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>
{
    List<ExchangeRate> findByTimestampBetweenAndBaseTickerOrderByTargetTickerAsc(long startTime,
                                                                                 long endTime,
                                                                                 String baseTicker);
    @Cacheable(value = "currentExchangeRateCache")
    @Query(value = "SELECT * FROM exchange_rates WHERE timestamp BETWEEN UNIX_TIMESTAMP(NOW() - INTERVAL 1 HOUR) "
                   + "AND UNIX_TIMESTAMP(NOW()) AND base_ticker = :baseTicker ORDER BY target_ticker ASC",
           nativeQuery = true)
    List<ExchangeRate> findRecentRates(String baseTicker);
}

