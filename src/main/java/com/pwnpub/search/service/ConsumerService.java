package com.pwnpub.search.service;

import com.google.gson.Gson;
import com.pwnpub.search.config.CoinName;
import com.pwnpub.search.pojo.BlockEntityAllUpdate;
import com.pwnpub.search.pojo.ERC20Entity;
import com.pwnpub.search.pojo.MainCoinEntityAll;
import com.pwnpub.search.pojo.TransactionEntityAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author soobeenwong
 * @date 2018-12-06 3:40 PM
 * @desc MQ消费者服务
 */
@Transactional
@Service
public class ConsumerService {

    private static final Logger logger = LogManager.getLogger(ConsumerService.class);

    public static final String BLOCK_MESSAGE = "block_message_new"; //区块
    public static final String TRANSACION_MESSAGE = "transaction_message_new"; //外部交易
    public static final String DEBUG_TRACE_TRANSACTION = "debug_traceTransaction";   //暂时不存储
    public static final String ETH_GET_TRANSACION_RECEIPT_LOG = "eth_get_transaction_receipt_log_new"; //erc20
    public static final String TRACE_TRANSACTION = "trace_transaction";  // 主币内部转账
    public static final String CALL_TRACER = "call_tracer_new";  // 监听主币内部转账 maincoin
    public static final String INTERNAL_TRANSACTION_MESSAGE = "internal_transaction_message_new";  // 监听主币内部转账 maincoin
    public static final String TRANSACION_MESSAGE_FAIL = "transaction_message_fail_new";

    @Autowired
    private TransportClient client;

    @Autowired
    private CoinName coinName;

    @JmsListener(destination = BLOCK_MESSAGE) // 监听指定消息队列
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
                    IndexResponse response = client.prepareIndex("block_new","data")
                            .setSource(content)
                            .get();

                    client.prepareUpdate();
                    logger.info("[Block]存入ES成功...");

                }catch (IOException e){
                    e.printStackTrace();
                    throw new RuntimeException("[Block存入ES发生异常]，该区块消息开始回滚至MQ队列...");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[Block存入ES发生异常]，该区块消息开始回滚至MQ队列...");
            }
        }
    }

    /**
     * 失败的交易 status：Fail
     * @param message
     */
    @JmsListener(destination = TRANSACION_MESSAGE_FAIL) // 监听指定消息队列
    public void receiveFailedTransaction(Message message) {
        // 判断消息类型是TextMessage
        if (message instanceof TextMessage) {
            // 如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
                // 8. 消费消息，打印消息内容
                String text = textMessage.getText();
                logger.info("消费方监听到的【失败交易】消息为" + text);

                Gson gson = new Gson();
                TransactionEntityAll transactionEntity = gson.fromJson(text, TransactionEntityAll.class);

                String timestampStr = transactionEntity.getTimestamp();
                long timestamp = Long.parseLong(timestampStr);

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
                                .field("timestamp", timestamp / 1000)
                                .field("timestampDay", new SimpleDateFormat("yyyy-MM-dd").format(timestamp))
                                .field("status","Fail")
                                .field("gasUsed", transactionEntity.getGasUsed())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("transaction_new","data")
                            .setSource(content)
                            .get();
                    logger.info("[失败的交易]存入ES成功....");

                }catch (IOException e){
                    logger.error("【存入失败的交易异常】：", e.getMessage());
                    throw new RuntimeException("[失败的交易]存入ES时发生异常，该区块消息开始回滚至MQ队列...");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[失败交易存入ES时发生异常]，该交易开始回滚至MQ队列...");
            }
        }
    }

    /**
     * 成功的交易 status：OUT
     * @param message
     */
    @JmsListener(destination = TRANSACION_MESSAGE) // 监听指定消息队列
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

                String timestampStr = transactionEntity.getTimestamp();
                long timestamp = Long.parseLong(timestampStr);

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
                                .field("timestamp", timestamp / 1000)
                                .field("timestampDay", new SimpleDateFormat("yyyy-MM-dd").format(timestamp))
                                .field("status","OUT")
                                .field("gasUsed", transactionEntity.getGasUsed())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("transaction_new","data")
                            .setSource(content)
                            .get();
                    logger.info("[OuterTransaction]存入ES成功....");

                }catch (IOException e){
                    logger.error("【存入交易异常】：", e.getMessage());
                    throw new RuntimeException("[外部交易存入ES时发生异常]，该区块消息开始回滚至MQ队列...");
                }



            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[外部交易存入ES时发生异常]，该区块消息开始回滚至MQ队列...");
            }
        }
    }

    /**
     * @status：IN
     * @param message
     */
    @JmsListener(destination = INTERNAL_TRANSACTION_MESSAGE) // 监听指定消息队列
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

                String timestampStr = transactionEntity.getTimestamp();
                long timestamp = Long.parseLong(timestampStr);
                logger.info("[内部交易时间戳]：" + timestamp);

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
                                .field("timestamp", timestamp/1000)
                                .field("timestampDay", new SimpleDateFormat("yyyy-MM-dd").format(timestamp))
                                .field("status","IN")
                                .field("gasUsed", transactionEntity.getGasUsed())
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("transaction_new","data")
                            .setSource(content)
                            .get();
                    logger.info("[Internal Transaction]存入ES成功....");

                }catch (IOException e){
                    e.printStackTrace();
                    throw new RuntimeException("[内部交易存入ES发生异常]，该区块消息开始回滚至MQ队列...");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[内部交易存入ES发生异常]，该区块消息开始回滚至MQ队列...");
            }
        }
    }


    @JmsListener(destination = ETH_GET_TRANSACION_RECEIPT_LOG) // 监听ERC20
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
                String timestampStr = erc20Entity.getTimestamp();
                long timestamp = Long.parseLong(timestampStr);
                logger.info("[ERC20转账记录]时间戳为：" + timestamp);

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
                                    .field("timestamp", timestamp)
                                    .endObject())
                    {
                        IndexResponse response = client.prepareIndex("erc20_new","data")
                                .setSource(content)
                                .get();

                        client.prepareUpdate();
                        logger.info("[ERC20]Token Transfer存入ES成功...");

                    }catch (IOException e){
                        e.printStackTrace();
                        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new RuntimeException("[ERC20Token Transfer存入ES发生异常]，该区块消息开始回滚至MQ队列...");
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
                                    .field("timestamp", timestamp)
                                    .endObject())
                    {
                        IndexResponse response = client.prepareIndex("erc20_new","data")
                                .setSource(content)
                                .get();

                        client.prepareUpdate();
                        logger.info("[Other] Token Transfer存入ES成功...");

                    }catch (IOException e){
                        e.printStackTrace();
                        throw new RuntimeException("[ERC20Token Transfer存入ES发生异常]，该区块消息开始回滚至MQ队列...");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[ERC20Token Transfer存入ES发生异常]，该区块消息开始回滚至MQ队列...");
            }
        }
    }

    @JmsListener(destination = CALL_TRACER) // 监听主币内部转账 maincoin
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
                String timestampStr = mainCoinEntity.getTimestamp();
                long timestamp = Long.parseLong(timestampStr);
                logger.info("[主币内部转账时间戳]：" + timestamp);


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
                                .field("timestamp",timestamp)
                                .endObject())
                {
                    IndexResponse response = client.prepareIndex("maincoin_new","data")
                            .setSource(content)
                            .get();

                    client.prepareUpdate();
                    logger.info("[主币]内部转账存入ES成功...");

                }catch (IOException e){
                    e.printStackTrace();
                    throw new RuntimeException("[主币内部转账存入ES发生异常]，该区块消息开始回滚至MQ队列...");                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[主币内部转账存入ES发生异常]，该区块消息开始回滚至MQ队列...");
            }
        }
    }
}


