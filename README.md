# 🏦 Online Banking — Microservices Application

> Application bancaire en ligne basée sur une architecture microservices, construite avec **Spring Boot 3.4** et **Java 21**.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-brightgreen?logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green?logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)

---

## 📋 Table des matières

- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Prérequis](#-prérequis)
- [Démarrage rapide](#-démarrage-rapide)
- [Documentation API](#-documentation-api)
- [Fonctionnalités](#-fonctionnalités)
- [Structure du projet](#-structure-du-projet)
- [Profils](#-profils)

---

## 🏗 Architecture

```
                        ┌─────────────────┐
                        │   API Gateway   │  :8080
                        │  (Spring Cloud) │
                        └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                                     │
     ┌────────▼────────┐               ┌────────────▼────────┐
     │   Auth Service  │  :8081        │ Accounting Service  │  :8082
     │  (JWT / BCrypt) │               │  (Comptes & Tx)     │
     └────────┬────────┘               └────────────┬────────┘
              │                                     │
              └──────────────┬──────────────────────┘
                             │
                    ┌────────▼────────┐
                    │    MongoDB      │  :27017
                    │  (auth_db_test  │
                    │ accounting_db)  │
                    └─────────────────┘

     ┌─────────────────┐       ┌─────────────────┐
     │ Service Discovery│ :8761 │  Config Server  │  :8888
     │    (Eureka)      │       │ (Spring Cloud)  │
     └─────────────────┘       └─────────────────┘
```

### Microservices

| Service | Port | Rôle |
|---|---|---|
| **API Gateway** | `8080` | Point d'entrée unique, routage des requêtes |
| **Auth Service** | `8081` | Authentification, autorisation, JWT |
| **Accounting Service** | `8082` | Gestion des comptes et transactions |
| **Config Server** | `8888` | Configurations centralisées par profil |
| **Service Discovery** | `8761` | Registre Eureka des microservices |
| **MongoDB** | `27017` | Persistance des données |
| **Mongo Express** | `9082` | Interface web d'administration |

### Module commun

Le module `common` est une bibliothèque partagée entre les services contenant les DTOs, filtres JWT, utilitaires et classes de sécurité.

---

## 💻 Technologies

| Catégorie | Technologie | Version |
|---|---|---|
| Langage | Java | 21 |
| Framework | Spring Boot | 3.4.1 |
| Cloud | Spring Cloud | 2024.0.0 |
| Sécurité | Spring Security + JWT (jjwt) | 0.11.5 |
| Base de données | MongoDB | 6.0 |
| Conteneurisation | Docker & Docker Compose | — |
| Build | Maven | 3.6+ |
| Documentation | SpringDoc OpenAPI | 2.7.0 |

---

## 🔧 Prérequis

- **Java 21+**
- **Maven 3.6+**
- **Docker** et **Docker Compose**

---

## 🚀 Démarrage rapide

### 1. Cloner le repository

```bash
git clone <url-du-repo>
cd mono_repo_ms
```

### 2. Créer le fichier `.env`

À la racine du projet, créer un fichier `.env` :

```env
JWT_SECRET=OnlineBankSuperSecretKeyForJWTSigning2024MinLength32Chars
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000
```

> ⚠️ Ne jamais committer ce fichier. Il est listé dans `.gitignore`.

### 3. Lancer avec Docker Compose

```bash
docker compose up --build
```

L'ordre de démarrage est géré automatiquement via les healthchecks :

```
MongoDB → Service Discovery → Config Server → Auth Service → Accounting Service → Gateway
```

### 4. Arrêter

```bash
# Arrêter les containers
docker compose down

# Arrêter et supprimer les volumes (repart de zéro avec MongoDB)
docker compose down -v
```

### Commandes utiles

```bash
# Voir les logs d'un service spécifique
docker compose logs -f auth-service

# Rebuild un seul service sans cache
docker compose build --no-cache auth-service
docker compose up auth-service
```

---

## 📖 Documentation API

Une fois l'application lancée, Swagger est disponible pour chaque service.

### Accès direct par service

| Service | URL |
|---|---|
| 🔐 Auth Service | [http://localhost:8081/api/auth/swagger-ui.html](http://localhost:8081/api/auth/swagger-ui.html) |
| 💰 Accounting Service | [http://localhost:8082/api/accounting/swagger-ui.html](http://localhost:8082/api/accounting/swagger-ui.html) |

### Accès agrégé via la Gateway

La Gateway expose un Swagger unique qui agrège tous les services :

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Les sources agrégées disponibles dans le sélecteur Swagger :

| Onglet | Source |
|---|---|
| Auth Service | `http://localhost:8080/api/auth/v3/api-docs` |
| Accounting Service | `http://localhost:8080/api/accounting/v3/api-docs` |

### Autres interfaces

| Interface | URL |
|---|---|
| 🗂 Eureka Dashboard | [http://localhost:8761](http://localhost:8761) |
| 🍃 Mongo Express | [http://localhost:9082](http://localhost:9082) |
| ⚙️ Config Server health | [http://localhost:8888/actuator/health](http://localhost:8888/actuator/health) |
| ❤️ Auth Service health | [http://localhost:8081/api/auth/actuator/health](http://localhost:8081/api/auth/actuator/health) |
| ❤️ Accounting Service health | [http://localhost:8082/api/accounting/actuator/health](http://localhost:8082/api/accounting/actuator/health) |

> ⚠️ Swagger est **désactivé en profil `prod`** pour tous les services.

---

## ✨ Fonctionnalités

- 🔐 **Authentification JWT** — inscription, connexion, refresh token
- 👤 **Gestion des utilisateurs** — création et gestion des comptes utilisateurs
- 💰 **Gestion des comptes bancaires** — ouverture de comptes, soldes, transactions
- 🌐 **API Gateway** — routage centralisé et load balancing via Eureka
- ⚙️ **Config Server** — propriétés externalisées par profil (`local`, `test`, `prod`)
- 🔍 **Service Discovery** — résilience et scalabilité avec Eureka
- 📊 **Actuator** — endpoints de monitoring (`/actuator/health`, `/actuator/metrics`)
- 📚 **Swagger agrégé** — documentation interactive centralisée via la Gateway

---

## 🗂 Structure du projet

```
mono_repo_ms/
├── common/                        # Bibliothèque partagée (DTOs, JWT filter, utils)
│   └── src/main/java/
├── discoveryService/              # Eureka Server  :8761
│   └── src/main/java/
├── configService/                 # Spring Cloud Config Server  :8888
│   └── src/main/resources/
│       └── config/                # Fichiers de config par service et profil
│           ├── authService-local.properties
│           ├── authService-test.properties
│           ├── authService-prod.properties
│           ├── accountingService-local.properties
│           ├── accountingService-test.properties
│           ├── accountingService-prod.properties
│           ├── gatewayService-local.properties
│           ├── gatewayService-test.properties
│           └── gatewayService-prod.properties
├── gatewayService/                # Spring Cloud Gateway  :8080
│   └── src/main/java/
├── authService/                   # Service d'authentification  :8081
│   └── src/main/java/
├── accountingService/             # Service de comptabilité  :8082
│   └── src/main/java/
├── docker-compose.yml             # Orchestration Docker
├── mongo-init.js                  # Script d'initialisation MongoDB
├── .env                           # Variables d'environnement (non commité)
└── README.md
```

---

## 🌍 Profils

| Profil | Usage | MongoDB | Eureka | Swagger |
|---|---|---|---|---|
| `local` | Développement local | `localhost:27017` | `localhost:8761` | ✅ activé |
| `test` | Docker Compose | `mongodb:27017` | `service-discovery:8761` | ✅ activé |
| `prod` | Production | `mongodb:27017` | `service-discovery:8761` | ❌ désactivé |

Le profil actif est défini via la variable d'environnement `PROFIL` (défaut : `local`).

```bash
# Exemple pour lancer en profil test via Docker
PROFIL=test docker compose up
```
