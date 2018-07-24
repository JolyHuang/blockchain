package com.sharingif.blockchain.app.components;

import co.olivecoin.wallet.api.http.HttpRequestHeaders;
import com.sharingif.cube.communication.MediaType;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import com.sharingif.cube.core.request.RequestContext;
import com.sharingif.cube.core.request.RequestContextResolver;

import java.util.Locale;

/**
 * UrlBodyTransportRequestContextResolver
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/21 下午1:21
 */
public class UrlBodyTransportRequestContextResolver implements RequestContextResolver<Object[], RequestContext<Object[]>> {

    @Override
    public RequestContext<Object[]> resolveRequest(Object[] request) {
        Object[] args = (Object[]) request[1];
        UrlBody urlBody = (UrlBody) args[0];

        co.olivecoin.wallet.api.http.HttpRequest<Object> httpRequest = new co.olivecoin.wallet.api.http.HttpRequest<Object>();
        co.olivecoin.wallet.api.http.HttpRequestHeaders headers = new co.olivecoin.wallet.api.http.HttpRequestHeaders();
        headers.setDeviceId("000101");
        headers.setDeviceType("IOS");
        headers.setAppVersion("1.0.0");
        httpRequest.setHeaders(headers);
        httpRequest.setBody(urlBody.getBody());

        RequestContext<Object[]> requestContext = new RequestContext<Object[]>(
                MediaType.APPLICATION_JSON.toString()
                ,urlBody.getUrl(), Locale.getDefault()
                ,RequestMethod.POST.name()
                ,new Object[]{httpRequest}
                );

        return requestContext;
    }

}
