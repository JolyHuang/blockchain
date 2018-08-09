package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;
import com.sharingif.blockchain.transaction.dao.TransactionBtcUtxoDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Repository;


@Repository
public class TransactionBtcUtxoDAOImpl extends BaseDAOImpl<TransactionBtcUtxo,java.lang.String> implements TransactionBtcUtxoDAO {

    @Override
    public PaginationRepertory<TransactionBtcUtxo> queryPaginationTxStatusInList(PaginationCondition<TransactionBtcUtxo> paginationCondition) {

        return queryPagination("queryPaginationTxStatusInList", paginationCondition);
    }

}
