package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.api.transaction.entity.UnregisterRsp;
import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddressRegisterServiceImpl extends BaseServiceImpl<AddressRegister, java.lang.String> implements AddressRegisterService {
	
	private AddressRegisterDAO addressRegisterDAO;

	public AddressRegisterDAO getAddressRegisterDAO() {
		return addressRegisterDAO;
	}
	@Resource
	public void setAddressRegisterDAO(AddressRegisterDAO addressRegisterDAO) {
		super.setBaseDAO(addressRegisterDAO);
		this.addressRegisterDAO = addressRegisterDAO;
	}

	@Override
	public RegisterRsp register(RegisterReq req) {

		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);

		RegisterRsp rsp = new RegisterRsp();

		if(queryAddressRegister != null && queryAddressRegister.getId() != null) {
			return rsp;
		}

		AddressRegister addressRegister = new AddressRegister();
		addressRegister.setAddress(req.getAddress());
		addressRegister.setCoinType(req.getCoinType());

		addressRegisterDAO.insert(addressRegister);

		return rsp;
	}

	@Override
	public UnregisterRsp unregister(UnregisterReq req) {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);

		UnregisterRsp rsp = new UnregisterRsp();

		if(queryAddressRegister == null || queryAddressRegister.getId() == null) {
			return rsp;
		}

		addressRegisterDAO.deleteById(queryAddressRegister.getId());

		return rsp;
	}
}
