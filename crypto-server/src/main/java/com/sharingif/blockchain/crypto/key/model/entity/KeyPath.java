package com.sharingif.blockchain.crypto.key.model.entity;

import com.sharingif.blockchain.crypto.api.key.BIP44AddressIndexReq;
import com.sharingif.blockchain.crypto.api.key.BIP44ChangeReq;
import com.sharingif.blockchain.crypto.api.key.BIP44GenerateReq;

/**
 * KeyPath
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午2:32
 */
public class KeyPath {

    private String parentPath;
    private String path;
    private String bitcoinjParentPath;
    private String bitcoinjPath;

    public KeyPath(BIP44ChangeReq req) {
        BIP44GenerateReq bip44GenerateReq = new BIP44GenerateReq();
        bip44GenerateReq.setMnemonicId(req.getMnemonicId());
        bip44GenerateReq.setCoinType(req.getCoinType());
        bip44GenerateReq.setAccount(req.getAccount());
        bip44GenerateReq.setChange(req.getChange());

        setAllPath(bip44GenerateReq);
    }

    public KeyPath(BIP44AddressIndexReq req) {
        BIP44GenerateReq bip44GenerateReq = new BIP44GenerateReq();
        bip44GenerateReq.setMnemonicId(req.getMnemonicId());
        bip44GenerateReq.setCoinType(req.getCoinType());
        bip44GenerateReq.setAccount(req.getAccount());
        bip44GenerateReq.setChange(req.getChange());
        bip44GenerateReq.setAddressIndex(req.getAddressIndex());

        setAllPath(bip44GenerateReq);
    }

    public KeyPath(BIP44GenerateReq req) {
        setAllPath(req);
    }

    protected void setAllPath(BIP44GenerateReq req) {
        StringBuilder stringBuilder = new StringBuilder("m/44'");
        StringBuilder bitcoinjPathStringBuilder = new StringBuilder("M/44H");
        if(req.getCoinType() != null) {
            parentPath = stringBuilder.toString();
            bitcoinjParentPath = bitcoinjPathStringBuilder.toString();
            stringBuilder.append("/").append(req.getCoinType()).append("'");
            bitcoinjPathStringBuilder.append("/").append(req.getCoinType()).append("H");
        }
        if(req.getAccount() != null) {
            parentPath = stringBuilder.toString();
            bitcoinjParentPath = bitcoinjPathStringBuilder.toString();
            stringBuilder.append("/").append(req.getAccount()).append("'");
            bitcoinjPathStringBuilder.append("/").append(req.getAccount()).append("H");
        }
        if(req.getChange() != null) {
            parentPath = stringBuilder.toString();
            bitcoinjParentPath = bitcoinjPathStringBuilder.toString();
            stringBuilder.append("/").append(req.getChange());
            bitcoinjPathStringBuilder.append("/").append(req.getChange());
        }
        if(req.getAddressIndex() != null) {
            parentPath = stringBuilder.toString();
            bitcoinjParentPath = bitcoinjPathStringBuilder.toString();
            stringBuilder.append("/").append(req.getAddressIndex());
            bitcoinjPathStringBuilder.append("/").append(req.getAddressIndex());
        }

        path = stringBuilder.toString();
        bitcoinjPath = bitcoinjPathStringBuilder.toString();
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getPath() {
        return path;
    }

    public String getBitcoinjParentPath() {
        return bitcoinjParentPath;
    }

    public String getBitcoinjPath() {
        return bitcoinjPath;
    }
}
