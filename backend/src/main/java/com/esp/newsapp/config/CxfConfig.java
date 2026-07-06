package com.esp.newsapp.config;

import com.esp.newsapp.soap.UserSoapService;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    private final Bus bus;
    private final UserSoapService userSoapService;

    public CxfConfig(Bus bus, UserSoapService userSoapService) {
        this.bus = bus;
        this.userSoapService = userSoapService;
    }

    // Publie le service SOAP sur /services/users?wsdl
    @Bean
    public Endpoint userSoapEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, userSoapService);
        endpoint.publish("/users");
        return endpoint;
    }
}
