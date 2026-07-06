package com.esp.newsapp.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

// Contrat du service web SOAP de gestion des utilisateurs.
// Toutes les operations de gestion (list/add/update/delete) exigent un jeton
// d'authentification valide, genere prealablement par un administrateur.
@WebService
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
