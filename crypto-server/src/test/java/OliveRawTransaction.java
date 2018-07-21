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
import java.util.concurrent.ExecutionException;

/**
 * OliveRawTransaction
 *
 * @author Joly
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/5 下午8:40
 */
public class OliveRawTransaction {

    static String contractAddress = "";
    static String methodName = "";
    static String toAddress = "";

    static Credentials credentials = null;

    static {
        try {
            credentials = WalletUtils.loadCredentials(
                    ""
                    ,"");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        transaction("localhost", 8545);

//        Web3j web3j = Web3j.build(new HttpService("http://47.52.42.78:8545"));

//        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

    }

    private static void transaction(String host, int port) throws ExecutionException, InterruptedException, IOException {
        Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
        BigInteger amount = Convert.toWei("400000", Convert.Unit.ETHER).toBigInteger();

        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(toAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount().add(new BigInteger("14"));

        Function function = new Function(
                methodName
                ,Arrays.asList(new Address(toAddress), new Uint256(amount))
                ,new ArrayList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction  = RawTransaction.createTransaction(
                nonce
                ,Convert.toWei("0.000000012", Convert.Unit.ETHER).toBigInteger()
                ,new BigInteger("83132")
                ,contractAddress
                ,encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        String transactionHash = web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();
        System.out.println(transactionHash);
    }

}
