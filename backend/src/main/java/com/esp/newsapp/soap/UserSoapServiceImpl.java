package com.esp.newsapp.soap;

import com.esp.newsapp.dto.UserDTO;
import com.esp.newsapp.dto.UserSaveDTO;
import com.esp.newsapp.model.User;
import com.esp.newsapp.service.AuthTokenService;
import com.esp.newsapp.service.UserService;
import jakarta.jws.WebService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@WebService(serviceName = "UserSoapService", endpointInterface = "com.esp.newsapp.soap.UserSoapService")
public class UserSoapServiceImpl implements UserSoapService {

    private final UserService userService;
    private final AuthTokenService authTokenService;

    public UserSoapServiceImpl(UserService userService, AuthTokenService authTokenService) {
        this.userService = userService;
        this.authTokenService = authTokenService;
    }

    @Override
    public AuthResult authenticate(String login, String password) {
        if (!userService.checkCredentials(login, password)) {
            return new AuthResult(false, null, "Identifiants invalides");
        }
        User u = userService.getByUsername(login);
        return new AuthResult(true, u.getRole().name(), "Authentification reussie");
    }

    @Override
    public List<UserSoapDTO> listUsers(String token) {
        checkToken(token);
        return userService.getAll().stream()
                .map(u -> new UserSoapDTO(u.id(), u.username(), u.role()))
                .toList();
    }

    @Override
    public UserSoapDTO addUser(String token, String username, String password, String role) {
        checkToken(token);
        UserDTO u = userService.create(new UserSaveDTO(username, password, role));
        return new UserSoapDTO(u.id(), u.username(), u.role());
    }

    @Override
    public UserSoapDTO updateUser(String token, Long id, String username, String password, String role) {
        checkToken(token);
        UserDTO u = userService.update(id, new UserSaveDTO(username, password, role));
        return new UserSoapDTO(u.id(), u.username(), u.role());
    }

    @Override
    public boolean deleteUser(String token, Long id) {
        checkToken(token);
        userService.delete(id);
        return true;
    }

    private void checkToken(String token) {
        if (!authTokenService.isValid(token)) {
            throw new SecurityException("Jeton d'authentification invalide ou manquant");
        }
    }
}
