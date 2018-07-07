package com.sharingif.blockchain.api.crypto.service;

import com.sharingif.blockchain.api.crypto.entity.MnemonicGenerateReq;
import com.sharingif.blockchain.api.crypto.entity.MnemonicGenerateRsp;

/**
 * MnemonicService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/5 下午7:54
 */
public interface MnemonicService {

    /**
     * 生成助记词
     * @param req
     * @return
     */
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);


}
