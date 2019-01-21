package com.pwnpub.search.mq.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author soobeenwong
 * @date 2018-11-23 1:16 AM
 * @desc 监听mq服务
 */
public class MyMessageListener implements MessageListener {

    private Logger LOG = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onMessage(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;

            try {
                // 消费消息，打印消息内容
                String text = textMessage.getText();
                System.out.println("接收到消息；消息内容为：" + text);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
