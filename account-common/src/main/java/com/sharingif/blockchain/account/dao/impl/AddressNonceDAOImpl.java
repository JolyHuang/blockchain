package com.sharingif.blockchain.account.dao.impl;


import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.account.model.entity.AddressNonce;
import com.sharingif.blockchain.account.dao.AddressNonceDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;


@Repository
public class AddressNonceDAOImpl extends BaseDAOImpl<AddressNonce,java.lang.String> implements AddressNonceDAO {
	
}
