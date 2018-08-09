package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.service.AddressNoticeSignatureService;
import com.sharingif.cube.security.binary.Base64Coder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * AddressNoticeSignatureServiceimpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:45
 */
@Service
public class AddressNoticeSignatureServiceimpl implements AddressNoticeSignatureService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Signature signature;
    private Base64Coder base64Coder;

    public Signature getSignature() {
        return signature;
    }
    public Base64Coder getBase64Coder() {
        return base64Coder;
    }
    @Resource
    public void setBase64Coder(Base64Coder base64Coder) {
        this.base64Coder = base64Coder;
    }
    @Value("${deposit.callback.private}")
    public void setDepositCallbackPrivate(String depositCallbackPrivate) {
        try {
            RSAPrivateKey rsaPrivateKey = RSAPrivateCrtKeyImpl.newKey(base64Coder.decode(depositCallbackPrivate));

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
        } catch (Exception e) {
            logger.error("invalid key exception", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sign(byte[] signData) {
        try {
            getSignature().update(signData);
            byte[] result = getSignature().sign();
            return getBase64Coder().encode(result);
        } catch (SignatureException e) {
            logger.error("signature exception", e);
            throw new RuntimeException(e);
        }
    }

}
