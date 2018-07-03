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

    /**
     * 加密种子
     * @param mnemonic
     * @param password
     * @return
     */
    String encrypt(String mnemonic, String password);

    /**
     * 解密种子
     * @param id : 种子sha256后的值
     * @param password
     * @return
     */
    String decrypt(String id, String password);

}
