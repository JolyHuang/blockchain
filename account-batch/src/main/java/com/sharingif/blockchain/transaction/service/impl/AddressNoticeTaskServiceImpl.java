package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.dao.AddressNoticeTaskDAO;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.AddressNoticeTask;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressNoticeTaskService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AddressNoticeTaskServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午2:07
 */
@Service
public class AddressNoticeTaskServiceImpl extends BaseServiceImpl<AddressNoticeTask, String> implements AddressNoticeTaskService {

    private AddressNoticeTaskDAO addressNoticeTaskDAO;
    private AddressNoticeService addressNoticeService;

    @Resource
    public void setAddressNoticeTaskDAO(AddressNoticeTaskDAO addressNoticeTaskDAO) {
        super.setBaseDAO(addressNoticeTaskDAO);
        this.addressNoticeTaskDAO = addressNoticeTaskDAO;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }

    @Override
    public void depositProcessing(String address, String coinType) {
        AddressNotice addressNotice = addressNoticeService.getDepositNoticeAddress(address, coinType);

        AddressNoticeTask addressNoticeTask = new AddressNoticeTask();
        addressNoticeTask.setAddressNoticeId(addressNotice.getId());
        addressNoticeTask.setType(AddressNoticeTask.TYPE_ETH_CZCLZ);
        addressNoticeTask.setStatus(AddressNoticeTask.STATUS_UNTREATED);

        addressNoticeTaskDAO.insert(addressNoticeTask);
    }

    @Override
    public PaginationRepertory<AddressNoticeTask> getDepositProcessing(PaginationCondition<AddressNoticeTask> paginationCondition) {
        paginationCondition.getCondition().setType(AddressNoticeTask.TYPE_ETH_CZCLZ);
        paginationCondition.getCondition().setStatus(AddressNoticeTask.STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }
}
