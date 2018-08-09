package com.sharingif.blockchain.btc.entity;

import com.neemre.btcdcli4j.core.domain.RawOutput;

/**
 * UtxoRawInput
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/9 下午5:33
 */
public class UtxoRawInput {

    /**
     * vin交易hash
     */
    private String vinTxHash;
    /**
     * vin tx index
     */
    private Integer vinTxIndex;
    /**
     * vout
     */
    private Integer vout;
    /**
     * rawOutput
     */
    private RawOutput rawOutput;

    public String getVinTxHash() {
        return vinTxHash;
    }

    public void setVinTxHash(String vinTxHash) {
        this.vinTxHash = vinTxHash;
    }

    public Integer getVinTxIndex() {
        return vinTxIndex;
    }

    public void setVinTxIndex(Integer vinTxIndex) {
        this.vinTxIndex = vinTxIndex;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public RawOutput getRawOutput() {
        return rawOutput;
    }

    public void setRawOutput(RawOutput rawOutput) {
        this.rawOutput = rawOutput;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UtxoRawInput{");
        sb.append("vinTxHash='").append(vinTxHash).append('\'');
        sb.append(", vinTxIndex=").append(vinTxIndex);
        sb.append(", vout=").append(vout);
        sb.append(", rawOutput=").append(rawOutput);
        sb.append('}');
        return sb.toString();
    }
}
