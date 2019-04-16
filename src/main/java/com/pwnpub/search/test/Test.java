package com.pwnpub.search.test;

import com.google.gson.Gson;
import com.pwnpub.search.pojo.TransactionEntityAll;

import java.text.SimpleDateFormat;

/**
 * @author soobeenwong
 * @date 2019-03-28 4:59 PM
 * @desc 测试
 */
public class Test {

    public static void main(String[] args) {


        //"timestamp":"1553762612000"
        String timestamp = "1553762612000";
        long l = Long.parseLong(timestamp);
        System.out.println(l);

        System.out.println();

        String format = new SimpleDateFormat("yyyy-MM-dd").format(l);
        System.out.println(format);



        String text = "{\"blockHash\":\"0x5574daf5dac35c4100ef361bb3b65ae011747cb53be63428ad9641acfb0fdd78\",\"blockNumber\":9014995,\"blockNumberRaw\":\"0x898ed3\",\"from\":\"0xc16806305f30364a5652de79f33f0b82a9216698\",\"gas\":1500000,\"gasPrice\":18000000000,\"gasPriceRaw\":\"0x430e23400\",\"gasRaw\":\"0x16e360\",\"gasUsed\":180711,\"hash\":\"0xcc03758ccef5d1ab074e68dd889ad8c4f8ea89daf90603df9b93523225387ef0\",\"input\":\"0xd6d1b06a00000000000000000000000000000000000000000000000000000000000000070000000000000000000000005516377dc6f9c7ff50bfa7ae6ce70562eb055df5000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000029a2241af62c000000000000000000000000000000000000000000000000000029a2241af62c00000000000000000000000000000000000000000000000000000000000000000000\",\"nonce\":5016,\"nonceRaw\":\"0x1398\",\"r\":\"0x2283568e1bfa3989bb64f308ab2a9b935fd4297eb386cf3496d74f51e19144c6\",\"s\":\"0x6b6b92b931fef519702e63911eabc696b85c3f1b7b6217305b7333de7333bff1\",\"timestamp\":\"1553762612000\",\"to\":\"0x2b4924cf5e5c8fa97ba3192c1554521b98404f94\",\"transactionIndex\":0,\"transactionIndexRaw\":\"0x0\",\"v\":27,\"value\":0,\"valueRaw\":\"0x0\"}";

        Gson gson = new Gson();
        TransactionEntityAll transactionEntity = gson.fromJson(text, TransactionEntityAll.class);

        String timestamp1 = transactionEntity.getTimestamp();

        String format1 = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(timestamp1));
        System.out.println(format1);


    }

}
