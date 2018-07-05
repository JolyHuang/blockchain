package com.sharingif.blockchain.crypto.service;

import com.sharingif.blockchain.api.crypto.MnemonicGenerateReq;
import com.sharingif.blockchain.api.crypto.MnemonicGenerateRsp;
import com.sharingif.blockchain.crypto.model.entity.Mnemonic;
import com.sharingif.cube.support.service.base.IBaseService;

/**
 * MnemonicService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/5 下午7:54
 */
public interface MnemonicService extends IBaseService<Mnemonic, String> {

    /**
     * 生成助记词
     * @param req
     * @return
     */
    MnemonicGenerateRsp generate(MnemonicGenerateReq req);

}
