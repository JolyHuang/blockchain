package com.sharingif.blockchain.crypto.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.dao.SecretKeyDAO;


@Repository
public class SecretKeyDAOImpl extends BaseDAOImpl<SecretKey,String> implements SecretKeyDAO {
	
}
