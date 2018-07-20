package com.sharingif.blockchain.crypto.service.impl;

import com.sharingif.blockchain.crypto.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.model.entity.ExtendedKey;
import com.sharingif.blockchain.crypto.service.ExtendedKeyService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ExtendedKeyServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/19 下午4:14
 */
@Service
public class ExtendedKeyServiceImpl extends BaseServiceImpl<ExtendedKey, String> implements ExtendedKeyService {

    private ExtendedKeyDAO extendedKeyDAO;

    public ExtendedKeyDAO getExtendedKeyDAO() {
        return extendedKeyDAO;
    }

    @Resource
    public void setExtendedKeyDAO(ExtendedKeyDAO extendedKeyDAO) {
        super.setBaseDAO(extendedKeyDAO);
        this.extendedKeyDAO = extendedKeyDAO;
    }
}
