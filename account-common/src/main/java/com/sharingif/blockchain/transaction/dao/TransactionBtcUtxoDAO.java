package com.sharingif.blockchain.transaction.dao;


import com.sharingif.blockchain.common.dao.BaseDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;


public interface TransactionBtcUtxoDAO extends BaseDAO<TransactionBtcUtxo,java.lang.String> {

    PaginationRepertory<TransactionBtcUtxo> queryPaginationTxStatusInList(PaginationCondition<TransactionBtcUtxo> paginationCondition);

}
