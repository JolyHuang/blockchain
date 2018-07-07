package com.sharingif.blockchain.api.crypto.service;

import com.sharingif.blockchain.api.crypto.entity.MnemonicGenerateReq;
import com.sharingif.blockchain.api.crypto.entity.MnemonicGenerateRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

/**
 * MnemonicService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/5 下午7:54
 */
@RequestMapping(value="mnemonic")
public interface MnemonicApiService {

    /**
     * 生成助记词
     * @param req
     * @return
     */
    @RequestMapping(value="generate", method= RequestMethod.POST)
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);


}
