package com.egtdigitaltask.gateway.repository;

import com.egtdigitaltask.gateway.model.RequestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RequestRepository extends JpaRepository<RequestData, String>
{
    Optional<RequestData> findByRequestId(String requestId);
}
