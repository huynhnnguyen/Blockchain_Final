package com.shop.bike.repository;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.entity.EthereumTransaction;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Repository
@Primary
public interface EthereumTransactionRepository extends JpaRepository<EthereumTransaction, Long> {
	
	@Query(value = "select * from ethereum_transaction p "
            + " where ((:keyword is null) or (lower(cast(unaccent(p.buyer_name) as text)) like concat('%',lower(cast(unaccent(CAST(:keyword AS text)) as text)),'%')) "
            + " or (lower(cast(unaccent(p.seller_name) as text)) like concat('%',lower(cast(unaccent(CAST(:keyword AS text)) as text)),'%'))) "
            + " AND (cast(cast(:fromDate as text) as timestamp) is null or p.date >= cast(cast(:fromDate as text) as timestamp)) "
            + " AND (cast(cast(:toDate as text) as timestamp) is null or p.date <= cast(cast(:toDate as text) as timestamp)) "
            + " AND (cast(cast(:lastModifiedDateFrom as text) as timestamp) is null or p.last_modified_date >= cast(cast(:lastModifiedDateFrom as text) as timestamp)) "
            + " AND (cast(cast(:lastModifiedDateTo as text) as timestamp) is null or p.last_modified_date <= cast(cast(:lastModifiedDateTo as text) as timestamp)) "
            + " AND ((:code is null) or (lower(cast(unaccent(p.code) as text)) like concat('%',lower(cast(unaccent(CAST(:code AS text)) as text)),'%'))) "
            + " AND ((:sellerName is null) or (lower(cast(unaccent(p.seller_name) as text)) like concat('%',lower(cast(unaccent(CAST(:sellerName AS text)) as text)),'%'))) "
            + " AND ((:email is null) or (lower(cast(unaccent(p.email) as text)) like concat('%',lower(cast(unaccent(CAST(:email AS text)) as text)),'%'))) "
            + " and (cast(cast(:priceFrom as text) as numeric) is null or p.price >= cast(cast(:priceFrom as text) as numeric)) "
            + " AND (cast(cast(:priceTo as text) as numeric) is null or p.price <= cast(cast(:priceTo as text) as numeric)) "
            + " and (cast(cast(:quantityEthFrom as text) as numeric) is null or p.quantity_eth >= cast(cast(:quantityEthFrom as text) as numeric)) "
            + " AND (cast(cast(:quantityEthTo as text) as numeric) is null or p.quantity_eth <= cast(cast(:quantityEthTo as text) as numeric)) "
            + " AND (cast(:sellerId as text) is null or cast(p.seller_id as text) like cast(:sellerId as text)) "
            + " AND (cast(:buyerId as text) is null or cast(p.seller_id as text) != cast(:buyerId as text)) ", nativeQuery = true)
    Page<EthereumTransaction> findAllTransactions(@Param("keyword") String keyword,
                                      @Param("code") String code,
                                      @Param("email") String email,
                                      @Param("sellerName") String sellerName,
                                      @Param("fromDate") Instant fromDate,
                                      @Param("toDate") Instant toDate,
                                      @Param("lastModifiedDateFrom") Instant lastModifiedDateFrom,
                                      @Param("lastModifiedDateTo") Instant lastModifiedDateTo,
                                      @Param("priceFrom") BigDecimal priceFrom,
                                      @Param("priceTo") BigDecimal priceTo,
                                      @Param("quantityEthFrom") Integer quantityEthFrom,
                                      @Param("quantityEthTo") Integer quantityEthTo,
                                      @Param("sellerId") String sellerId,
                                      @Param("buyerId") String buyerId,
                                      Pageable pageable);
}
