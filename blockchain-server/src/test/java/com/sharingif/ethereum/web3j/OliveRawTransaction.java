package com.sharingif.ethereum.web3j;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * OliveRawTransaction
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/5 下午8:40
 */
public class OliveRawTransaction {

    static String contractAddress = "0x9d9223436ddd466fc247e9dbbd20207e640fef58";
    static String fromAddress = "0x5b6695966503a4618b3cc8D1Ed7768BFb3757750";
    static String toAddress = "0x5b6695966503a4618b3cc8D1Ed7768BFb3757750";

    static Credentials credentials = null;

    static {
        try {
            credentials = WalletUtils.loadCredentials(
                    "11111111"
                    ,"/Users/Joly/Library/Ethereum/keystore/UTC--2018-03-16T11-34-59.694949000Z--5b6695966503a4618b3cc8d1ed7768bfb3757750");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        List<String> hostList = new ArrayList<String>();
//        hostList.add("185.244.192.252");
        hostList.add("52.166.62.158");
        hostList.add("185.21.100.42");
        hostList.add("52.77.202.112");
        hostList.add("110.173.57.186");
//        hostList.add("159.203.171.169");
        hostList.add("110.173.57.187");


        for(String host : hostList) {
            transaction(host, 8545);
        }

    }

    private static void transaction(String host, int port) throws ExecutionException, InterruptedException, IOException {
        Web3j web3j = Web3j.build(new HttpService("http://"+host+":"+port));
        BigInteger amount = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(toAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount().add(new BigInteger("1"));

        Function function = new Function(
                "transfer"
                ,Arrays.asList(new Address(toAddress), new Uint256(amount))
                ,new ArrayList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction  = RawTransaction.createTransaction(
                nonce
                ,Convert.toWei("0.000000008", Convert.Unit.ETHER).toBigInteger()
                ,new BigInteger("43132")
                ,contractAddress
                ,encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        String transactionHash = web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();
        System.out.println(transactionHash);
    }

}
