package com.sharingif.ethereum.web3j;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.security.binary.HexCoder;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import rx.Subscription;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.web3j.crypto.Hash.sha256;

/**
 * TODO
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/4/3 上午11:04
 */
public class TestWeb3j {

    Web3j web3j = Web3j.build(new HttpService("http://47.88.156.133:7236"));

    String address = "0x5b6695966503a4618b3cc8D1Ed7768BFb3757750";


    public BigInteger getBalance(String address) {

        EthGetBalance ethGetBalance;
        try {
            ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        } catch (Exception e) {
            throw new CubeRuntimeException("get balance error", e);
        }

        return ethGetBalance.getBalance();
    }

    @Test
    public void testGetBalance() {
        BigInteger balance = getBalance(address);
        System.out.println(balance);
        System.out.println(Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString());
    }

    @Test
    public void testGetTransactionByHash() throws IOException {
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash("0x28d71467b2697ff1b7f628af231fd0992b25b5cf0c24060d54bb60306555f07b").send();

        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(ethTransaction.getTransaction().get().getHash()).send();
        System.out.println(ethGetTransactionReceipt.getTransactionReceipt().get().getStatus());
    }

    @Test
    public void testGetTransactionListByAddress() throws IOException {
        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

        BigInteger transactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        System.out.println(transactionCount);

    }

    @Test
    public void test() {
        Subscription subscription = web3j.replayTransactionsObservable(
                new DefaultBlockParameterNumber(new BigInteger("5662012")), new DefaultBlockParameterNumber(new BigInteger("5662154")))
        .subscribe(tx -> {
            if(address.equals(tx.getFrom()) || address.equals(tx.getTo())) {
                System.out.println("TxHash:"+tx.getHash()+" Block:"+tx.getBlockNumber()+" From:"+tx.getFrom()+" To:"+tx.getTo()+ " Value:"+tx.getValue());
            }
        });
    }

    @Test
    public void testMnemonicUtils(){
        byte[] seed = MnemonicUtils.generateSeed("wear position lyrics exit coach purchase various genre cushion oppose arctic nation", "");
        HexCoder hexCoder = new HexCoder();
        System.out.println(hexCoder.encode(seed));
    }

    @Test
    public void testPrivateKey() throws UnreadableWalletException {
        HexCoder hexCoder = new HexCoder();

        Credentials credentials = Credentials.create("aeaca182682165acdaf0763efa93d2ea9baf581ee3275a3408b773314855b4b2");
        System.out.println(credentials.getAddress());
//
//        byte[] extendKey = hexCoder.decode("0488ade40537dc776e00000000fb328affb5ae06a64bb1ed7e3a0a91e29994714734b71677d06b3316c27cf04100aeaca182682165acdaf0763efa93d2ea9baf581ee3275a3408b773314855b4b296e5e886");
//        byte[] privateKey = new byte[32];
//        System.arraycopy(extendKey, 0 , privateKey, 0, privateKey.length);
//
//        Credentials credentials2 = Credentials.create(ECKeyPair.create(sha256(privateKey)));
//        System.out.println(credentials2.getAddress());
//
//        Credentials credentials3 = Credentials.create(ECKeyPair.create(sha256(hexCoder.decode("0488ade40537dc776e00000000fb328affb5ae06a64bb1ed7e3a0a91e29994714734b71677d06b3316c27cf04100aeaca182682165acdaf0763efa93d2ea9baf581ee3275a3408b773314855b4b296e5e886"))));
//        System.out.println(credentials3.getAddress());
//
        Credentials credentials4 = Credentials.create(ECKeyPair.create(hexCoder.decode("2a5fbc7791ee5631f30c86ca880ffe5465156aa92e5edd29c62187eac1719f4a")));
        System.out.println(credentials4.getAddress());
//
        String seedCode = "tunnel maple valve card settle author cream vibrant beauty ocean mixed toe";

        // BitcoinJ
        DeterministicSeed seed = new DeterministicSeed(seedCode, null, "", 0l);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

        System.out.println("private:"+hexCoder.encode(key.getPrivKeyBytes()));
        System.out.println("publicKey:"+Hex.toHexString(key.getPubKey()));
        System.out.println("chainCode:"+hexCoder.encode(key.getChainCode()));
        System.out.println("path:"+key.getPath());
        System.out.println("childNumber:"+key.getChildNumber());



        String serializePrivB58 = key.serializePrivB58(MainNetParams.get());
        DeterministicKey deterministicKey = DeterministicKey.deserializeB58(serializePrivB58, MainNetParams.get());
        deterministicKey = new DeterministicKey(HDUtils.append(HDUtils.parsePath("M/44H/60H/0H"), ChildNumber.ZERO), deterministicKey.getChainCode(), deterministicKey.getPrivKey(), deterministicKey);

        deterministicKey = HDKeyDerivation.deriveChildKey(deterministicKey, new ChildNumber(0, false));
        System.out.println("deterministicKey2:"+deterministicKey.getPath());
        System.out.println("childDeterministicKey:"+deterministicKey.getPath());
        // Web3j
        Credentials credentials6 = Credentials.create(deterministicKey.getPrivKey().toString(16));
        System.out.println(credentials6.getAddress());

    }

    public static void getUserExternalAddress(final String pubKey, final NetworkParameters networkParameters, final long userIndex) {

        DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);

        List<ChildNumber> child = null;
        if (deterministicKey.getDepth() == 2) {

            /* M/44'/0' node tpub */
            child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(1/*user*/, false),
                    new ChildNumber(0/*external*/, false), new ChildNumber((int) userIndex, false));

        } else if (deterministicKey.getDepth() == 3) {

            /* M/44'/0'/X context tpub */
            child = ImmutableList.of(new ChildNumber(1/*user*/, false),
                    new ChildNumber(0/*external*/, false), new ChildNumber((int) userIndex, false));
        }

        DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
        imprintingKey.toAddress(networkParameters);
    }

    @Test
    public void testMnemonicCode() throws IOException, MnemonicException.MnemonicLengthException {
        byte[] entropy = new HexCoder().decode("3ee74309d55715669d2649bbf63b9ab9");

        MnemonicCode ms = new MnemonicCode();
        List<String> mnemonic = ms.toMnemonic(entropy);
        String mnemonicStr = Joiner.on(" ").join(mnemonic);


        System.out.println(mnemonicStr);
    }

    @Test
    public void testConnect() throws Exception {
        Date currentDate = new Date();


        System.out.println((new Date().getTime() - currentDate.getTime()) / 1000);


        System.out.println(web3j.netVersion().send().getNetVersion());
        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

        EthAccounts ethAccounts = web3j.ethAccounts().send();
        System.out.println("size:==========>" + ethAccounts.getAccounts());
        for (String ethAccount : ethAccounts.getAccounts()) {
            try {
                EthGetBalance ethGetBalance = web3j.ethGetBalance(ethAccount, DefaultBlockParameterName.LATEST).send();
                BigInteger ethBalance = ethGetBalance.getBalance();
                if (BigInteger.ZERO.compareTo(ethBalance) == 0) {
                    continue;
                }
                ethBalance = ethBalance.subtract(Convert.toWei("0.00021", Convert.Unit.ETHER).toBigInteger());
                if (BigInteger.ZERO.compareTo(ethBalance) == 0) {
                    continue;
                }
                System.out.println("ethAccount:"+ethAccount+",ethBalance:"+Convert.fromWei(ethBalance.toString(), Convert.Unit.ETHER).toString());
//                TransactionManager transactionManager = new ClientTransactionManager(web3j, ethAccount);
//                new Transfer(web3j, transactionManager).sendFunds(
//                        "0x9fec7467d4d56d93de9145e77a6ae921ef61cd2a",
//                        new BigDecimal(String.valueOf(ethBalance)),
//                        Convert.Unit.WEI
//                ).send();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
