package com.shop.bike.service;

import com.shop.bike.entity.Ethereum;
import com.shop.bike.entity.MiningEthereum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MiningEthereumService {

    void miningEthereum();

    Page<MiningEthereum> findAllBuUserId(Pageable pageable);

}
