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

    static String contractAddress = "0xe49a2Faa1081C177721e2F579b55371Fea5b3019";
    static String methodName = "transfer";
    static String toAddress = "0x55018a1fb087ab3b7383ba31f569d68c5e9fc399";
    static String fromAddress = "0xf49b8bC27067935eD2852691F78a6Cc37D24A819";

    static Credentials credentials = null;

    static {
        try {
            credentials = Credentials.create("96997592b950b811ab843861cbb59df22f66decef9f79b7028b3bf09ec3e08e9");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        transaction();
    }

    private static void transaction() throws ExecutionException, InterruptedException, IOException {
        Web3j web3j = Web3j.build(new HttpService("http://47.88.156.133:7236"));
        BigInteger amount = Convert.toWei("100", Convert.Unit.ETHER).toBigInteger();

        System.out.println(web3j.ethBlockNumber().send().getBlockNumber());

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        Function function = new Function(
                methodName
                ,Arrays.asList(new Address(toAddress), new Uint256(amount))
                ,new ArrayList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction  = RawTransaction.createTransaction(
                nonce
                ,Convert.toWei("0.000000012", Convert.Unit.ETHER).toBigInteger()
                ,new BigInteger("1000000")
                ,contractAddress
                ,encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        String transactionHash = web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();
        System.out.println(transactionHash);
    }

}
