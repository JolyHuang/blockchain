package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.AddressNoticeDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressNoticeServiceImpl extends BaseServiceImpl<AddressNotice, String> implements AddressNoticeService {
	
	private AddressNoticeDAO addressNoticeDAO;

	public AddressNoticeDAO getAddressNoticeDAO() {
		return addressNoticeDAO;
	}
	@Resource
	public void setAddressNoticeDAO(AddressNoticeDAO addressNoticeDAO) {
		super.setBaseDAO(addressNoticeDAO);
		this.addressNoticeDAO = addressNoticeDAO;
	}

	@Override
	public void registerDepositAddressNotice(String addressRegisterId, String noticeAddress, String address, String coinType) {
		AddressNotice addressNotice = new AddressNotice();
		addressNotice.setAddressRegisterId(addressRegisterId);
		addressNotice.setNoticeAddress(noticeAddress);
		addressNotice.setAddress(address);
		addressNotice.setCoinType(coinType);
		addressNotice.setNoticeType(AddressNotice.NOTICE_TYPE_DEPOSIT);

		addressNoticeDAO.insert(addressNotice);
	}

	@Override
	public void unregisterAddressNotice(String addressRegisterId) {
		AddressNotice addressNotice = new AddressNotice();
		addressNotice.setAddressRegisterId(addressRegisterId);

		addressNoticeDAO.deleteByCondition(addressNotice);
	}
}
