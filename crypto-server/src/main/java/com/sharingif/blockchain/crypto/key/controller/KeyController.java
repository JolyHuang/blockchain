package com.sharingif.blockchain.crypto.key.controller;

import com.sharingif.blockchain.crypto.api.key.KeyGenerateReq;
import com.sharingif.blockchain.crypto.api.key.KeyGenerateRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * KeyController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:37
 */
@Controller
@RequestMapping(value="key")
public class KeyController {

    /**
     * 生成key
     * @return
     */
    @RequestMapping(value="bip44", method= RequestMethod.POST)
    public KeyGenerateRsp bip44(KeyGenerateReq req) {

        return null;
    }

}
