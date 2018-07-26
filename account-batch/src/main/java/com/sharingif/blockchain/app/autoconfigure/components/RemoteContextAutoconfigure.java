package com.sharingif.blockchain.app.autoconfigure.components;

import com.sharingif.cube.communication.JsonModel;
import com.sharingif.cube.communication.exception.JsonModelBusinessCommunicationExceptionHandler;
import com.sharingif.cube.communication.http.apache.transport.HttpJsonConnection;
import com.sharingif.cube.communication.http.transport.HandlerMethodCommunicationTransportRequestContextResolver;
import com.sharingif.cube.communication.remote.RemoteServices;
import com.sharingif.cube.communication.remote.RemoteServicesApplicationContext;
import com.sharingif.cube.communication.transport.ProxyInterfaceHandlerMethodCommunicationTransportFactory;
import com.sharingif.cube.communication.transport.transform.ProxyInterfaceHandlerMethodCommunicationTransform;
import com.sharingif.cube.core.handler.bind.support.BindingInitializer;
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
public class RemoteContextAutoconfigure {

    @Bean(name = "httpJsonConnection")
    public HttpJsonConnection createSgpHttpJsonConnection(
            @Value("${http.host}")String host
            ,@Value("${http.port}")int port
            ,@Value("${http.contextPath}") String contextPath
        ) {
        HttpJsonConnection apacheHttpJsonConnection = new HttpJsonConnection(host, port, contextPath);

        return apacheHttpJsonConnection;
    }


    @Bean(name= "httpJsonRemoteHandlerMethodTransportFactory")
    public ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,JsonModel<Object>> createHandlerMethodCommunicationTransportFactory(
            HttpJsonConnection httpJsonConnection
            ,ProxyInterfaceHandlerMethodCommunicationTransform<String,String,JsonModel<Object>> jsonModelProxyInterfaceHandlerMethodCommunicationTransform
            ,JsonModelBusinessCommunicationExceptionHandler jsonModelBusinessCommunicationExceptionHandler
            ,MultiHandlerMethodChain transportChains
    ) {
        ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,JsonModel<Object>> httpJsonRemoteHandlerMethodTransportFactory = new ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,JsonModel<Object>>();
        httpJsonRemoteHandlerMethodTransportFactory.setConnection(httpJsonConnection);
        httpJsonRemoteHandlerMethodTransportFactory.setTransform(jsonModelProxyInterfaceHandlerMethodCommunicationTransform);
        httpJsonRemoteHandlerMethodTransportFactory.setBusinessCommunicationExceptionHandler(jsonModelBusinessCommunicationExceptionHandler);
        httpJsonRemoteHandlerMethodTransportFactory.setHandlerMethodChain(transportChains);

        return httpJsonRemoteHandlerMethodTransportFactory;
    }

    @Bean(name = "remoteServices")
    public RemoteServices createOpayRemoteServices(
            HandlerMethodCommunicationTransportRequestContextResolver handlerMethodCommunicationTransportRequestContextResolver
            ,ProxyInterfaceHandlerMethodCommunicationTransportFactory<String,String,JsonModel<Object>> httpJsonRemoteHandlerMethodTransportFactory
    ) {
        List<String> services = new ArrayList<String>();

        services.add("com.sharingif.blockchain.crypto.api.eth.service.EthApiService");
        services.add("com.sharingif.blockchain.crypto.api.eth.service.EthErc20ContractApiService");

        RemoteServices remoteServices = new RemoteServices();
        remoteServices.setRequestContextResolver(handlerMethodCommunicationTransportRequestContextResolver);
        remoteServices.setHandlerMethodCommunicationTransportFactory(httpJsonRemoteHandlerMethodTransportFactory);
        remoteServices.setServices(services);

        return remoteServices;
    }

    @Bean(name = "remoteServicesApplicationContext")
    public RemoteServicesApplicationContext createRemoteServicesApplicationContext(
            BindingInitializer bindingInitializer
            ,RemoteServices remoteServices
            ,RemoteServices urlBodyRemoteServices
    ) {


        List<RemoteServices> remoteServicesList = new ArrayList<RemoteServices>();
        remoteServicesList.add(remoteServices);
        remoteServicesList.add(urlBodyRemoteServices);

        RemoteServicesApplicationContext remoteServicesApplicationContext = new RemoteServicesApplicationContext();
        remoteServicesApplicationContext.setBindingInitializer(bindingInitializer);
        remoteServicesApplicationContext.setRemoteServices(remoteServicesList);

        return remoteServicesApplicationContext;
    }

}
