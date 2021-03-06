package com.sharingif.blockchain.transaction.model.entity;

import com.sharingif.cube.core.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ETHAddressRegister
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/12 下午6:10
 */
public class ETHAddressRegister {

    private Map<String,String> ethMap = new HashMap<String,String>();
    private Map<String, Map<String, String>> contractAddressMap = new HashMap<String, Map<String, String>>();

    public ETHAddressRegister(List<AddressRegister> addressRegisterList) {
        for(AddressRegister addressRegister : addressRegisterList) {
            addAddressRegister(addressRegister);
        }
    }

    public Map<String, String> getEthMap() {
        return ethMap;
    }

    public void setEthMap(Map<String, String> ethMap) {
        this.ethMap = ethMap;
    }

    public Map<String, Map<String, String>> getContractAddressMap() {
        return contractAddressMap;
    }

    public void setContractAddressMap(Map<String, Map<String, String>> contractAddressMap) {
        this.contractAddressMap = contractAddressMap;
    }

    public Map<String, String> getSubCoinTypeMap(String address) {
        return contractAddressMap.get(address.toLowerCase());
    }

    public boolean isETHRegisterAddress(String address) {
        return ethMap.get(address.toLowerCase()) == null ? false : true;
    }

    public boolean isContractRegisterAddress(String address) {
        return contractAddressMap.get(address.toLowerCase()) == null ? false : true;
    }

    public boolean isSubCoinType(Map<String, String> subCoinTypeMap, String address) {
        return subCoinTypeMap.get(address.toLowerCase()) == null ? false : true;
    }

    public void addAddressRegister(AddressRegister addressRegister) {
        String address = addressRegister.getAddress().toLowerCase();

        if(StringUtils.isTrimEmpty(addressRegister.getSubCoinType())) {
            ethMap.put(address, addressRegister.getCoinType());
        } else {
            String contractAddress = addressRegister.getContractAddress().toLowerCase();

            Map<String, String> subCoinTypeMap = contractAddressMap.get(contractAddress);
            if(subCoinTypeMap == null) {
                subCoinTypeMap = new HashMap<String, String>();
                contractAddressMap.put(contractAddress, subCoinTypeMap);
            }
            subCoinTypeMap.put(address, addressRegister.getSubCoinType());
        }
    }

}
