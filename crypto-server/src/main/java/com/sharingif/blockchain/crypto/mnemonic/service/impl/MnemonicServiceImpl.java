package com.sharingif.blockchain.crypto.mnemonic.service.impl;

import com.sharingif.bips.bip0032.Mnemonic;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateRsp;
import com.sharingif.blockchain.crypto.app.components.Keystore;
import com.sharingif.blockchain.crypto.mnemonic.service.MnemonicService;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.core.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MnemonicServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/2 下午4:44
 */
@Service
public class MnemonicServiceImpl implements MnemonicService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Keystore keystore;

    @Resource
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    @Override
    public MnemonicGenerateRsp generate(MnemonicGenerateReq req) {
        // 生成助记词
        Mnemonic mnemonic = new Mnemonic(req.getLocale(), req.getLength());

        // 生成返回对象
        MnemonicGenerateRsp rsp = new MnemonicGenerateRsp();
        rsp.setMnemonicId(UUIDUtils.generateUUID());
        rsp.setFileName(new StringBuilder(rsp.getMnemonicId()).append(".").append(Keystore.MNEMONIC_FILE_POSTFIX).toString());
        rsp.setMnemonic(mnemonic.getMnemonic());

        // 助记词加密存储
        try {
            keystore.persistence(rsp.getMnemonicId(), rsp.getFileName(), mnemonic.getMnemonic(), req.getPassword());
        } catch (Exception e) {
            throw new ValidationCubeException("generate mnemonic error");
        }

        return rsp;
    }

}
