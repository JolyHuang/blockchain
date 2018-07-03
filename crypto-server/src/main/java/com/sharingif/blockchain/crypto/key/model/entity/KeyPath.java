package com.sharingif.blockchain.crypto.key.model.entity;

import com.sharingif.blockchain.crypto.api.key.KeyGenerateReq;

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

    public KeyPath(KeyGenerateReq req) {
        StringBuilder stringBuilder = new StringBuilder("m/44'");
        if(req.getCoinType() != null) {
            parentPath = stringBuilder.toString();
            stringBuilder.append("/").append(req.getCoinType()).append("'");
            return;
        }
        if(req.getAccount() != null) {
            parentPath = stringBuilder.toString();
            stringBuilder.append("/").append(req.getAccount()).append("'");
            return;
        }
        if(req.getChange() != null) {
            parentPath = stringBuilder.toString();
            stringBuilder.append("/").append(req.getChange());
            return;
        }
        if(req.getAddressIndex() != null) {
            parentPath = stringBuilder.toString();
            stringBuilder.append("/").append(req.getAddressIndex());
            return;
        }

        path = stringBuilder.toString();
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getPath() {
        return path;
    }
}
