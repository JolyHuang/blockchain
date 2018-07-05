package com.sharingif.blockchain.crypto.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.crypto.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.model.entity.ExtendedKey;
import org.springframework.stereotype.Repository;


@Repository
public class ExtendedKeyDAOImpl extends BaseDAOImpl<ExtendedKey,String> implements ExtendedKeyDAO {
	
}
