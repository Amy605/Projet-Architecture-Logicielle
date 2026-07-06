package com.esp.client.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

// Contrat identique a celui expose par le serveur (com.esp.newsapp.soap.UserSoapService).
// Le meme namespace/serviceName doit correspondre pour que Service.getPort(...) fonctionne.
@WebService(targetNamespace = "http://soap.newsapp.esp.com/", name = "UserSoapService")
public interface UserSoapService {

    @WebMethod
    AuthResult authenticate(@WebParam(name = "login") String login,
                             @WebParam(name = "password") String password);

    @WebMethod
    List<UserSoapDTO> listUsers(@WebParam(name = "token") String token);

    @WebMethod
    UserSoapDTO addUser(@WebParam(name = "token") String token,
                         @WebParam(name = "username") String username,
                         @WebParam(name = "password") String password,
                         @WebParam(name = "role") String role);

    @WebMethod
    UserSoapDTO updateUser(@WebParam(name = "token") String token,
                            @WebParam(name = "id") Long id,
                            @WebParam(name = "username") String username,
                            @WebParam(name = "password") String password,
                            @WebParam(name = "role") String role);

    @WebMethod
    boolean deleteUser(@WebParam(name = "token") String token,
                        @WebParam(name = "id") Long id);
}
