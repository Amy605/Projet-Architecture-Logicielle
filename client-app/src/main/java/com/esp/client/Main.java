package com.esp.client;

import com.esp.client.soap.AuthResult;
import com.esp.client.soap.UserSoapDTO;
import com.esp.client.soap.UserSoapService;

import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Application client (console) pour la gestion des utilisateurs.
 * Au lancement, elle demande login/mot de passe et invoque le service web
 * d'authentification SOAP. Si l'utilisateur a les droits ADMIN, l'application
 * donne acces complet a la gestion des utilisateurs via le service SOAP
 * (necessite un jeton d'authentification genere depuis la page d'administration).
 */
public class Main {

    private static final String WSDL_URL = "http://localhost:9000/services/users?wsdl";
    private static final String NAMESPACE = "http://soap.newsapp.esp.com/";
    private static final String SERVICE_NAME = "UserSoapService";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println(" Application client - Gestion utilisateurs");
        System.out.println("=========================================");

        UserSoapService port = createPort();

        System.out.print("Login       : ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        AuthResult authResult = port.authenticate(login, password);

        if (!authResult.success) {
            System.out.println("\n[ERREUR] " + authResult.message);
            return;
        }

        System.out.println("\nAuthentification reussie. Role : " + authResult.role);

        if (!"ADMIN".equalsIgnoreCase(authResult.role)) {
            System.out.println("Cet utilisateur n'a pas les droits d'administration.");
            System.out.println("Seul un ADMIN peut gerer les utilisateurs depuis cette application.");
            return;
        }

        System.out.print("\nJeton d'authentification SOAP (genere depuis la page d'administration) : ");
        String token = scanner.nextLine();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listUsers(port, token);
                    case "2" -> addUser(port, token, scanner);
                    case "3" -> updateUser(port, token, scanner);
                    case "4" -> deleteUser(port, token, scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("[ERREUR] " + e.getMessage());
            }
        }

        System.out.println("Fin de l'application. Au revoir !");
    }

    private static UserSoapService createPort() throws Exception {
        QName serviceQName = new QName(NAMESPACE, SERVICE_NAME);
        Service service = Service.create(new URL(WSDL_URL), serviceQName);
        return service.getPort(UserSoapService.class);
    }

    private static void printMenu() {
        System.out.println("\n---------- Menu ----------");
        System.out.println("1. Lister les utilisateurs");
        System.out.println("2. Ajouter un utilisateur");
        System.out.println("3. Modifier un utilisateur");
        System.out.println("4. Supprimer un utilisateur");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private static void listUsers(UserSoapService port, String token) {
        List<UserSoapDTO> users = port.listUsers(token);
        System.out.println("\nID\tNom d'utilisateur\tRole");
        for (UserSoapDTO u : users) {
            System.out.println(u.id + "\t" + u.username + "\t\t" + u.role);
        }
    }

    private static void addUser(UserSoapService port, String token, Scanner scanner) {
        System.out.print("Nom d'utilisateur : ");
        String username = scanner.nextLine();
        System.out.print("Mot de passe      : ");
        String password = scanner.nextLine();
        System.out.print("Role (EDITEUR/ADMIN) : ");
        String role = scanner.nextLine();
        UserSoapDTO created = port.addUser(token, username, password, role);
        System.out.println("Utilisateur cree avec l'ID " + created.id);
    }

    private static void updateUser(UserSoapService port, String token, Scanner scanner) {
        System.out.print("ID de l'utilisateur a modifier : ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Nouveau nom d'utilisateur : ");
        String username = scanner.nextLine();
        System.out.print("Nouveau mot de passe      : ");
        String password = scanner.nextLine();
        System.out.print("Nouveau role (EDITEUR/ADMIN) : ");
        String role = scanner.nextLine();
        UserSoapDTO updated = port.updateUser(token, id, username, password, role);
        System.out.println("Utilisateur mis a jour : " + updated.username + " (" + updated.role + ")");
    }

    private static void deleteUser(UserSoapService port, String token, Scanner scanner) {
        System.out.print("ID de l'utilisateur a supprimer : ");
        Long id = Long.parseLong(scanner.nextLine());
        boolean ok = port.deleteUser(token, id);
        System.out.println(ok ? "Utilisateur supprime." : "Echec de la suppression.");
    }
}
