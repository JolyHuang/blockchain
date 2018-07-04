package com.sharingif.blockchain.crypto.mnemonic.service;

import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateRsp;

/**
 * MnemonicService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/2 下午4:43
 */
public interface MnemonicService {

    /**
     * 生成助记词
     * @param req
     * @return
     */
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);

}
