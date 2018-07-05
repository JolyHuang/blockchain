package com.sharingif.blockchain.crypto.api.mnemonic.service;

import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateRsp;

/**
 * MnemonicApiService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/5 下午10:42
 */
public interface MnemonicApiService {

    /**
     * 生成助记词
     * @param req
     * @return
     */
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);

}
