package com.sharingif.blockchain.app.components;

import co.olivecoin.wallet.api.http.HttpResponse;
import co.olivecoin.wallet.api.http.HttpResponseHeaders;
import com.sharingif.cube.communication.JsonModel;
import com.sharingif.cube.communication.exception.BusinessCommunicationException;
import com.sharingif.cube.communication.exception.IBusinessCommunicationExceptionHandler;

/**
 * JsonModel 业务通讯异常处理器
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2017/7/27 下午4:21
 */
public class HttpBusinessCommunicationExceptionHandler implements IBusinessCommunicationExceptionHandler<HttpResponse<Object>> {

    @Override
    public void handleCommunicationException(HttpResponse<Object> httpResponse) throws BusinessCommunicationException {

        HttpResponseHeaders headers = httpResponse.getHeaders();

        if(headers.isTranStatus()) {
            return;
        }

        BusinessCommunicationException businessCommunicationException = new BusinessCommunicationException(headers.getErrCode());
        businessCommunicationException.setLocalizedMessage(headers.getErrMsg());

        throw businessCommunicationException;
    }

}
