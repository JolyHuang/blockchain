package com.sharingif.blockchain.crypto.mnemonic.service;

import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

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
    @RequestMapping(value="generate", method= RequestMethod.POST)
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);

}
