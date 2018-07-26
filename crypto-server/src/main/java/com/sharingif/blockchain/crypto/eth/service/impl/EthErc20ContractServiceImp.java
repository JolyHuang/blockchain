package com.sharingif.blockchain.crypto.eth.service.impl;

import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferRsp;
import com.sharingif.blockchain.crypto.eth.service.EthErc20ContractService;
import com.sharingif.blockchain.crypto.key.service.SecretKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * EthErc20ContractServiceImp
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:53
 */
@Service
public class EthErc20ContractServiceImp  implements EthErc20ContractService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SecretKeyService secretKeyService;

    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }

    @Override
    public Erc20TransferRsp transfer(Erc20TransferReq req) {

        Credentials credentials = secretKeyService.getCredentials(req.getSecretKeyId(), req.getPassword());

        Function function = new Function(
                "transfer"
                ,Arrays.asList(new Address(req.getToAddress()), new Uint256(req.getAmount()))
                ,new ArrayList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction  = RawTransaction.createTransaction(
                req.getNonce()
                ,req.getGasPrice()
                ,req.getGasLimit()
                ,req.getContractAddress()
                ,encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        Erc20TransferRsp rsp = new Erc20TransferRsp();
        rsp.setHexValue(hexValue);

        return rsp;
    }

}
