package com.sharingif.blockchain.transaction.controller;


import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


@Controller
@RequestMapping(value="transactionEth")
public class TransactionEthController {
	
	private TransactionEthService transactionEthService;

	public TransactionEthService getTransactionEthService() {
		return transactionEthService;
	}
	@Resource
	public void setTransactionEthService(TransactionEthService transactionEthService) {
		this.transactionEthService = transactionEthService;
	}
	
}
