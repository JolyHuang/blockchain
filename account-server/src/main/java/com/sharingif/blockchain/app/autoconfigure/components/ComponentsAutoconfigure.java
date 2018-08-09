package com.sharingif.blockchain.app.autoconfigure.components;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.cube.security.binary.Base64Coder;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import com.sharingif.cube.security.confidentiality.encrypt.aes.AESECBEncryptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Configuration
public class ComponentsAutoconfigure {

    @Bean("passwordTextEncryptor")
    public TextEncryptor createPasswordTextEncryptor(@Value("${password.key}") String key) {
        Base64Coder base64Coder = new Base64Coder();
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
    public OleContract createOleContract(Web3j web3j, @Value("${ole.contract.address}") String oleContractAddress) {
        Credentials credentials =Credentials.create("75846771996958358843340910652988727472477619126081108028885364801697837883534");

        OleContract oleContract = OleContract.load(
                oleContractAddress
                ,web3j
                ,credentials
                ,BigInteger.ZERO
                ,BigInteger.ZERO
        );

        return oleContract;
    }

    @Bean("btcdClient")
    public BtcdClient createBtcdClient(
            @Value("${node.bitcoind.rpc.protocol}")String rpcProtocol
            ,@Value("${node.bitcoind.rpc.host}")String rpcHost
            ,@Value("${node.bitcoind.rpc.port}")Integer rpcPort
            ,@Value("${node.bitcoind.rpc.user}")String rpcUser
            ,@Value("${node.bitcoind.rpc.password}")String rpcPassword
            ,@Value("${node.bitcoind.http.auth_scheme}")String httpAuthScheme
    ) throws BitcoindException, CommunicationException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(200);
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(connManager)
                .build();

        BtcdClient btcdClient = new BtcdClientImpl(httpProvider, rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, httpAuthScheme);

        return btcdClient;
    }

}
