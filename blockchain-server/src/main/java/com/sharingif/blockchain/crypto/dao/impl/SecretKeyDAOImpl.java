package com.sharingif.blockchain.crypto.dao.impl;


import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.dao.SecretKeyDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;


@Repository
public class SecretKeyDAOImpl extends BaseDAOImpl<SecretKey,java.lang.String> implements SecretKeyDAO {
	
}
