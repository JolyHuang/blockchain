package com.sharingif.blockchain.common.components.ole;

import org.web3j.protocol.core.methods.response.Log;

/**
 * OwnershipTransferredEventResponse
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/10 下午5:31
 */
public class OwnershipTransferredEventResponse {
    public Log log;

    public String previousOwner;

    public String newOwner;
}
