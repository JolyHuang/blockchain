package com.sharingif.blockchain.transaction.controller;


import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


@Controller
@RequestMapping(value="addressNotice")
public class AddressNoticeController {
	
	private AddressNoticeService addressNoticeService;

	public AddressNoticeService getAddressNoticeService() {
		return addressNoticeService;
	}
	@Resource
	public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
		this.addressNoticeService = addressNoticeService;
	}
	
}
