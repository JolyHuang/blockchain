package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressRegisterServiceImpl extends BaseServiceImpl<AddressRegister, String> implements AddressRegisterService {
	
	private AddressRegisterDAO addressRegisterDAO;
	private AddressNoticeService addressNoticeService;

	public AddressRegisterDAO getAddressRegisterDAO() {
		return addressRegisterDAO;
	}
	@Resource
	public void setAddressRegisterDAO(AddressRegisterDAO addressRegisterDAO) {
		super.setBaseDAO(addressRegisterDAO);
		this.addressRegisterDAO = addressRegisterDAO;
	}

	@Override
	public ETHAddressRegister getETHAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_ETH);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		return new ETHAddressRegister(addressRegisterList);
	}

	@Override
	public Map<String, String> getBTCAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_BTC);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		if(addressRegisterList == null) {
			return new HashMap<String, String>();
		}

		Map<String, String> map = new HashMap<String, String>();
		for(AddressRegister addressRegister : addressRegisterList) {
			map.put(addressRegister.getAddress(), addressRegister.getCoinType());
		}

		return map;
	}
}
