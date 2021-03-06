package com.sharingif.blockchain.app.autoconfigure.components;

import co.olivecoin.wallet.api.http.HttpResponse;
import com.sharingif.blockchain.app.components.HttpBusinessCommunicationExceptionHandler;
import com.sharingif.blockchain.app.components.StringToHttpMarshaller;
import com.sharingif.blockchain.app.components.UrlBodyTransportRequestContextResolver;
import com.sharingif.cube.communication.http.apache.transport.HttpJsonConnection;
import com.sharingif.cube.communication.http.transport.transform.ObjectToJsonStringMarshaller;
import com.sharingif.cube.communication.remote.RemoteServices;
import com.sharingif.cube.communication.transport.ProxyInterfaceHandlerMethodCommunicationTransportFactory;
import com.sharingif.cube.communication.transport.transform.ProxyInterfaceHandlerMethodCommunicationTransform;
import com.sharingif.cube.core.handler.chain.MultiHandlerMethodChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * RemoteContextAutoconfigure
 * 2017/6/1 下午3:23
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
@Configuration
public class UrlBodyRemoteContextAutoconfigure {

    @Bean(name = "urlBodyHttpJsonConnection")
    public HttpJsonConnection createUrlBodyHttpJsonConnection(
            @Value("${http.so.timeout}")int soTimeout
    ) {
        HttpJsonConnection apacheHttpJsonConnection = new HttpJsonConnection();
        apacheHttpJsonConnection.setSoTimeout(soTimeout);

        return apacheHttpJsonConnection;
    }

    @Bean(name="stringToHttpMarshaller")
    public StringToHttpMarshaller createStringToHttpMarshaller() {
        StringToHttpMarshaller stringToHttpMarshaller = new StringToHttpMarshaller();

        return stringToHttpMarshaller;
    }

    @Bean(name="httpProxyInterfaceHandlerMethodCommunicationTransform")
    public ProxyInterfaceHandlerMethodCommunicationTransform<String,String,HttpResponse<Object>> createJsonModelProxyInterfaceHandlerMethodCommunicationTransform(
            ObjectToJsonStringMarshaller objectToJsonStringMarshaller
            ,StringToHttpMarshaller stringToHttpMarshaller
    ) {
        ProxyInterfaceHandlerMethodCommunicationTransform<String,String,HttpResponse<Object>> proxyInterfaceHandlerMethodCommunicationTransform = new ProxyInterfaceHandlerMethodCommunicationTransform<String,String,HttpResponse<Object>>();
        proxyInterfaceHandlerMethodCommunicationTransform.setMarshaller(objectToJsonStringMarshaller);
        proxyInterfaceHandlerMethodCommunicationTransform.setUnmarshaller(stringToHttpMarshaller);

        return proxyInterfaceHandlerMethodCommunicationTransform;
    }

    @Bean(name="httpBusinessCommunicationExceptionHandler")
    public HttpBusinessCommunicationExceptionHandler createHttpBusinessCommunicationExceptionHandler(){
        return new HttpBusinessCommunicationExceptionHandler();
    }

    @Bean(name= "urlBodyHttpJsonRemoteHandlerMethodTransportFactory")
    public ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> createHandlerMethodCommunicationTransportFactory(
            HttpJsonConnection urlBodyHttpJsonConnection
            ,ProxyInterfaceHandlerMethodCommunicationTransform<String,String,HttpResponse<Object>> httpProxyInterfaceHandlerMethodCommunicationTransform
            ,HttpBusinessCommunicationExceptionHandler httpBusinessCommunicationExceptionHandler
            ,MultiHandlerMethodChain transportChains
    ) {
        ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> httpJsonRemoteHandlerMethodTransportFactory = new ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>>();
        httpJsonRemoteHandlerMethodTransportFactory.setConnection(urlBodyHttpJsonConnection);
        httpJsonRemoteHandlerMethodTransportFactory.setTransform(httpProxyInterfaceHandlerMethodCommunicationTransform);
        httpJsonRemoteHandlerMethodTransportFactory.setBusinessCommunicationExceptionHandler(httpBusinessCommunicationExceptionHandler);
        httpJsonRemoteHandlerMethodTransportFactory.setHandlerMethodChain(transportChains);

        return httpJsonRemoteHandlerMethodTransportFactory;
    }

    @Bean(name = "urlBodyTransportRequestContextResolver")
    public UrlBodyTransportRequestContextResolver createUrlBodyTransportRequestContextResolver() {
        return new UrlBodyTransportRequestContextResolver();
    }

    @Bean(name = "urlBodyRemoteServices")
    public RemoteServices createOpayRemoteServices(
            UrlBodyTransportRequestContextResolver urlBodyTransportRequestContextResolver
            ,ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> urlBodyHttpJsonRemoteHandlerMethodTransportFactory
    ) {
        List<String> services = new ArrayList<String>();

        services.add("co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService");

        RemoteServices remoteServices = new RemoteServices();
        remoteServices.setRequestContextResolver(urlBodyTransportRequestContextResolver);
        remoteServices.setHandlerMethodCommunicationTransportFactory(urlBodyHttpJsonRemoteHandlerMethodTransportFactory);
        remoteServices.setServices(services);

        return remoteServices;
    }

}
