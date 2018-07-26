package com.sharingif.blockchain.account.controller;

import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 取现
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午2:21
 */
@Controller
@RequestMapping(value="withdrawal")
public class WithdrawalController {

    private WithdrawalService withdrawalService;

    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    /**
     * 取现申请
     * @param req
     */
    @RequestMapping(value="apply", method= RequestMethod.POST)
    public void apply(WithdrawalApplyReq req) {
        withdrawalService.apply(req);
    }

}
