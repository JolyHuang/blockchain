package com.sharingif.blockchain.crypto.key.dao.impl;


import com.sharingif.blockchain.crypto.app.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.crypto.key.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.key.model.entity.SecretKey;
import org.springframework.stereotype.Repository;

@Repository
public class SecretKeyDAOImpl extends BaseDAOImpl<SecretKey,String> implements SecretKeyDAO {
	
}
