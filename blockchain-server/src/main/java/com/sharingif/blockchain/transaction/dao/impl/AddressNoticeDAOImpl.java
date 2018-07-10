package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.dao.AddressNoticeDAO;


@Repository
public class AddressNoticeDAOImpl extends BaseDAOImpl<AddressNotice,String> implements AddressNoticeDAO {
	
}
