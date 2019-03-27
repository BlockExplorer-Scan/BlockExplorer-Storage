package com.pwnpub.search.service;

import com.google.gson.Gson;
import com.pwnpub.search.config.CoinName;
import com.pwnpub.search.pojo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author soobeenwong
 * @date 2018-12-06 3:40 PM
 * @desc MQ消费者服务
 */
@Service
public class ConsumerService {

    private static final Logger logger = LogManager.getLogger(ConsumerService.class);

    public static final String BLOCK_MESSAGE = "block_message"; //区块
    public static final String TRANSACION_MESSAGE = "transaction_message"; //外部交易
    public static final String DEBUG_TRACE_TRANSACTION = "debug_traceTransaction";   //暂时不存储
    public static final String ETH_GET_TRANSACION_RECEIPT_LOG = "eth_get_transaction_receipt_log"; //erc20
    public static final String TRACE_TRANSACTION = "trace_transaction";  // 主币内部转账

    @Autowired
    private TransportClient client;

    @Autowired
    private CoinName coinName;

    @JmsListener(destination = "block_message") // 监听指定消息队列
    public void receiveBlock(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();
                logger.info("消费者监听到的[区块]消息为：" + text);

                Gson gson = new Gson();
                BlockEntityAllUpdate blockEntityAll = gson.fromJson(text, BlockEntityAllUpdate.class);
                BlockEntityAllUpdate.BlockBean blockEntity = blockEntityAll.getBlock();

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
                    logger.info("[Block]存入ES成功...");

                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @JmsListener(destination = "transaction_message") // 监听指定消息队列
    public void receiveTransaction(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();
                logger.info("消费方监听到的【外部交易】消息为" + text);

                Gson gson = new Gson();
                TransactionEntityAll transactionEntity = gson.fromJson(text, TransactionEntityAll.class);

                long currentTimeMillis = System.currentTimeMillis();

                try (
                        XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                .field("blockHash", transactionEntity.getBlockHash())
                                .field("blockNumber", transactionEntity.getBlockNumber())
                                .field("blockNumberRaw", transactionEntity.getBlockNumberRaw())
                                .field("chainId", transactionEntity.getChainId())
                                .field("from", transactionEntity.getFrom())
                                .field("to", transactionEntity.getTo())
                                .field("gas", transactionEntity.getGas())
                                .field("gasPrice", transactionEntity.getGasPrice())
                                .field("gasPriceRaw", transactionEntity.getGasPriceRaw())
                                .field("gasRaw", transactionEntity.getGasRaw())
                                .field("hash", transactionEntity.getHash())
                                .field("input", transactionEntity.getInput())
                                .field("nonce", transactionEntity.getNonce())
                                .field("nonceRaw", transactionEntity.getNonceRaw())
                                .field("r", transactionEntity.getR())
                                .field("s", transactionEntity.getS())
                                .field("transactionIndex", transactionEntity.getTransactionIndex())
                                .field("transactionIndexRaw", transactionEntity.getTransactionIndexRaw())
                                .field("v", transactionEntity.getV())
                                .field("valueStr", transactionEntity.getValue())
                                .field("valueRaw", transactionEntity.getValueRaw())
                                //.field("timestamp", String.valueOf(currentTimeMillis / 1000))
                                .field("timestamp", Integer.valueOf(transactionEntity.getTimestamp()) / 1000)
                                .field("timestampDay", new SimpleDateFormat("yyyy-MM-dd").format(transactionEntity.getTimestamp()))
                                .field("status","OUT")
                                .field("gasUsed", transactionEntity.getGasUsed())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("transaction","data")
                            .setSource(content)
                            .get();
                    logger.info("[OuterTransaction]存入ES成功....");

                }catch (IOException e){
                    logger.error("【存入交易异常】：", e.getMessage());
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @JmsListener(destination = "internal_transaction_message") // 监听指定消息队列
    public void receiveInternalTransaction(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();

                logger.info("消费方监听到的【内部交易】消息为:" + text);

                Gson gson = new Gson();
                TransactionEntityAll transactionEntity = gson.fromJson(text, TransactionEntityAll.class);

                long currentTimeMillis = System.currentTimeMillis();

                try (
                        XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                .field("blockHash", transactionEntity.getBlockHash())
                                .field("blockNumber", transactionEntity.getBlockNumber())
                                .field("blockNumberRaw", transactionEntity.getBlockNumberRaw())
                                .field("chainId", transactionEntity.getChainId())
                                .field("from", transactionEntity.getFrom())
                                .field("to", transactionEntity.getTo())
                                .field("gas", transactionEntity.getGas())
                                .field("gasPrice", transactionEntity.getGasPrice())
                                .field("gasPriceRaw", transactionEntity.getGasPriceRaw())
                                .field("gasRaw", transactionEntity.getGasRaw())
                                .field("hash", transactionEntity.getHash())
                                .field("input", transactionEntity.getInput())
                                .field("nonce", transactionEntity.getNonce())
                                .field("nonceRaw", transactionEntity.getNonceRaw())
                                .field("r", transactionEntity.getR())
                                .field("s", transactionEntity.getS())
                                .field("transactionIndex", transactionEntity.getTransactionIndex())
                                .field("transactionIndexRaw", transactionEntity.getTransactionIndexRaw())
                                .field("v", transactionEntity.getV())
                                .field("value", transactionEntity.getValue())
                                .field("valueRaw", transactionEntity.getValueRaw())
                                //.field("timestamp", String.valueOf(currentTimeMillis / 1000))
                                .field("timestamp", Integer.valueOf(transactionEntity.getTimestamp())/1000)
                                .field("timestampDay", new SimpleDateFormat("yyyy-MM-dd").format(transactionEntity.getTimestamp()))
                                .field("status","IN")
                                .field("gasUsed", transactionEntity.getGasUsed())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("transaction","data")
                            .setSource(content)
                            .get();
                    logger.info("[Internal Transaction]存入ES成功....");

                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @JmsListener(destination = "eth_get_transaction_receipt_log") // 监听ERC20
    public void receiveERC20(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();
                logger.info("消费方监听到的【ERC20转账记录】消息为" + text);

                Gson gson = new Gson();
                ERC20Entity  erc20Entity = gson.fromJson(text, ERC20Entity.class);

                if (erc20Entity.getTopics().size() == 3 && erc20Entity.getTopics().get(0).equals("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef")) {
                    String keccak = erc20Entity.getTopics().get(0);
                    String from = erc20Entity.getTopics().get(1);

                    String to = erc20Entity.getTopics().get(2);

                    //存储到ES
                    try (
                            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                    .field("address", erc20Entity.getAddress())
                                    .field("blockHash", erc20Entity.getBlockHash())
                                    .field("blockNumber", erc20Entity.getBlockNumber())
                                    .field("blockNumberRaw", erc20Entity.getBlockNumberRaw())
                                    .field("data", erc20Entity.getData())
                                    .field("logIndex", erc20Entity.getLogIndex())
                                    .field("logIndexRaw", erc20Entity.getLogIndexRaw())
                                    .field("removed", erc20Entity.isRemoved())
                                    .field("topics", erc20Entity.getTopics())
                                    .field("keccak", keccak)
                                    .field("from", "0x"+from.substring(26))
                                    .field("to", "0x"+ to.substring(26))
                                    .field("transactionHash", erc20Entity.getTransactionHash())
                                    .field("transactionIndex", erc20Entity.getTransactionIndex())
                                    .field("transactionIndexRaw", erc20Entity.getTransactionIndexRaw())
                                    .field("status", coinName.getErc20())
                                    //.field("timestamp", System.currentTimeMillis())
                                    .field("timestamp", erc20Entity.getTimestamp())
                                    .endObject())
                    {
                        IndexResponse response = client.prepareIndex("erc20","data")
                                .setSource(content)
                                .get();

                        client.prepareUpdate();
                        logger.info("[ERC20]Token Transfer存入ES成功...");

                    }catch (IOException e){
                        e.printStackTrace();
                    }

                } else {
                    try (
                            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                    .field("address", erc20Entity.getAddress())
                                    .field("blockHash", erc20Entity.getBlockHash())
                                    .field("blockNumber", erc20Entity.getBlockNumber())
                                    .field("blockNumberRaw", erc20Entity.getBlockNumberRaw())
                                    .field("data", erc20Entity.getData())
                                    .field("logIndex", erc20Entity.getLogIndex())
                                    .field("logIndexRaw", erc20Entity.getLogIndexRaw())
                                    .field("removed", erc20Entity.isRemoved())
                                    .field("topics", erc20Entity.getTopics())
                                    .field("keccak", "")
                                    .field("from", "")
                                    .field("to", "")
                                    .field("transactionHash", erc20Entity.getTransactionHash())
                                    .field("transactionIndex", erc20Entity.getTransactionIndex())
                                    .field("transactionIndexRaw", erc20Entity.getTransactionIndexRaw())
                                    .field("status", "other")
                                    //.field("timestamp", System.currentTimeMillis())
                                    .field("timestamp", erc20Entity.getTimestamp())
                                    .endObject())
                    {
                        IndexResponse response = client.prepareIndex("erc20","data")
                                .setSource(content)
                                .get();

                        client.prepareUpdate();
                        logger.info("[Other] Token Transfer存入ES成功...");

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @JmsListener(destination = "call_tracer") // 监听主币内部转账 maincoin
    public void receiveTraceTransaction(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();
                logger.info("消费方监听到的【主币内部转账】消息为" + text);

                Gson gson = new Gson();
                MainCoinEntityAll mainCoinEntity = gson.fromJson(text, MainCoinEntityAll.class);

                try (
                        XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                                .field("from", mainCoinEntity.getFrom())
                                .field("to", mainCoinEntity.getTo())
                                .field("value", mainCoinEntity.getValue())
                                .field("input", mainCoinEntity.getInput())
                                .field("type", mainCoinEntity.getType())
                                .field("transactionHash", mainCoinEntity.getTransactionHash())
                                .field("blockNumber", mainCoinEntity.getBlockNumber())
                                .field("gasCost", mainCoinEntity.getGasCost())
                                .field("gasIn", mainCoinEntity.getGasIn())
                                .field("outLen", mainCoinEntity.getOutLen())
                                .field("outOff", mainCoinEntity.getOutOff())
                                .field("status", coinName.getMaincoin())
                                .field("timestamp",mainCoinEntity.getTimestamp())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("maincoin","data")
                            .setSource(content)
                            .get();

                    client.prepareUpdate();
                    logger.info("[主币]内部转账存入ES成功...");

                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}


