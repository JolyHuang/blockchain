package com.sharingif.blockchain.account.controller;


import com.sharingif.blockchain.account.service.AccountJnlService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


@Controller
@RequestMapping(value="accountJnl")
public class AccountJnlController {
	
	private AccountJnlService accountJnlService;

	public AccountJnlService getAccountJnlService() {
		return accountJnlService;
	}
	@Resource
	public void setAccountJnlService(AccountJnlService accountJnlService) {
		this.accountJnlService = accountJnlService;
	}
	
}
