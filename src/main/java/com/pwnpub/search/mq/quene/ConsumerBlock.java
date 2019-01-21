package com.pwnpub.search.mq.quene;

import com.google.gson.Gson;
import com.pwnpub.search.pojo.BlockEntityAllUpdate;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.IOException;

/**
 * @author soobeenwong
 * @date 2018-11-23 12:48 AM
 * @desc 消费者
 */
@Component
public class ConsumerBlock {


    @Autowired
    private TransportClient client;

    // 定义每过3秒执行任务
    //@Scheduled(fixedRate = 3000)
    //@Scheduled(cron = "4-40 * * * * ?")   http://cron.qqe2.com
    @Scheduled(cron = "0 38 21 01 12 ?")
    public void getBlock() throws Exception {

        // 1. 创建ActiveMQConnectionFactory连接工厂，需要ActiveMQ的服务地址，使用的是tcp协议
        String brokerURL = "tcp://localhost:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

        // 2. 使用连接工厂创建连接
        Connection connection = factory.createConnection();

        // 3. 使用连接对象开启连接，使用start方法
        connection.start();

        // 4. 从连接对象里获取session
        // 第一个参数的作用是，是否使用JTA分布式事务，设置为false不开启
        // 第二个参数是设置应答方式，如果第一个参数是true，那么第二个参数就失效了，这里设置的应答方式是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 5. 从session获取消息类型Destination（模式（队列还是订阅），对应的名称），获取queue（名称为myqueue）
        // 参数就是设置队列名称
        Queue queue = session.createQueue("block_message");

        // 6. 从session中获取消息的消费者
        MessageConsumer consumer = session.createConsumer(queue);


        // 使用监听器接收消息
        // 使用监听的方式接收消息，其实是创建了一个新的线程来处理消息的接收
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                // 判断消息类型是TextMessage
                if (message instanceof TextMessage) {
                    // 如果是，则进行强转
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        // 8. 消费消息，打印消息内容
                        String text = textMessage.getText();
                        System.out.println("定时任务消费方监听到的【区块】消息为" + text);

                        Gson gson = new Gson();
                        BlockEntityAllUpdate blockEntityAll = gson.fromJson(text, BlockEntityAllUpdate.class);
                        BlockEntityAllUpdate.BlockBean blockEntity = blockEntityAll.getBlock();

                        System.out.println("-----------------------------------------------");
                        //存储到ES
                        try (
                                XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                        .field("difficulty", blockEntity.getDifficulty())
                                        .field("difficultyRaw", blockEntity.getDifficultyRaw())
                                        .field("extraData", blockEntity.getExtraData())
                                        .field("gasLimit", blockEntity.getGasLimit())
                                        .field("gasLimitRaw", blockEntity.getGasLimitRaw())
                                        .field("gasUsed", blockEntity.getGasUsed())
                                        .field("gasUsedRaw", blockEntity.getGasUsedRaw())
                                        .field("hash", blockEntity.getHash())
                                        .field("logsBloom", blockEntity.getLogsBloom())
                                        .field("miner", blockEntity.getMiner())
                                        .field("mixHash", blockEntity.getMixHash())
                                        .field("nonce", blockEntity.getNonce())
                                        .field("nonceRaw", blockEntity.getNonceRaw())
                                        .field("number", blockEntity.getNumber())
                                        .field("numberRaw", blockEntity.getNumberRaw())
                                        .field("parentHash", blockEntity.getParentHash())
                                        .field("receiptsRoot", blockEntity.getReceiptsRoot())
                                        .field("sha3Uncles", blockEntity.getSha3Uncles())
                                        .field("size", blockEntity.getSize())
                                        .field("sizeRaw", blockEntity.getSizeRaw())
                                        .field("stateRoot", blockEntity.getStateRoot())
                                        .field("timestamp", blockEntity.getTimestamp())
                                        .field("timestampRaw", blockEntity.getTimestampRaw())
                                        .field("totalDifficulty", blockEntity.getTotalDifficulty())
                                        .field("totalDifficultyRaw", blockEntity.getTotalDifficultyRaw())
                                        .field("transactions", blockEntity.getTransactions()) //数组
                                        .field("transactionsRoot", blockEntity.getTransactionsRoot())
                                        .field("uncles", blockEntity.getUncles())     //数组
                                        .endObject())
                        {
                            IndexResponse response = client.prepareIndex("block","data")
                                    .setSource(content)
                                    .get();

                            client.prepareUpdate();
                            System.out.println("存入ES成功...");

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });

        // 主线程睡眠5秒，让子线程有时间接收并处理消息
        Thread.sleep(5000);

        // 9. 释放资源
		/*consumer.close();
		session.close();
		connection.close();*/

    }
}
