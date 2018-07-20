package com.sharingif.blockchain.account.controller;


import com.sharingif.blockchain.account.api.account.entity.AccountSysSetChangeExtendedKeyReq;
import com.sharingif.blockchain.account.api.account.entity.AccountSysSetChangeExtendedKeyRsp;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeRsp;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


@Controller
@RequestMapping(value="accountSys")
public class AccountSysPrmController {

	private AccountSysPrmService accountSysPrmService;

	@Resource
	public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
		this.accountSysPrmService = accountSysPrmService;
	}

	/**
	 * 设置当前change ExtendedKey
	 * @return
	 */
	@RequestMapping(value="setChangeExtendedKey", method= RequestMethod.POST)
	public void setChangeExtendedKey(AccountSysSetChangeExtendedKeyReq req) {
		accountSysPrmService.setChangeExtendedKey(req);
	}

}
