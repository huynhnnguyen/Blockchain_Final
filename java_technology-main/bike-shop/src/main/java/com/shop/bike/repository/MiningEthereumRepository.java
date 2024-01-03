package com.shop.bike.repository;

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
public interface MiningEthereumRepository extends JpaRepository<MiningEthereum, Long> {

    Optional<MiningEthereum> findByUserIdAndTimeMining(@Param("userId") String userId,@Param("timeMining") Instant timeMining);


    @Query(value = "SELECT max(a.time_mining) FROM mining_ethereum a WHERE a.user_id like :userId",nativeQuery = true)
    Instant findTheLastMining(@Param("userId") String userId);


    Page<MiningEthereum> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT COALESCE(sum(a.total_mining),0) FROM mining_ethereum a WHERE a.user_id like :userId",nativeQuery = true)
    Integer countByUserId(@Param("userId") String userId);

    @Query(value = "SELECT COALESCE(SUM(a.quantity_eth), 0) FROM history_transaction a WHERE cast(a.seller_id as text) like :userId",nativeQuery = true)
    Integer countTotalTransactionByUserId(@Param("userId") String userId);

    @Query(value = "SELECT COALESCE(count(a.buyer_id), 0) FROM history_transaction a WHERE cast(a.seller_id as text) like :userId",nativeQuery = true)
    Integer countTotalCustomerByUserId(@Param("userId") String userId);

}
