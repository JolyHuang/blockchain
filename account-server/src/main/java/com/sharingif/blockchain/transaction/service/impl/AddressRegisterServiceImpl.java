package com.sharingif.blockchain.transaction.service.impl;


import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterRsp;
import com.sharingif.blockchain.account.batch.api.transaction.service.AddressRegisterApiService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.dao.AddressRegisterDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressRegisterServiceImpl extends BaseServiceImpl<AddressRegister, String> implements AddressRegisterService {
	
	private AddressRegisterDAO addressRegisterDAO;
	private AddressNoticeService addressNoticeService;
	private BtcdClient btcdClient;
	private com.sharingif.blockchain.account.batch.api.transaction.service.AddressRegisterApiService addressRegisterApiService;

	public AddressRegisterDAO getAddressRegisterDAO() {
		return addressRegisterDAO;
	}
	@Resource
	public void setAddressRegisterDAO(AddressRegisterDAO addressRegisterDAO) {
		super.setBaseDAO(addressRegisterDAO);
		this.addressRegisterDAO = addressRegisterDAO;
	}
	@Resource
	public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
		this.addressNoticeService = addressNoticeService;
	}
	@Resource
	public void setBtcdClient(BtcdClient btcdClient) {
		this.btcdClient = btcdClient;
	}
	@Resource
	public void setAddressRegisterApiService(AddressRegisterApiService addressRegisterApiService) {
		this.addressRegisterApiService = addressRegisterApiService;
	}

	@Override
	@Transactional
	public RegisterRsp register(RegisterReq req) {

		RegisterRsp rsp = new RegisterRsp();

		// 如果记录存在，直接返回成功
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister.setSubCoinType(req.getSubCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);
		if(queryAddressRegister != null && queryAddressRegister.getId() != null) {
			return rsp;
		}

		AddressRegister addressRegister = new AddressRegister();
		addressRegister.setAddress(req.getAddress());
		addressRegister.setCoinType(req.getCoinType());
		addressRegister.setSubCoinType(req.getSubCoinType());
		addressRegister.setContractAddress(req.getContractAddress());

		// 注册地址
		addressRegisterDAO.insert(addressRegister);

		// 注册通知地址
		if(StringUtils.isTrimEmpty(req.getSubCoinType()) && !(StringUtils.isTrimEmpty(req.getNoticeAddress()))) {
			addressNoticeService.registerDepositAddressNotice(addressRegister.getId(), req.getNoticeAddress(), addressRegister.getAddress(), addressRegister.getCoinType());
		}

		// 如果币种是BTC，地址导入BTC钱包
		if(CoinType.BTC.name().equals(req.getCoinType())) {
			try {
				btcdClient.importAddress(req.getAddress(), "OLIVE", false);
			} catch (Exception e) {
				logger.error("import address error", e);
				throw new CubeRuntimeException(e);
			}
		}

		// 通知batch服务更新地址
		com.sharingif.blockchain.account.batch.api.transaction.entity.RegisterReq batchRegisterReq = new com.sharingif.blockchain.account.batch.api.transaction.entity.RegisterReq();
		batchRegisterReq.setAddress(req.getAddress());
		batchRegisterReq.setCoinType(req.getCoinType());
		batchRegisterReq.setSubCoinType(req.getSubCoinType());
		batchRegisterReq.setContractAddress(req.getContractAddress());
		addressRegisterApiService.register(batchRegisterReq);

		return rsp;
	}

	@Override
	public UnregisterRsp unregister(UnregisterReq req) {
		UnregisterRsp rsp = new UnregisterRsp();

		// 如果记录不存在，直接返回成功
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setAddress(req.getAddress());
		queryAddressRegister.setCoinType(req.getCoinType());
		queryAddressRegister= addressRegisterDAO.query(queryAddressRegister);
		if(queryAddressRegister == null || queryAddressRegister.getId() == null) {
			return rsp;
		}

		// 解除地址注册
		addressRegisterDAO.deleteById(queryAddressRegister.getId());

		return rsp;
	}

	@Override
	public ETHAddressRegister getETHAddressRegister() {
		AddressRegister queryAddressRegister = new AddressRegister();
		queryAddressRegister.setCoinType(AddressRegister.COIN_TYPE_ETH);

		List<AddressRegister> addressRegisterList = addressRegisterDAO.queryList(queryAddressRegister);

		return new ETHAddressRegister(addressRegisterList);
	}
}
