package com.sharingif.blockchain.common.dao.impl;


import com.sharingif.cube.components.sequence.ISequenceHandler;
import com.sharingif.cube.persistence.database.pagination.IPaginationHandler;
import com.sharingif.cube.persistence.mybatis.dao.CubeMyBatisDAOImpl;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.io.Serializable;

public class BaseDAOImpl<T, ID extends Serializable> extends CubeMyBatisDAOImpl<T, ID> {

    @Override
    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    @Resource
    public void setPaginationHandler(IPaginationHandler mySqlPaginationHandler) {
        super.setPaginationHandler(mySqlPaginationHandler);
    }

    @Override
    @Resource
    public void setSequenceHandler(ISequenceHandler sequenceHandler) {
        super.setSequenceHandler(sequenceHandler);
    }


}
