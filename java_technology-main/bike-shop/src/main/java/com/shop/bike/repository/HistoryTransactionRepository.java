package com.shop.bike.repository;

import com.shop.bike.entity.HistoryTransaction;
import com.shop.bike.entity.MiningEthereum;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@Primary
public interface HistoryTransactionRepository extends JpaRepository<HistoryTransaction, Long> {


    HistoryTransaction findBySmartContractId(Long smartContractId);
}
