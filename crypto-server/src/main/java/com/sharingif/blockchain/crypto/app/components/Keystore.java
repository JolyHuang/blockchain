package com.sharingif.blockchain.crypto.app.components;

import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.core.util.Charset;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.aes.AESECBEncryptor;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Keystore
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 下午1:28
 */
public class Keystore {

    public static final String MNEMONIC_FILE_POSTFIX = "mnemonic";
    public static final String EXTENDEDKEY_FILE_POSTFIX = "extendedkey";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String keyRootPath;
    private SHA256Encryptor sha256Encryptor;
    private Base64Coder base64Coder;

    public Keystore(String keyRootPath, SHA256Encryptor sha256Encryptor, Base64Coder base64Coder) {
        this.keyRootPath = keyRootPath;
        this.sha256Encryptor = sha256Encryptor;
        this.base64Coder = base64Coder;
    }

    public String getKeyRootPath() {
        return keyRootPath;
    }

    public String encrypt(String text, String password) {

        byte[] passwordBytes = getUTF8Bytes(password);
        byte[] keysByte = sha256Encryptor.encrypt(passwordBytes);

        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        return encryptor.encrypt(text);
    }

    public String decrypt(String filePath, String password) {

        filePath = new StringBuilder(keyRootPath).append("/").append(filePath).toString();

        File filePathFile = new File(filePath);
        Long fileLength = filePathFile.length();
        byte[] mnemonicByte = new byte[fileLength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(filePathFile);
            in.read(mnemonicByte);
        } catch (IOException e) {
            logger.error("read file error", e);
            throw new RuntimeException(e);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close FileInputStream error", e);
                    throw new RuntimeException(e);
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

        if(!(filePathFile.getName().equals(mnemonicSha256))) {
            throw new ValidationCubeException("password error");
        }

        return mnemonic;
    }

    public String persistenceExtendedKey(String mnemonicId, String keyPath, String fileName, String text, String password) {
        String directory = new StringBuilder(mnemonicId).append("/").append(keyPath).toString();
        return persistence(directory, fileName, text, password);
    }

    public String persistence(String directory, String fileName, String text, String password) {

        StringBuilder directoryStringBuilder = new StringBuilder(keyRootPath);
        if(!StringUtils.isTrimEmpty(directory)) {
            directoryStringBuilder.append("/").append(directory);
        }

        // 加密文本
        String encryptText = encrypt(text, password);

        // 生成保存文件路径
        directory = directoryStringBuilder.toString();
        File directoryFile = new File(directory);
        if(!(directoryFile.mkdirs())) {
            logger.error("create directory error, directory:{}", directoryFile);
            throw new ValidationCubeException("create directory error");
        }
        String filePathStr = directoryStringBuilder.append("/").append(fileName).toString();
        File filePath = new File(filePathStr);
        try {
            filePath.createNewFile();
        } catch (IOException e) {
            logger.error("create file error, file name:{}", filePathStr);
            throw new RuntimeException(e);
        }

        // 保存助记词
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(encryptText);
        } catch (IOException e) {
            logger.error("write file error, file name:{}", filePathStr);
            throw new RuntimeException(e);
        } finally {
            try {
                if(fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                logger.error("close fileWriter error", e);
                throw new RuntimeException(e);
            }
        }

        return filePathStr;
    }

    public String load(String filePath, String password) {
        return decrypt(filePath, password);
    }

    protected byte[] getUTF8Bytes(String text) {
        try {
            return text.getBytes(Charset.UTF8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("unsupported encoding exception", e);
            throw new RuntimeException(e);
        }
    }

}
