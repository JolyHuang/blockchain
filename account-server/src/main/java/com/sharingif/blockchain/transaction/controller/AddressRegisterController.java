package com.sharingif.blockchain.transaction.controller;


import com.sharingif.blockchain.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


@Controller
@RequestMapping(value="address")
public class AddressRegisterController {
	
	private AddressRegisterService addressRegisterService;

	public AddressRegisterService getAddressRegisterService() {
		return addressRegisterService;
	}
	@Resource
	public void setAddressRegisterService(AddressRegisterService addressRegisterService) {
		this.addressRegisterService = addressRegisterService;
	}

	/**
	 * 注册地址
	 * @param req
	 * @return
	 */
	@RequestMapping(value="register", method= RequestMethod.POST)
	public void register(RegisterReq req) {

		addressRegisterService.register(req);
	}

	/**
	 * 解除注册地址
	 * @param req
	 * @return
	 */
	@RequestMapping(value="unregister", method= RequestMethod.POST)
	public void unregister(UnregisterReq req) {

		addressRegisterService.unregister(req);
	}


}
