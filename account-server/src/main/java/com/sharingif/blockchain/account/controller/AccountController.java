package com.sharingif.blockchain.account.controller;


import javax.annotation.Resource;

import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.sharingif.blockchain.account.service.AccountService;


@Controller
@RequestMapping(value="account")
public class AccountController {
	
	private AccountService accountService;

	public AccountService getAccountService() {
		return accountService;
	}
	@Resource
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
