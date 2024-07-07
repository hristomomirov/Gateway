package com.egtdigitaltask.gateway.repository;

import com.egtdigitaltask.gateway.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>
{
    List<ExchangeRate> findByTimestampBetweenAndBaseTickerOrderByTargetTickerAsc(long startTime,
                                                                                 long endTime,
                                                                                 String baseTicker);

}
