package com.sharingif.blockchain.crypto.mnemonic.service.impl;

import com.sharingif.bips.bip0032.Mnemonic;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.MnemonicGenerateRsp;
import com.sharingif.blockchain.crypto.mnemonic.service.MnemonicService;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.core.util.Charset;
import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.aes.AESECBEncryptor;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;

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

    private String keyRootPath;
    private SHA256Encryptor sha256Encryptor;
    private Base64Coder base64Coder;

    @Value("${key.root.path}")
    public void setKeyRootPath(String keyRootPath) {
        this.keyRootPath = keyRootPath;
    }
    @Resource
    public void setSha256Encryptor(SHA256Encryptor sha256Encryptor) {
        this.sha256Encryptor = sha256Encryptor;
    }
    @Resource
    public void setBase64Coder(Base64Coder base64Coder) {
        this.base64Coder = base64Coder;
    }

    @Override
    public MnemonicGenerateRsp generate(MnemonicGenerateReq req) {
        // 生成助记词
        Mnemonic mnemonic = new Mnemonic(req.getLocale(), req.getLength());
        // 生成文件名
        String fileName = sha256Encryptor.encrypt(mnemonic.getMnemonic());

        // 生成返回对象
        MnemonicGenerateRsp rsp = new MnemonicGenerateRsp();
        rsp.setId(fileName);
        rsp.setMnemonic(mnemonic.getMnemonic());

        // 助记词j加密
        String encryptMnemonic = encrypt(mnemonic.getMnemonic(), req.getPassword());

        // 生成保存文件路径
        String filePath = new StringBuilder(keyRootPath).append("/").append(fileName).toString();
        File mnemonicFilePath = new File(filePath);
        if(!(mnemonicFilePath.mkdirs())) {
            logger.error("create directory error, path:{}", filePath);
            throwGenerateMnemonicError();
        }
        String fileNamePath = new StringBuilder(filePath).append("/").append(fileName).toString();
        File mnemonicFileNamePath = new File(fileNamePath);
        try {
            mnemonicFileNamePath.createNewFile();
        } catch (IOException e) {
            logger.error("create file error, file name:{}", fileNamePath);
            throwGenerateMnemonicError();
        }

        // 保存助记词
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(mnemonicFileNamePath);
            fileWriter.write(encryptMnemonic);
        } catch (IOException e) {
            logger.error("write file error, file name:{}", fileNamePath);
            throwGenerateMnemonicError();
        } finally {
            try {
                if(fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                logger.error("close fileWriter error", e);
                throwGenerateMnemonicError();
            }
        }


        return rsp;
    }

    protected void throwGenerateMnemonicError() {
        throw new ValidationCubeException("generate mnemonic error");
    }

    protected void throwMnemonicPasswordError() {
        throw new ValidationCubeException("mnemonic password error");
    }

    @Override
    public String encrypt(String mnemonic, String password) {

        byte[] passwordBytes = getUTF8Bytes(password);
        byte[] keysByte = sha256Encryptor.encrypt(passwordBytes);

        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        return encryptor.encrypt(mnemonic);
    }

    @Override
    public String decrypt(String id, String password) {

        String fileNamePath = new StringBuilder(keyRootPath).append("/").append(id).append("/").append(id).toString();
        File file = new File(fileNamePath);
        Long fileLength = file.length();
        byte[] mnemonicByte = new byte[fileLength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(mnemonicByte);
        } catch (IOException e) {
            logger.error("read file error", e);
            throwMnemonicPasswordError();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] passwordBytes = getUTF8Bytes(password);
        byte[] keysByte = sha256Encryptor.encrypt(passwordBytes);
        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        String mnemonicBase64 = new String(mnemonicByte);
        String mnemonic = encryptor.decrypt(mnemonicBase64);

        // 生成文件名
        String mnemonicSha256 = sha256Encryptor.encrypt(mnemonic);

        if(!(id.equals(mnemonicSha256))) {
            throwMnemonicPasswordError();
        }

        return mnemonic;
    }

    protected byte[] getUTF8Bytes(String text) {
        try {
            return text.getBytes(Charset.UTF8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new CubeRuntimeException("unsupported encoding exception", e);
        }
    }

}
