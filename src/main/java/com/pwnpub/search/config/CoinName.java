package com.pwnpub.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2019-01-14 11:32 AM
 * @desc coinname based on xxx standard protocols
 */
@Component
@ConfigurationProperties(prefix = "coin")
public class CoinName {

    private String maincoin;
    private String erc20;

    public String getMaincoin() {
        return maincoin;
    }

    public void setMaincoin(String maincoin) {
        this.maincoin = maincoin;
    }

    public String getErc20() {
        return erc20;
    }

    public void setErc20(String erc20) {
        this.erc20 = erc20;
    }


}
