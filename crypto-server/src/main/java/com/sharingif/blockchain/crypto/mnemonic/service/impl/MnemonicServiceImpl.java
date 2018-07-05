package com.sharingif.blockchain.crypto.mnemonic.service.impl;

import com.sharingif.bips.bip0032.Mnemonic;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateRsp;
import com.sharingif.blockchain.crypto.app.components.Keystore;
import com.sharingif.blockchain.crypto.app.constants.ErrorConstants;
import com.sharingif.blockchain.crypto.mnemonic.service.MnemonicService;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.core.util.UUIDUtils;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;
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
    private SHA256Encryptor sha256Encryptor;

    @Resource
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    @Resource
    public void setSha256Encryptor(SHA256Encryptor sha256Encryptor) {
        this.sha256Encryptor = sha256Encryptor;
    }

    @Override
    public MnemonicGenerateRsp generate(MnemonicGenerateReq req) {
        // 生成助记词
        Mnemonic mnemonic = new Mnemonic(req.getLocale(), req.getLength());

        String directory = UUIDUtils.generateUUID();
        String fileName = sha256Encryptor.encrypt(mnemonic.getMnemonic());

        String filePath;
        // 助记词加密存储
        try {
            filePath = keystore.persistence(directory, fileName, mnemonic.getMnemonic(), req.getPassword());
        } catch (Exception e) {
            throw new ValidationCubeException(ErrorConstants.GENERATE_MNEMONIC_ERROR);
        }

        // 生成返回对象
        MnemonicGenerateRsp rsp = new MnemonicGenerateRsp();
        rsp.setMnemonic(mnemonic.getMnemonic());
        rsp.setFilePath(filePath);

        return rsp;
    }

}
