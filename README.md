# Le Phare — Projet d'Architecture Logicielle

Site d'actualite complet avec trois profils utilisateurs (visiteur, editeur, administrateur),
un service web **SOAP** (gestion des utilisateurs) et un service web **REST** (consultation des
articles en JSON/XML), plus une **application client Java** de gestion des utilisateurs.

## 1. De quoi parle ce projet

Le sujet demande de realiser, **integralement**, trois parties liees entre elles :

| # | Partie | Ce qui est demande |
|---|--------|---------------------|
| 1 | **Site web** | Page d'accueil paginee (suivant/precedent), detail d'article, filtre par categorie, 3 profils (visiteur, editeur, admin) |
| 2 | **Services web** | Un service **SOAP** (CRUD utilisateurs + authentification, protege par jeton) et un service **REST** (liste des articles en JSON/XML, par categorie) |
| 3 | **Application client** | Appli Java qui demande login/mdp, appelle le service SOAP d'authentification, et si l'utilisateur est admin, permet de gerer les utilisateurs via SOAP |

## 2. Ce qui a ete implemente

- **Backend** : Spring Boot (Java 17) — REST + SOAP (Apache CXF) + securite JWT + base H2
- **Frontend** : React (Vite) — design editorial "Le Phare", responsive
- **Client** : application Java console (JAX-WS / Apache CXF)

```
projet-al/
├── backend/        Spring Boot : API REST + service SOAP + securite
├── frontend/        React (Vite) : site web public + espaces editeur/admin
├── client-app/       Application Java console consommant le service SOAP
└── README.md
```

