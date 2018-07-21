package com.sharingif.blockchain.app.components;

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
        UrlBody urlBody = (UrlBody) request[1];

        RequestContext<Object[]> requestContext = new RequestContext<Object[]>(
                MediaType.APPLICATION_JSON.toString()
                ,urlBody.getUrl(), Locale.getDefault()
                ,RequestMethod.POST.name()
                ,new Object[]{urlBody.getBody()}
                );

        return requestContext;
    }

}
