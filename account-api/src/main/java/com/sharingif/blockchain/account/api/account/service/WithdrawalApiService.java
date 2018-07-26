package com.sharingif.blockchain.account.api.account.service;

import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

/**
 * 提现服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午3:42
 */
@RequestMapping(value="withdrawal")
public interface WithdrawalApiService {

    /**
     * 取现申请
     * @param req
     */
    @RequestMapping(value="apply", method= RequestMethod.POST)
    void apply(WithdrawalApplyReq req);

}
