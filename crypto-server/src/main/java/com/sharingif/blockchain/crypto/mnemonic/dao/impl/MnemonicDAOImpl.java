package com.sharingif.blockchain.crypto.mnemonic.dao.impl;


import com.sharingif.blockchain.crypto.app.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.crypto.mnemonic.entity.Mnemonic;
import com.sharingif.blockchain.crypto.mnemonic.dao.MnemonicDAO;
import org.springframework.stereotype.Repository;


@Repository
public class MnemonicDAOImpl extends BaseDAOImpl<Mnemonic,String> implements MnemonicDAO {
	
}
