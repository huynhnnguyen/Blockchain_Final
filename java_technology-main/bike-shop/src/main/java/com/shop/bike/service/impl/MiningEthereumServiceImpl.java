package com.shop.bike.service.impl;

import com.shop.bike.entity.Ethereum;
import com.shop.bike.entity.MiningEthereum;
import com.shop.bike.entity.MyEthereum;
import com.shop.bike.entity.User;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.EthereumRepository;
import com.shop.bike.repository.MiningEthereumRepository;
import com.shop.bike.repository.MyEthereumRepository;
import com.shop.bike.repository.UserRepository;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.EthereumService;
import com.shop.bike.service.MiningEthereumService;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
@Primary
public class MiningEthereumServiceImpl implements MiningEthereumService {

    @Autowired
    private MiningEthereumRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EthereumRepository ethereumRepository;

    @Autowired
    private MyEthereumRepository myEthereumRepository;


    @Override
    public void miningEthereum() {


        String userId = SecurityUtils.getCurrentUserLogin().get();

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND));

        Instant lastMining = repository.findTheLastMining(userId);

        Optional<MiningEthereum> miningEthereum = repository.findByUserIdAndTimeMining(userId, lastMining);

        Ethereum ethereum = ethereumRepository.findById(1l)
                .orElseThrow(()-> new BadRequestAlertException(ErrorEnum.ETHEREUM_NOT_FOUND));





//

        if (miningEthereum.isPresent()) {
            System.out.println("time last and time now "+lastMining + " "+Instant.now());
            Long minutesBetween = lastMining.until(Instant.now(), ChronoUnit.MINUTES);
            if(minutesBetween <=60)
                throw new BadRequestAlertException("youCan't","mining", "you can't mining after 1 hours");
            else {
                MiningEthereum miningEthereum1 = initNewMining(user);

                //set ethereum in server
                ethereum.setTotalEther(ethereum.getTotalEther() - (miningEthereum.get().getTotalMining() + 1));

                //set ethereum total info and mining ethereum
                miningEthereum1.setTotalMining(miningEthereum.get().getTotalMining() + 1);
                miningEthereum1.setTotalEthereumNow(ethereum.getTotalEther() - (miningEthereum.get().getTotalMining()));

                //set total ethereum mined
                Optional<MyEthereum> myEthereum = myEthereumRepository.findByUserId(userId);
                myEthereum.get().setTotalEther(myEthereum.get().getTotalEther() +  miningEthereum1.getTotalMining());
                myEthereumRepository.save(myEthereum.get());

                repository.save(miningEthereum1);
            }
        }
        else {
            MiningEthereum miningEthereum1 = initNewMining(user);

            //set ethereum in server
            ethereum.setTotalEther(ethereum.getTotalEther() - 1.0);

            //set ethereum total info and mining ethereum
            miningEthereum1.setTotalMining(1.0);
            miningEthereum1.setTotalEthereumNow(ethereum.getTotalEther());

            MyEthereum myEthereum = new MyEthereum();
            myEthereum.setTotalEther(1.0);
            myEthereum.setUserId(userId);
            myEthereumRepository.save(myEthereum);
            repository.save(miningEthereum1);
        }


        ethereumRepository.save(ethereum);

    }

    @Override
    public Page<MiningEthereum> findAllBuUserId(Pageable pageable) {
        return repository.findAllByUserId(SecurityUtils.getCurrentUserLogin().get(), pageable);
    }

    private MiningEthereum initNewMining(User user) {
        MiningEthereum miningEthereum1 = new MiningEthereum();

        //set user info mining
        miningEthereum1.setTimeMining(Instant.now());
        miningEthereum1.setUserId(user.getId().toString());
        miningEthereum1.setUserName(user.getName());
        return  miningEthereum1;
    }


}
