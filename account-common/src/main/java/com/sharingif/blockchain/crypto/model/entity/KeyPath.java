package com.sharingif.blockchain.crypto.model.entity;


import com.sharingif.blockchain.crypto.api.key.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.crypto.api.key.entity.BIP44ChangeReq;
import com.sharingif.blockchain.crypto.api.key.entity.BIP44GenerateReq;

/**
 * KeyPath
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午2:32
 */
public class KeyPath {

    private String path;

    public KeyPath(String path) {
        this.path = path;
    }

    public KeyPath(BIP44ChangeReq req) {
        BIP44GenerateReq bip44GenerateReq = new BIP44GenerateReq();
        bip44GenerateReq.setMnemonicId(req.getMnemonicId());
        bip44GenerateReq.setCoinType(req.getCoinType());
        bip44GenerateReq.setAccount(req.getAccount());
        bip44GenerateReq.setChange(req.getChange());

        setAllPath(bip44GenerateReq);
    }

    public KeyPath(BIP44AddressIndexReq req, int addressIndex) {
        BIP44GenerateReq bip44GenerateReq = new BIP44GenerateReq();
        bip44GenerateReq.setMnemonicId(req.getMnemonicId());
        bip44GenerateReq.setCoinType(req.getCoinType());
        bip44GenerateReq.setAccount(req.getAccount());
        bip44GenerateReq.setChange(req.getChange());
        bip44GenerateReq.setAddressIndex(addressIndex);

        setAllPath(bip44GenerateReq);
    }

    public KeyPath(BIP44GenerateReq req) {
        setAllPath(req);
    }

    protected void setAllPath(BIP44GenerateReq req) {
        StringBuilder stringBuilder = new StringBuilder("m/44'");
        if(req.getCoinType() != null) {
            stringBuilder.append("/").append(req.getCoinType()).append("'");
        }
        if(req.getAccount() != null) {
            stringBuilder.append("/").append(req.getAccount()).append("'");
        }
        if(req.getChange() != null) {
            stringBuilder.append("/").append(req.getChange());
        }
        if(req.getAddressIndex() != null) {
            stringBuilder.append("/").append(req.getAddressIndex());
        }

        path = stringBuilder.toString();
    }


    public String getPath() {
        return path;
    }

    public Integer getCoinType() {
        String[] pathArray = this.path.split("/");

        return new Integer(pathArray[2].replace("'",""));
    }

    public Integer getAccount() {
        String[] pathArray = this.path.split("/");

        return new Integer(pathArray[3].replace("'",""));
    }

    public Integer getChange() {
        String[] pathArray = this.path.split("/");

        return new Integer(pathArray[4]);
    }

}
