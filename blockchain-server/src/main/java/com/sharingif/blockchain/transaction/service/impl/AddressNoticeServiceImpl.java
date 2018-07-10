package com.sharingif.blockchain.transaction.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.dao.AddressNoticeDAO;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;

import java.util.List;

@Service
public class AddressNoticeServiceImpl extends BaseServiceImpl<AddressNotice, java.lang.String> implements AddressNoticeService {
	
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
	public void registerAddressNotice(String addressRegisterId, List<String> noticeList) {
		for(String notice : noticeList) {
			AddressNotice addressNotice = new AddressNotice();
			addressNotice.setAddressRegisterId(addressRegisterId);
			addressNotice.setNoticeAddress(notice);

			addressNoticeDAO.insert(addressNotice);
		}
	}

	@Override
	public void unregisterAddressNotice(String addressRegisterId) {
		AddressNotice addressNotice = new AddressNotice();
		addressNotice.setAddressRegisterId(addressRegisterId);

		addressNoticeDAO.deleteByCondition(addressNotice);
	}
}
