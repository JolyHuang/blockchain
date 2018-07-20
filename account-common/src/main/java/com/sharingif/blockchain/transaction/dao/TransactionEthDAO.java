package com.sharingif.blockchain.transaction.dao;


import com.sharingif.blockchain.common.dao.BaseDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

import java.util.List;


public interface TransactionEthDAO extends BaseDAO<TransactionEth,String> {

    PaginationRepertory<TransactionEth> queryPaginationTxStatusInList(PaginationCondition<TransactionEth> paginationCondition);

}
