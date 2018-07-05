package com.sharingif.blockchain.crypto.mnemonic.controller;

import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateReq;
import com.sharingif.blockchain.crypto.api.mnemonic.entity.MnemonicGenerateRsp;
import com.sharingif.blockchain.crypto.mnemonic.service.MnemonicService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * MnemonicController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/2 下午4:22
 */
@Controller
@RequestMapping(value="mnemonic")
public class MnemonicController {

    private MnemonicService mnemonicService;

    @Resource
    public void setMnemonicService(MnemonicService mnemonicService) {
        this.mnemonicService = mnemonicService;
    }

    /**
     * 生成助记词
     * @return
     */
    @RequestMapping(value="generate", method= RequestMethod.POST)
    public MnemonicGenerateRsp generate(MnemonicGenerateReq req) {

        return mnemonicService.generate(req);
    }

}
