package com.shop.bike.repository;

import com.shop.bike.entity.Wallet;
import com.shop.bike.entity.Wards;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByOwnerId(String ownerId);

}
