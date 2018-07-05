package com.sharingif.blockchain.crypto.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.crypto.dao.MnemonicDAO;
import com.sharingif.blockchain.crypto.model.entity.Mnemonic;
import org.springframework.stereotype.Repository;




@Repository
public class MnemonicDAOImpl extends BaseDAOImpl<Mnemonic,String> implements MnemonicDAO {
	
}
