package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterRsp;
import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
	@Resource
	public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
		this.addressNoticeService = addressNoticeService;
	}

	@Override
	@Transactional
	public RegisterRsp register(RegisterReq req) {

		RegisterRsp rsp = new RegisterRsp();

		// 如果记录存在，直接返回成功
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister.setSubCoinType(req.getSubCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);
		if(queryAddressRegister != null && queryAddressRegister.getId() != null) {
			return rsp;
		}

		AddressRegister addressRegister = new AddressRegister();
		addressRegister.setAddress(req.getAddress());
		addressRegister.setCoinType(req.getCoinType());
		addressRegister.setSubCoinType(req.getSubCoinType());
		addressRegister.setContractAddress(req.getContractAddress());

		// 注册地址
		addressRegisterDAO.insert(addressRegister);

		// 注册通知地址
		if(StringUtils.isTrimEmpty(req.getSubCoinType())) {
			addressNoticeService.registerDepositAddressNotice(addressRegister.getId(), req.getNoticeAddress(), addressRegister.getAddress(), addressRegister.getCoinType());
		}

		return rsp;
	}

	@Override
	public UnregisterRsp unregister(UnregisterReq req) {
		UnregisterRsp rsp = new UnregisterRsp();

		// 如果记录不存在，直接返回成功
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);
		if(queryAddressRegister == null || queryAddressRegister.getId() == null) {
			return rsp;
		}

		// 解除地址注册
		addressRegisterDAO.deleteById(queryAddressRegister.getId());

		return rsp;
	}

	@Override
	public ETHAddressRegister getETHAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_ETH);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		return new ETHAddressRegister(addressRegisterList);
	}
}
