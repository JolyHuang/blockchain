package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;

import java.util.Arrays;
import java.util.List;


@Repository
public class TransactionEthDAOImpl extends BaseDAOImpl<TransactionEth,String> implements TransactionEthDAO {

    @Override
    public PaginationRepertory<TransactionEth> queryPaginationTxStatusInList(PaginationCondition<TransactionEth> paginationCondition) {

        return queryPagination("queryPaginationTxStatusInList", paginationCondition);
    }
}
