package com.sharingif.blockchain.app.components.erc20.ole;

import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;

/**
 * ApprovalEventResponse
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/10 下午5:29
 */
public class ApprovalEventResponse {
    public Log log;

    public String owner;

    public String spender;

    public BigInteger value;
}
