package com.pwnpub.search.pojo;

import java.util.List;

/**
 *
 * @date 2018-11-30 4:28 PM
 * @desc 区块实体类
 */
public class BlockEntityAllUpdate {

    /**
     * block : {"difficulty":169967,"difficultyRaw":"0x297ef","extraData":"0xd983010812846765746888676f312e31312e328664617277696e","gasLimit":2477799541,"gasLimitRaw":"0x93b03875","gasUsed":0,"gasUsedRaw":"0x0","hash":"0xc64ab73f0b2b1529bba5051855da2dacaaaa14e2d403f3903362621f6730c269","logsBloom":"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000","miner":"0xcc3dbf94038731217c8ba9f9e34ab4b7eec103e0","mixHash":"0x0912871311bfb194fb52476fd5b4434e5f1e12044d79166952c310100d5f1de9","nonce":6492489143370988800,"nonceRaw":"0x5a19f47a2ab53d00","number":563,"numberRaw":"0x233","parentHash":"0x15d78c9fc5e0766de6408371270bfe4f6448f985309b4f3fc825b8bf9033843e","receiptsRoot":"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421","sha3Uncles":"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347","size":540,"sizeRaw":"0x21c","stateRoot":"0x9a3994c5cc20911aea133471034d3380c7a82fe114a819ef3ae7e84d38d88174","timestamp":1543566372,"timestampRaw":"0x5c00f424","totalDifficulty":84318524,"totalDifficultyRaw":"0x506993c","transactions":[],"transactionsRoot":"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421","uncles":[]}
     * id : 2699
     * jsonrpc : 2.0
     * result : {"$ref":"$.block"}
     */

    private BlockBean block;
    private int id;
    private String jsonrpc;
    private ResultBean result;

    public BlockBean getBlock() {
        return block;
    }

    public void setBlock(BlockBean block) {
        this.block = block;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class BlockBean {
        /**
         * difficulty : 169967
         * difficultyRaw : 0x297ef
         * extraData : 0xd983010812846765746888676f312e31312e328664617277696e
         * gasLimit : 2477799541
         * gasLimitRaw : 0x93b03875
         * gasUsed : 0
         * gasUsedRaw : 0x0
         * hash : 0xc64ab73f0b2b1529bba5051855da2dacaaaa14e2d403f3903362621f6730c269
         * logsBloom : 0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
         * miner : 0xcc3dbf94038731217c8ba9f9e34ab4b7eec103e0
         * mixHash : 0x0912871311bfb194fb52476fd5b4434e5f1e12044d79166952c310100d5f1de9
         * nonce : 6492489143370988800
         * nonceRaw : 0x5a19f47a2ab53d00
         * number : 563
         * numberRaw : 0x233
         * parentHash : 0x15d78c9fc5e0766de6408371270bfe4f6448f985309b4f3fc825b8bf9033843e
         * receiptsRoot : 0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421
         * sha3Uncles : 0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347
         * size : 540
         * sizeRaw : 0x21c
         * stateRoot : 0x9a3994c5cc20911aea133471034d3380c7a82fe114a819ef3ae7e84d38d88174
         * timestamp : 1543566372
         * timestampRaw : 0x5c00f424
         * totalDifficulty : 84318524
         * totalDifficultyRaw : 0x506993c
         * transactions : []
         * transactionsRoot : 0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421
         * uncles : []
         */

        private String difficulty;
        private String difficultyRaw;
        private String extraData;
        private long gasLimit;
        private String gasLimitRaw;
        private String gasUsed;
        private String gasUsedRaw;
        private String hash;
        private String logsBloom;
        private String miner;
        private String mixHash;
        private long nonce;
        private String nonceRaw;
        private Integer number;
        private String numberRaw;
        private String parentHash;
        private String receiptsRoot;
        private String sha3Uncles;
        private int size;
        private String sizeRaw;
        private String stateRoot;
        private Integer timestamp;
        private String timestampRaw;
        private String totalDifficulty;
        private String totalDifficultyRaw;
        private String transactionsRoot;
        private List<?> transactions;
        private List<?> uncles;

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getDifficultyRaw() {
            return difficultyRaw;
        }

        public void setDifficultyRaw(String difficultyRaw) {
            this.difficultyRaw = difficultyRaw;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public long getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(long gasLimit) {
            this.gasLimit = gasLimit;
        }

        public String getGasLimitRaw() {
            return gasLimitRaw;
        }

        public void setGasLimitRaw(String gasLimitRaw) {
            this.gasLimitRaw = gasLimitRaw;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getGasUsedRaw() {
            return gasUsedRaw;
        }

        public void setGasUsedRaw(String gasUsedRaw) {
            this.gasUsedRaw = gasUsedRaw;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public String getMixHash() {
            return mixHash;
        }

        public void setMixHash(String mixHash) {
            this.mixHash = mixHash;
        }

        public long getNonce() {
            return nonce;
        }

        public void setNonce(long nonce) {
            this.nonce = nonce;
        }

        public String getNonceRaw() {
            return nonceRaw;
        }

        public void setNonceRaw(String nonceRaw) {
            this.nonceRaw = nonceRaw;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getNumberRaw() {
            return numberRaw;
        }

        public void setNumberRaw(String numberRaw) {
            this.numberRaw = numberRaw;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getSha3Uncles() {
            return sha3Uncles;
        }

        public void setSha3Uncles(String sha3Uncles) {
            this.sha3Uncles = sha3Uncles;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getSizeRaw() {
            return sizeRaw;
        }

        public void setSizeRaw(String sizeRaw) {
            this.sizeRaw = sizeRaw;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestampRaw() {
            return timestampRaw;
        }

        public void setTimestampRaw(String timestampRaw) {
            this.timestampRaw = timestampRaw;
        }

        public String getTotalDifficulty() {
            return totalDifficulty;
        }

        public void setTotalDifficulty(String totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
        }

        public String getTotalDifficultyRaw() {
            return totalDifficultyRaw;
        }

        public void setTotalDifficultyRaw(String totalDifficultyRaw) {
            this.totalDifficultyRaw = totalDifficultyRaw;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public List<?> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<?> transactions) {
            this.transactions = transactions;
        }

        public List<?> getUncles() {
            return uncles;
        }

        public void setUncles(List<?> uncles) {
            this.uncles = uncles;
        }
    }

    public static class ResultBean {
        /**
         * $ref : $.block
         */

        private String $ref;

        public String get$ref() {
            return $ref;
        }

        public void set$ref(String $ref) {
            this.$ref = $ref;
        }
    }
}
