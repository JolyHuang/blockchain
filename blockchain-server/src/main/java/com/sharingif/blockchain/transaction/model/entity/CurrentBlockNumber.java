package com.sharingif.blockchain.transaction.model.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * CurrentBlockNumber
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/13 下午12:24
 */
public class CurrentBlockNumber {

    private BigInteger currentBlockNumber;
    private Date timestamp;

    public BigInteger getCurrentBlockNumber() {
        return currentBlockNumber;
    }

    public void setCurrentBlockNumber(BigInteger currentBlockNumber) {
        this.currentBlockNumber = currentBlockNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = new Date(timestamp.multiply(new BigInteger("1000")).longValue());
    }
}
