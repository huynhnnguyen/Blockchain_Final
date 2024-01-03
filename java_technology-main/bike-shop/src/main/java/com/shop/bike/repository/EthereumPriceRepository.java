package com.shop.bike.repository;

import com.shop.bike.entity.EthereumPrice;
import com.shop.bike.entity.MyEthereum;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface EthereumPriceRepository extends JpaRepository<EthereumPrice, Long> {

    @Query(value = "SELECT * FROM ethereum_price ep WHERE "
            + " (cast(cast(:fromDate as text) as timestamp) is null or ep.time>=cast(cast(:fromDate as text) as timestamp)) " +
            " AND (cast(cast(:toDate as text) as timestamp) is null or ep.time<=cast(cast(:toDate as text) as timestamp))"
            ,nativeQuery = true)
    List<EthereumPrice> ethereumPriceChart(@Param("fromDate")Instant fromDate, @Param("toDate") Instant toDate);

}
