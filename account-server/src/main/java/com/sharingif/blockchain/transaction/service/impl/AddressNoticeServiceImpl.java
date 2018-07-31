package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.AddressNoticeDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.cube.core.util.StringUtils;
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
		AddressNotice queryAddressNotice = new AddressNotice();
		queryAddressNotice.setAddress(address);
		queryAddressNotice.setCoinType(coinType);
		queryAddressNotice.setNoticeType(AddressNotice.NOTICE_TYPE_DEPOSIT);
		queryAddressNotice = addressNoticeDAO.query(queryAddressNotice);

		if(queryAddressNotice == null) {
			AddressNotice insertAddressNotice = new AddressNotice();
			insertAddressNotice.setAddressRegisterId(addressRegisterId);
			insertAddressNotice.setNoticeAddress(noticeAddress);
			insertAddressNotice.setAddress(address);
			insertAddressNotice.setCoinType(coinType);
			insertAddressNotice.setNoticeType(AddressNotice.NOTICE_TYPE_DEPOSIT);

			addressNoticeDAO.insert(insertAddressNotice);
		} else {
			AddressNotice updateAddressNotice = new AddressNotice();
			updateAddressNotice.setId(queryAddressNotice.getId());
			updateAddressNotice.setNoticeAddress(noticeAddress);
			updateAddressNotice.setAddressRegisterId(addressRegisterId);

			addressNoticeDAO.updateById(updateAddressNotice);
		}
	}

	@Override
	public void registerWithdrawalAddressNotice(String addressRegisterId, String noticeAddress, String address, String coinType) {
		AddressNotice queryAddressNotice = new AddressNotice();
		queryAddressNotice.setAddress(address);
		queryAddressNotice.setCoinType(coinType);
		queryAddressNotice.setNoticeType(AddressNotice.NOTICE_TYPE_WITHDRAWAL);
		queryAddressNotice = addressNoticeDAO.query(queryAddressNotice);

		if(queryAddressNotice == null) {
			AddressNotice insertAddressNotice = new AddressNotice();
			insertAddressNotice.setAddressRegisterId(addressRegisterId);
			insertAddressNotice.setNoticeAddress(noticeAddress);
			insertAddressNotice.setAddress(address);
			insertAddressNotice.setCoinType(coinType);
			insertAddressNotice.setNoticeType(AddressNotice.NOTICE_TYPE_WITHDRAWAL);

			addressNoticeDAO.insert(insertAddressNotice);
		} else {
			AddressNotice updateAddressNotice = new AddressNotice();
			updateAddressNotice.setId(queryAddressNotice.getId());
			updateAddressNotice.setNoticeAddress(noticeAddress);
			updateAddressNotice.setAddressRegisterId(addressRegisterId);

			addressNoticeDAO.updateById(updateAddressNotice);
		}

	}

	@Override
	public void unregisterAddressNotice(String addressRegisterId) {
		AddressNotice addressNotice = new AddressNotice();
		addressNotice.setAddressRegisterId(addressRegisterId);

		addressNoticeDAO.deleteByCondition(addressNotice);
	}
}
