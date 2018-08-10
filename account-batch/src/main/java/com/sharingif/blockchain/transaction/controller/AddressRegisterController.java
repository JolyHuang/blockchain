package com.sharingif.blockchain.transaction.controller;

import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * AddressRegisterController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/10 下午12:19
 */
@Controller
@RequestMapping(value="address")
public class AddressRegisterController {

    private AddressRegisterService addressRegisterService;

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

}
