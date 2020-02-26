package com.pwnpub.search.pojo;

/**
 *
 * @date 2019-01-08 5:10 PM
 * @desc 新-主币内部转账
 */
public class MainCoinEntityAll {


    /**
     * from : 0x43b7525973714ea6d6f4750366001d9cde4563ff
     * gasCost : 9700
     * gasIn : 1050458
     * input : 0x
     * outLen : 0
     * outOff : 128
     * to : 0xf6bec0bf0e8b2b9e0542e8308e982c70f32f0d12
     * type : CALL
     * value : 0x1628f923c958000
     * transactionHash : aaa
     * blockNumber : aaa
     */

    private String from; //
    private String gasCost;//
    private String gasIn;//
    private String input;//
    private String outLen;
    private String outOff;
    private String to;//
    private String type;//
    private String value;//
    private String transactionHash;//
    private Integer blockNumber;//
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGasCost() {
        return gasCost;
    }

    public void setGasCost(String gasCost) {
        this.gasCost = gasCost;
    }

    public String getGasIn() {
        return gasIn;
    }

    public void setGasIn(String gasIn) {
        this.gasIn = gasIn;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutLen() {
        return outLen;
    }

    public void setOutLen(String outLen) {
        this.outLen = outLen;
    }

    public String getOutOff() {
        return outOff;
    }

    public void setOutOff(String outOff) {
        this.outOff = outOff;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }
}
