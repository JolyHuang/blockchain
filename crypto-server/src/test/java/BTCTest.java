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

//    public static void main(String[] args) throws Exception {
//        String seedCode = "dog kiwi bus broken merge patient possible version cliff announce tobacco film";
//
//        DeterministicSeed seed = new DeterministicSeed(seedCode, null, "", 0l);
//        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
//        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/0H/0H/0/0");
//        DeterministicKey key = chain.getKeyByPath(keyPath, true);
//
//        System.out.println(key.getPrivateKeyAsWiF(TestNet3Params.get()));
//
//        BtcdClient btcdProvider = ResourceUtils.getBtcdProvider();
//        List<Output> outputList = btcdProvider.listUnspent(6,9999999, Arrays.asList("n2uYLrTPtkteuuBwLNWzM5BydsUH83Vro5"));
//        for(Output a : outputList) {
//            System.out.println(a);
//        }
//
//        Output output = outputList.get(0);
//        System.out.println(output);
//
//        RawTransaction rawTransaction = (RawTransaction)btcdProvider.getRawTransaction(output.getTxId(), true, "");
//
//        TestNet3Params params = TestNet3Params.get();
//        Transaction tx = new Transaction(params);
//        tx.addOutput(Coin.COIN, key.toAddress(params));
//        keyPath = HDUtils.parsePath("M/44H/0H/0H/0/3");
//        DeterministicKey n3H5BVJV8WnsjJo41MgEsTatQc4rA9qpmF = chain.getKeyByPath(keyPath, true);
//        tx.addOutput(Coin.COIN.add(Coin.COIN).div(10), n3H5BVJV8WnsjJo41MgEsTatQc4rA9qpmF.toAddress(params));
//
//
//
//        TransactionOutPoint outPoint = new TransactionOutPoint(params, new Long(output.getVOut()), Sha256Hash.wrap(output.getTxId()));
////        tx.addSignedInput(outPoint, new Script(rawTransaction.getVOut().get(1).get), key, Transaction.SigHash.ALL, true);
//        tx.getConfidence().setSource(TransactionConfidence.Source.SELF);
//        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
//
//        System.out.println(tx.getHashAsString());
//
//    }

}
