import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.util.DateUtils;
import com.sharingif.cube.security.binary.HexCoder;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.junit.Test;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import rx.Subscription;
import rx.functions.Action0;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
//    Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
//        Web3j web3j = Web3j.build(new HttpService("http://159.203.171.169:8545"));

    String address = "0x5b6695966503a4618b3cc8D1Ed7768BFb3757750";


    @Test
    public void testConnection() throws IOException {
        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());
    }

    @Test
    public void testBlockTimestamp() throws IOException {
        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());
        Transaction transaction = web3j.ethGetTransactionByHash("0xad2c0db6794b53033e3937a5240e34b8f4996edfaabd9906bc06457b76a56b8b").send().getTransaction().get();
        EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(transaction.getBlockNumber()), false).send().getBlock();

        Date date = new Date(block.getTimestamp().multiply(new BigInteger("1000")).longValue());
        System.out.println(DateUtils.getDate(date, DateUtils.DATETIME_MILLI_ISO_FORMAT));
    }

    @Test
    public void testActualFee() throws IOException {
        Transaction transaction = web3j.ethGetTransactionByHash("0xad2c0db6794b53033e3937a5240e34b8f4996edfaabd9906bc06457b76a56b8b").send().getTransaction().get();

        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send().getTransactionReceipt().get();

        System.out.println(transactionReceipt.getStatus());
        System.out.println(transaction.getGas());
        System.out.println(transactionReceipt.getGasUsed());
        System.out.println(transaction.getGasPrice());
        System.out.println(Convert.fromWei(transaction.getGas().multiply(transaction.getGasPrice()).toString(), Convert.Unit.ETHER));
    }

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
    public void testGetTransactionByHash() throws Exception {
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash("0x28d71467b2697ff1b7f628af231fd0992b25b5cf0c24060d54bb60306555f07b").send();

//        String method = inputData.substring(0,10);
//        System.out.println(method);
//        String to = inputData.substring(10,74);
//        String value = inputData.substring(74);
//
//        System.out.println();
//
//        Method refMethod = TypeDecoder.class.getDeclaredMethod("transfer",String.class,int.class,Class.class);
//        refMethod.setAccessible(true);
//        Address address = (Address)refMethod.invoke(null,to,0,Address.class);
//        System.out.println(address.toString());
//        Uint256 amount = (Uint256) refMethod.invoke(null,value,0,Uint256.class);
//        System.out.println(amount.getValue());


//        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(ethTransaction.getTransaction().get().getHash()).send();
//
//        List<Log> logs = ethGetTransactionReceipt.getTransactionReceipt().get().getLogs();
//        Log log = logs.get(0);
//        EventValues eventValues = OleContract.staticExtractEventParameters(OleContract.TRANSFER_EVENT, log);
//        System.out.println(eventValues);
//        BigInteger value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
//        System.out.println(Convert.fromWei(value.toString(), Convert.Unit.ETHER));
//
//        System.out.println(ethGetTransactionReceipt.getTransactionReceipt().get().getStatus());
    }

    @Test
    public void testInput() throws Exception {
//        String inputData = "0xa9059cbb0000000000000000000000005b6695966503a4618b3cc8d1ed7768bfb37577500000000000000000000000000000000000000000000000000de0b6b3a7640000";
//
//        Function function = new Function(
//                OleContract.FUNC_TRANSFER,
//                Arrays.<Type>asList(Address.DEFAULT, Uint256.DEFAULT),
//                Collections.<TypeReference<?>>emptyList());
//
//        String functionSignature = FunctionEncoder.encode(function);
//
//        String method = inputData.substring(0,10);
//
//        if(method.equals(functionSignature.substring(0,10))) {
//            System.out.println(method);
//            String to = inputData.substring(10,74);
//            String value = inputData.substring(74);
//
//            List<Type> results = FunctionReturnDecoder.decode(
//                    inputData.substring(10),
//                    Utils.convert(Arrays.<TypeReference<?>>asList(
//                            new TypeReference<Address>(){},
//                            new TypeReference<Uint256>(){})
//                    )
//            );
//
//        }

    }


    @Test
    public void testSolidityFunctionWrapperGenerator() {

    }

    @Test
    public void testGetTransactionListByAddress() throws IOException {
        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

        BigInteger transactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        System.out.println(transactionCount);

    }


    @Test
    public void test() {




    }

    @Test
    public void testMnemonicUtils(){
        byte[] seed = MnemonicUtils.generateSeed("wear position lyrics exit coach purchase various genre cushion oppose arctic nation", "");
        HexCoder hexCoder = new HexCoder();
        System.out.println(hexCoder.encode(seed));
    }

    @Test
    public void testPrivateKey() throws UnreadableWalletException {
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
//
        String seedCode = "wear position lyrics exit coach purchase various genre cushion oppose arctic nation";

        // BitcoinJ1H7Vqj2VutV9L67hwQgtPk5c8UJXg3FHdB
        DeterministicSeed seed = new DeterministicSeed(seedCode, null, "", 0l);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/0H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);

//        Credentials credentials6 = Credentials.create(key.getPrivKey().toString(16));
//        System.out.println(credentials6.getAddress());


        System.out.println(key.toAddress(MainNetParams.get()));

//        BigInteger privKey = key.getPrivKey();
//
//        System.out.println("private:"+hexCoder.encode(key.getPrivKeyBytes()));
//        System.out.println("publicKey:"+Hex.toHexString(key.getPubKey()));
//        System.out.println("chainCode:"+hexCoder.encode(key.getChainCode()));
//        System.out.println("path:"+key.getPath());
//        System.out.println("childNumber:"+key.getChildNumber());
//
//
//
//        String serializePrivB58 = key.serializePrivB58(MainNetParams.get());
//        DeterministicKey deterministicKey = DeterministicKey.deserializeB58(serializePrivB58, MainNetParams.get());
//        deterministicKey = new DeterministicKey(HDUtils.append(HDUtils.parsePath("M/44H/60H/0H"), ChildNumber.ZERO), deterministicKey.getChainCode(), deterministicKey.getPrivKey(), deterministicKey);
//
//        deterministicKey = HDKeyDerivation.deriveChildKey(deterministicKey, new ChildNumber(0, false));
//        System.out.println("deterministicKey2:"+deterministicKey.getPath());
//        System.out.println("childDeterministicKey:"+deterministicKey.getPath());
//        // Web3j
//        Credentials credentials6 = Credentials.create(deterministicKey.getPrivKey().toString(16));
//        System.out.println(credentials6.getAddress());

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

    @Test
    public void testSendETHError() throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(
                        "1hjdsR4"
                        ,"/keystore/2B9824D5822240AB8C1ED88B38012CD0/m/44'/60'/0'/0/0/UTC--2018-07-23T02-29-30.381000000Z--f49b8bc27067935ed2852691f78a6cc37d24a819.json");

        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);


        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j, credentials, "0x113227618ad8226df25e41cfa751e4096068388e",
                new BigDecimal("1"), Convert.Unit.ETHER).send();

        System.out.println(transactionReceipt.getTransactionHash());
    }

}
