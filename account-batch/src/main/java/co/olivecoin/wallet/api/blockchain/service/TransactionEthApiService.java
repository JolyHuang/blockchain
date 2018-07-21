package co.olivecoin.wallet.api.blockchain.service;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthApiReq;
import com.sharingif.blockchain.app.components.UrlBody;

/**
 * eth 交易
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午1:57
 */
public interface TransactionEthApiService {

    /**
     * eth交易
     * @param req
     */
    void eth(UrlBody<TransactionEthApiReq> req);

}
