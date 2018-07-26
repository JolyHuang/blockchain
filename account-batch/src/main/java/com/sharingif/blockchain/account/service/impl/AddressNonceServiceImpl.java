package com.sharingif.blockchain.account.service.impl;

import com.sharingif.blockchain.account.dao.AddressNonceDAO;
import com.sharingif.blockchain.account.model.entity.AddressNonce;
import com.sharingif.blockchain.account.service.AddressNonceService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * AddressNonceServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午2:01
 */
@Service
public class AddressNonceServiceImpl extends BaseServiceImpl<AddressNonce, String> implements AddressNonceService {

    private AddressNonceDAO addressNonceDAO;

    @Resource
    public void setAddressNonceDAO(AddressNonceDAO addressNonceDAO) {
        super.setBaseDAO(addressNonceDAO);
        this.addressNonceDAO = addressNonceDAO;
    }

    @Override
    synchronized public BigInteger getNonce(String address) {
        AddressNonce queryAddressNonce = addressNonceDAO.queryById(address);

        if(queryAddressNonce == null) {
            queryAddressNonce = new AddressNonce();
            queryAddressNonce.setAddress(address);
            queryAddressNonce.setNonce(BigInteger.ZERO);
            addressNonceDAO.insert(queryAddressNonce);

            return queryAddressNonce.getNonce();
        }

        AddressNonce updateAddressNonce = new AddressNonce();
        updateAddressNonce.setAddress(address);
        updateAddressNonce.setNonce(queryAddressNonce.getNonce().add(new BigInteger("1")));
        addressNonceDAO.updateById(updateAddressNonce);

        return updateAddressNonce.getNonce();
    }

}
