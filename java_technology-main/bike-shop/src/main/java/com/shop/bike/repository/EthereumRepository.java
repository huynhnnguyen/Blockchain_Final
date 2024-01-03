package com.shop.bike.repository;

import com.shop.bike.entity.Address;
import com.shop.bike.entity.Ethereum;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface EthereumRepository extends JpaRepository<Ethereum, Long> {

}
