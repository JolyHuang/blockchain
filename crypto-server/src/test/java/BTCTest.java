import org.bitcoinj.core.*;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;

import java.io.File;

/**
 * BTCTest
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/30 下午12:30
 */
public class BTCTest {

    @Test
    public void test() throws BlockStoreException {
        NetworkParameters params  = TestNet3Params.get();

        ECKey key = new ECKey();
        System.out.println("We created a new key:\n" + key);

        Address addressFromKey = key.toAddress(params);
        System.out.println("Public Address generated: " + addressFromKey);

        System.out.println("Private key is: " + key.getPrivateKeyEncoded(params).toString());

        Wallet wallet = new Wallet(params);
        wallet.importKey(key);

        File blockFile = new File("/tmp/bitcoin-blocks");
        SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);

        BlockChain blockChain = new BlockChain(params, wallet, blockStore);
        PeerGroup peerGroup = new PeerGroup(params, blockChain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.addWallet(wallet);

        System.out.println("Start peer group");
        peerGroup.start();

        System.out.println("Downloading block chain");
        peerGroup.downloadBlockChain();
        System.out.println("Block chain downloaded");
    }

}
