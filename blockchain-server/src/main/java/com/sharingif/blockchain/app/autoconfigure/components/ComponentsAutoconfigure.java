package com.sharingif.blockchain.app.autoconfigure.components;

import com.sharingif.blockchain.app.components.erc20.ole.OleContract;
import com.sharingif.blockchain.eth.service.Web3jService;
import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import com.sharingif.cube.security.confidentiality.encrypt.aes.AESECBEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@Configuration
public class ComponentsAutoconfigure {

    @Bean("propertyTextEncryptor")
    public TextEncryptor createPropertyTextEncryptor(@Value("${property.key}") String key) throws UnsupportedEncodingException {
        Base64Coder base64Coder = new Base64Coder();
        byte[] keysByte = base64Coder.decode(key);
        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        return encryptor;
    }

    @Bean("oleContract")
    public OleContract createOleContract(Web3jService web3jService) {
        Credentials credentials =Credentials.create("75846771996958358843340910652988727472477619126081108028885364801697837883534");

        OleContract oleContract = OleContract.load(
                "0x9d9223436dDD466FC247e9dbbD20207e640fEf58"
                ,web3jService.getCurrentAvailableWeb3j()
                ,credentials
                ,BigInteger.ZERO
                ,BigInteger.ZERO
        );

        return oleContract;
    }

}
