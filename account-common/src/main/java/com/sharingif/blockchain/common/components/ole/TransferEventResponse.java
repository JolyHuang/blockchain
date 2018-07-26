package com.sharingif.blockchain.common.components.ole;

import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;

/**
 * TransferEventResponse
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/10 下午5:29
 */
public class TransferEventResponse {
    public Log log;

    public String from;

    public String to;

    public BigInteger value;
}
