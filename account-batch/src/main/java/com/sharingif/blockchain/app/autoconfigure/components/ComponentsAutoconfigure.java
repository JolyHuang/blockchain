package com.sharingif.blockchain.app.autoconfigure.components;

import com.sharingif.blockchain.app.ole.OleContract;
import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import com.sharingif.cube.security.confidentiality.encrypt.aes.AESECBEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Configuration
public class ComponentsAutoconfigure {

    @Bean(name="base64Coder")
    public Base64Coder createBase64Coder() {
        Base64Coder base64Coder = new Base64Coder();

        return base64Coder;
    }

    @Bean("passwordTextEncryptor")
    public TextEncryptor createPasswordTextEncryptor(@Value("${password.key}") String key, Base64Coder base64Coder) {
        byte[] keysByte = base64Coder.decode(key);
        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        return encryptor;
    }

    @Bean("propertyTextEncryptor")
    public TextEncryptor createPropertyTextEncryptor(@Value("${property.key}") String key) {
        Base64Coder base64Coder = new Base64Coder();
        byte[] keysByte = base64Coder.decode(key);
        AESECBEncryptor encryptor = new AESECBEncryptor(keysByte, base64Coder);

        return encryptor;
    }

    @Bean("web3j")
    public Web3j createWeb3j(@Value("${eth.web3j.address}")String ethWeb3jAddress) {
        return Web3j.build(new HttpService(ethWeb3jAddress));
    }

    @Bean("oleContract")
    public OleContract createOleContract(Web3j web3j) {
        Credentials credentials =Credentials.create("75846771996958358843340910652988727472477619126081108028885364801697837883534");

        OleContract oleContract = OleContract.load(
                "0x9d9223436dDD466FC247e9dbbD20207e640fEf58"
                ,web3j
                ,credentials
                ,BigInteger.ZERO
                ,BigInteger.ZERO
        );

        return oleContract;
    }

}
