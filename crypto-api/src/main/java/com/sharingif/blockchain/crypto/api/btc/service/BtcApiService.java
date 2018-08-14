package com.sharingif.blockchain.crypto.api.btc.service;

import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

/**
 * BtcApiService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午3:56
 */
public interface BtcApiService {

    /**
     * 生成transfer交易
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    BtcTransferRsp transfer(BtcTransferReq req);

}
