package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;


@Repository
public class AddressRegisterDAOImpl extends BaseDAOImpl<AddressRegister,String> implements AddressRegisterDAO {
	
}
