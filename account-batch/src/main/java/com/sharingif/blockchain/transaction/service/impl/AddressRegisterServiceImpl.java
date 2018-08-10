package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressRegisterServiceImpl extends BaseServiceImpl<AddressRegister, String> implements AddressRegisterService, InitializingBean {

	private ETHAddressRegister ethAddressRegister;
	private Map<String, String> btcAddressRegister;
	
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
	public void register(RegisterReq req) {
		if(CoinType.BTC.name().equals(req.getCoinType())) {
			btcAddressRegister.put(req.getAddress(), req.getCoinType());
		}else {
			AddressRegister addressRegister = new AddressRegister();
			addressRegister.setAddress(req.getAddress());
			addressRegister.setContractAddress(req.getContractAddress());
			addressRegister.setCoinType(req.getCoinType());
			addressRegister.setSubCoinType(req.getSubCoinType());
			ethAddressRegister.addAddressRegister(addressRegister);
		}
	}

	@Override
	public ETHAddressRegister getETHAddressRegister() {
		return ethAddressRegister;
	}

	@Override
	public Map<String, String> getBTCAddressRegister() {
		return btcAddressRegister;
	}

	protected void initETHAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_ETH);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		ethAddressRegister = new ETHAddressRegister(addressRegisterList);
	}

	protected void initBTCAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_BTC);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		if(addressRegisterList == null) {
			btcAddressRegister = new HashMap<String, String>();
			return;
		}

		btcAddressRegister = new HashMap<String, String>();
		for(AddressRegister addressRegister : addressRegisterList) {
			btcAddressRegister.put(addressRegister.getAddress(), addressRegister.getCoinType());
		}


	}

	protected void initAddressRegister() {
		initETHAddressRegister();

		initBTCAddressRegister();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initAddressRegister();
	}
}
