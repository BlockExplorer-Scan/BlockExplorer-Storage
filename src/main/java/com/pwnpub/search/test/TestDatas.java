package com.pwnpub.search.test;

import com.pwnpub.search.config.CoinName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * @author soobeenwong
 * @date 2018-12-17 4:21 PM
 * @desc
 */
@RestController
@RequestMapping("/Test")
public class TestDatas {

    private static final Logger logger = LogManager.getLogger(TestDatas.class);

    @Autowired
    private CoinName coinName;

    @GetMapping("/A")
    public void Aaa() {

        System.out.println(System.currentTimeMillis() / 1000);
        System.out.println("1545197661");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() / 1000));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()/1000));

        String a = "0x123123123";
        System.out.println("0x"+ a.substring(2));
        logger.info("===========");

        logger.error("aaaaaaaa");

        System.out.println(coinName.getErc20());
        System.out.println(coinName.getMaincoin());

    }

}
