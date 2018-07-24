package com.sharingif.blockchain.app.autoconfigure.components;

import co.olivecoin.wallet.api.http.HttpResponse;
import com.sharingif.blockchain.app.components.HttpBusinessCommunicationExceptionHandler;
import com.sharingif.blockchain.app.components.StringToHttpMarshaller;
import com.sharingif.blockchain.app.components.UrlBodyTransportRequestContextResolver;
import com.sharingif.cube.communication.http.apache.transport.HttpJsonConnection;
import com.sharingif.cube.communication.http.transport.transform.ObjectToJsonStringMarshaller;
import com.sharingif.cube.communication.remote.RemoteServices;
import com.sharingif.cube.communication.remote.RemoteServicesApplicationContext;
import com.sharingif.cube.communication.transport.ProxyInterfaceHandlerMethodCommunicationTransportFactory;
import com.sharingif.cube.communication.transport.transform.ProxyInterfaceHandlerMethodCommunicationTransform;
import com.sharingif.cube.core.handler.bind.support.BindingInitializer;
import com.sharingif.cube.core.handler.chain.MultiHandlerMethodChain;
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
public class RemoteContextAutoconfigure {

    @Bean(name = "httpJsonConnection")
    public HttpJsonConnection createSgpHttpJsonConnection() {
        HttpJsonConnection apacheHttpJsonConnection = new HttpJsonConnection();

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

    @Bean(name= "httpJsonRemoteHandlerMethodTransportFactory")
    public ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> createHandlerMethodCommunicationTransportFactory(
            HttpJsonConnection httpJsonConnection
            ,ProxyInterfaceHandlerMethodCommunicationTransform<String,String,HttpResponse<Object>> httpProxyInterfaceHandlerMethodCommunicationTransform
            ,HttpBusinessCommunicationExceptionHandler httpBusinessCommunicationExceptionHandler
            ,MultiHandlerMethodChain transportChains
    ) {
        ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> httpJsonRemoteHandlerMethodTransportFactory = new ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>>();
        httpJsonRemoteHandlerMethodTransportFactory.setConnection(httpJsonConnection);
        httpJsonRemoteHandlerMethodTransportFactory.setTransform(httpProxyInterfaceHandlerMethodCommunicationTransform);
        httpJsonRemoteHandlerMethodTransportFactory.setBusinessCommunicationExceptionHandler(httpBusinessCommunicationExceptionHandler);
        httpJsonRemoteHandlerMethodTransportFactory.setHandlerMethodChain(transportChains);

        return httpJsonRemoteHandlerMethodTransportFactory;
    }

    @Bean(name = "urlBodyTransportRequestContextResolver")
    public UrlBodyTransportRequestContextResolver createUrlBodyTransportRequestContextResolver() {
        return new UrlBodyTransportRequestContextResolver();
    }

    @Bean(name = "remoteServices")
    public RemoteServices createOpayRemoteServices(
            UrlBodyTransportRequestContextResolver urlBodyTransportRequestContextResolver
            ,ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,HttpResponse<Object>> httpJsonRemoteHandlerMethodTransportFactory
    ) {
        List<String> services = new ArrayList<String>();

        services.add("co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService");

        RemoteServices remoteServices = new RemoteServices();
        remoteServices.setRequestContextResolver(urlBodyTransportRequestContextResolver);
        remoteServices.setHandlerMethodCommunicationTransportFactory(httpJsonRemoteHandlerMethodTransportFactory);
        remoteServices.setServices(services);

        return remoteServices;
    }

    @Bean(name = "remoteServicesApplicationContext")
    public RemoteServicesApplicationContext createRemoteServicesApplicationContext(
            BindingInitializer bindingInitializer
            ,RemoteServices remoteServices
    ) {


        List<RemoteServices> remoteServicesList = new ArrayList<RemoteServices>();
        remoteServicesList.add(remoteServices);

        RemoteServicesApplicationContext remoteServicesApplicationContext = new RemoteServicesApplicationContext();
        remoteServicesApplicationContext.setBindingInitializer(bindingInitializer);
        remoteServicesApplicationContext.setRemoteServices(remoteServicesList);

        return remoteServicesApplicationContext;
    }

}
