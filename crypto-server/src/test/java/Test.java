import com.sharingif.bips.bip0032.Mnemonic;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

/**
 * TODO
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/11 上午12:22
 */
public class Test {

    @org.junit.Test
    public void testPrivateKey() throws UnreadableWalletException {
        Mnemonic mnemonic = new Mnemonic(Locale.US, 12);
        // BitcoinJ
        DeterministicSeed seed = new DeterministicSeed(mnemonic.getMnemonic(), null, "", 0l);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/1");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

        System.out.println(privKey);

        Credentials credentials = Credentials.create(ECKeyPair.create(privKey));
        System.out.println(credentials.getAddress());


    }

}
