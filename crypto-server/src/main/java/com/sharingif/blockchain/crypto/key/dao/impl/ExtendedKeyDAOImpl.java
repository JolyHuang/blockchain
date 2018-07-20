package com.sharingif.blockchain.crypto.key.dao.impl;


import com.sharingif.blockchain.crypto.app.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.crypto.key.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.key.model.entity.ExtendedKey;
import org.springframework.stereotype.Repository;

@Repository
public class ExtendedKeyDAOImpl extends BaseDAOImpl<ExtendedKey,String> implements ExtendedKeyDAO {
	
}
