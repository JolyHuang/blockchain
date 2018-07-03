package com.sharingif.blockchain.crypto.app.autoconfigure.components;

import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentsAutoconfigure {

    @Bean(name="sha256Encryptor")
    public SHA256Encryptor createSHA256Encryptor() {
        SHA256Encryptor sha256Encryptor = new SHA256Encryptor();

        return sha256Encryptor;
    }

    @Bean(name="base64Coder")
    public Base64Coder createBase64Coder() {
        Base64Coder base64Coder = new Base64Coder();

        return base64Coder;
    }

}
