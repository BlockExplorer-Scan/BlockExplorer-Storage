package com.pwnpub.search.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @date 2018-11-27 5:56 PM
 * @desc 测试定时任务
 */

public class TestTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 定义每过3秒执行任务
    @Scheduled(fixedRate = 3000)
    //@Scheduled(cron = "4-40 * * * * ?")   http://cron.qqe2.com
    public void reportCurrentTime() {

        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }

}
