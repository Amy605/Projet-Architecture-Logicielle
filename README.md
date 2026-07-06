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

### Comptes de demonstration (crees automatiquement au premier lancement)

| Utilisateur | Mot de passe | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `editeur` | `editeur123` | EDITEUR |

Un jeton SOAP de demonstration est aussi cree : `demo-token-0000-0000-000000000000`
(a utiliser dans l'application client, ou generez-en un nouveau depuis l'administration).

## 3. Comment executer le projet

### Prerequis
- Java 17+ et Maven
- Node.js 18+ et npm

### 3.1 Backend (a lancer en premier)

```bash
cd backend
mvn spring-boot:run
```

- API REST : `http://localhost:9000/api/...`
- Service SOAP + WSDL : `http://localhost:9000/services/users?wsdl`
- Console H2 (base de donnees) : `http://localhost:9000/h2-console` (JDBC URL : `jdbc:h2:file:./data/newsapp`)

### 3.2 Frontend

```bash
cd frontend
npm install
npm run dev
```

Ouvrez `http://localhost:5173`. Les appels `/api` sont automatiquement redirriges vers le
backend (voir `vite.config.js`).

### 3.3 Application client (necessite que le backend tourne)

```bash
cd client-app
mvn package
java -jar target/newsapp-client-1.0.0-jar-with-dependencies.jar
```

Deroulement :
1. L'appli demande un login/mot de passe → appelle `authenticate` en SOAP.
2. Si le role retourne est `ADMIN`, elle demande un **jeton SOAP** (genere depuis
   Administration → Jetons SOAP dans le site web, ou utilisez le jeton de demo).
3. Menu : lister / ajouter / modifier / supprimer des utilisateurs (100% via SOAP).

### Tester le service REST directement

```bash
curl "http://localhost:9000/api/articles?page=0&size=5"                     # JSON
curl -H "Accept: application/xml" "http://localhost:9000/api/articles?page=0&size=5"  # XML
curl "http://localhost:9000/api/articles/groupes-par-categorie"
curl "http://localhost:9000/api/articles/categorie/1"
```

### Tester le service SOAP directement

Le WSDL est consultable dans un navigateur : `http://localhost:9000/services/users?wsdl`.
Vous pouvez aussi tester avec SoapUI/Postman en pointant sur cette URL.

## 4. Points d'architecture a mentionner dans votre rapport

- **Securite REST (site web)** : JWT stateless, filtre `JwtAuthFilter`, roles Spring Security
  (`hasRole("ADMIN")`, `hasAnyRole("EDITEUR","ADMIN")`).
- **Securite SOAP** : jeton opaque (UUID) genere par un admin et verifie a chaque appel de
  gestion des utilisateurs (`authenticate` seule ne requiert pas de jeton, conformement au sujet).
- **CORS** : configure via `CorsConfigurationSource` dans `SecurityConfig` (a documenter,
  c'est souvent la source d'erreurs 403/CORS si mis ailleurs).
- **REST content negotiation** : le meme endpoint retourne du JSON ou du XML selon l'en-tete
  `Accept` (grace a `jackson-dataformat-xml`), conforme a la demande "au choix de l'utilisateur".
- **3 profils** : visiteur = endpoints publics ; editeur = `/api/editeur/**` ; admin =
  `/api/admin/**` (gestion utilisateurs + jetons).

## 5. Ce qu'il reste a personnaliser avant de rendre

- Le fichier PDF du sujet mentionne une **date limite `[date_à_définir]`** — verifiez-la avec l'enseignant.
- Preparez un **rapport** (souvent demande en plus du code) decrivant l'architecture, les choix
  techniques et des captures d'ecran.
- Completez le fichier `pom.xml` / `package.json` avec les noms exacts de votre groupe si besoin.

## 6. Livrables a rendre (rappel du sujet)

D'apres l'enonce :
- Code source complet (les 3 parties), qualite du code prise en compte dans la notation.
- A envoyer **avant la date limite a 23h59:59** via un lien vers un **depot Git accessible publiquement**.
- Email a `envoitp@gmail.com` avec pour objet : `Projet_AL_Groupe_X_Classe`
  (X = numero du groupe, Classe = DIC2 / MASTER1 / DIT2 selon votre cas).
- Groupe de **3 etudiants maximum**. Le non-respect de ces contraintes rend le travail irrecevable.

### Suggestion de commandes Git pour publier le depot

```bash
cd projet-al
git init
git add .
git commit -m "Projet Architecture Logicielle - Site actualites + SOAP + REST + client Java"
git branch -M main
git remote add origin <URL_DE_VOTRE_DEPOT_GITHUB_OU_GITLAB>
git push -u origin main
```

N'oubliez pas de rendre le depot **public** avant d'envoyer le lien.
