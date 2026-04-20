# LaplateformeTracker

## 📁 Structure du projet

```
LaplateformeTracker/
│
├── src/
│   └── main/
│       └── java/
│           └── com/laplateforme/tracker/
│               │
│               ├── Main.java                    # Point d'entrée
│               │
│               ├── model/
│               │   ├── Student.java             # Entité (POJO)
│               │   ├── User.java                # Gestion authentification
│               │   └── dao/
│               │       ├── StudentDAO.java       # Interface CRUD
│               │       └── StudentDAOImpl.java   # Implémentation JDBC
│               │
│               ├── controller/
│               │   ├── StudentController.java
│               │   ├── SearchController.java
│               │   ├── StatsController.java
│               │   └── AuthController.java
│               │
│               ├── view/
│               │   ├── LoginView.java
│               │   ├── MainView.java
│               │   ├── StudentListView.java
│               │   ├── StudentFormView.java
│               │   └── StatsView.java
│               │
│               ├── service/
│               │   ├── ImportExportService.java  # CSV / JSON / XML
│               │   ├── BackupService.java        # Sauvegarde automatique
│               │   └── AuthService.java
│               │
│               └── util/
│                   ├── DatabaseConnection.java   # Singleton JDBC
│                   └── Validator.java
│
├── resources/
│   ├── fxml/                                    # Interfaces JavaFX
│   └── db/
│       └── init.sql                             # Script de création BDD
│
├── pom.xml                                      # Configuration Maven
└── README.md
```
# LaPlateformeTracker

Application desktop Java de gestion d'etudiants avec interface graphique JavaFX et base de donnees PostgreSQL. Projet realise dans le cadre d'une formation, suivant une architecture MVC stricte.

---

## Technologies

| Categorie | Technologie | Version |
|-----------|-------------|---------|
| Langage | Java | 17 |
| Interface graphique | JavaFX | 21.0.1 |
| Base de donnees | PostgreSQL | 15+ |
| Connexion BDD | JDBC | — |
| Securite mots de passe | BCrypt (jBCrypt) | 0.4 |
| Serialisation JSON | Gson | 2.10.1 |
| Gestion de projet | Maven | 3.x |
| Tests unitaires | JUnit 5 | 5.10.2 |
| Mocks | Mockito | 5.14.2 |
| Couverture de code | JaCoCo | 0.8.12 |



## Fonctionnalites

### Gestion des etudiants
- Ajouter, modifier, supprimer un etudiant
- Detection automatique des doublons (prenom + nom)
- Validation des saisies (age 1-150, note 0-20, email optionnel)

### Recherche et affichage
- Recherche simple par nom ou prenom (LIKE insensible a la casse)
- **Recherche avancee** : ID exact, prenom, nom, email, age min/max, note min/max
- Tri par colonne (nom, prenom, age, note, ID) ASC ou DESC
- Pagination configurable (5 / 10 / 20 / 50 etudiants par page)

### Import / Export
- Export CSV et JSON de la liste complete
- Import CSV avec detection des doublons

### Statistiques
- Moyenne, note max, note min de la promotion
- Graphiques camembert : repartition par tranche d'age et par mention

### Authentification
- Inscription avec regles de securite : 8 caracteres min, 1 majuscule, 1 minuscule, 1 chiffre
- Mots de passe haches avec BCrypt (sel aleatoire unique par utilisateur)
- Connexion / Deconnexion

### Sauvegarde et connexion BDD
- Sauvegarde manuelle depuis le menu (JSON horodate dans `backups/`)
- Sauvegarde automatique proposee a la fermeture
- Fermeture propre de la connexion PostgreSQL au quitter

---

## Prerequis

- Java 17 ou superieur
- Maven 3.6+
- PostgreSQL 15+ avec une base nommee `tracker`

---

## Installation

### 1. Cloner le depot

```bash
git clone https://github.com/willLaPlateforme/LaPlateformeTracker.git
cd LaPlateformeTracker
```

### 2. Configurer la base de donnees

Connectez-vous a PostgreSQL et executez le script SQL :

```bash
psql -U postgres -f init.sql
```

Le fichier `init.sql` cree les tables `student` et `users`.

Si vous souhaitez modifier les identifiants de connexion, editez `DatabaseConnection.java` :

```java
private static final String URL      = "jdbc:postgresql://localhost:5432/tracker";
private static final String USER     = "postgres";
private static final String PASSWORD = "postgres";
```

### 3. Compiler et lancer

```bash
mvn clean javafx:run
```

### 4. Premier lancement

Le hash de mot de passe par defaut dans `init.sql` est incompatible avec BCrypt.  
**Creez votre compte depuis l'ecran d'inscription** (lien "Pas encore de compte ?").

Regles du mot de passe : 8 caracteres minimum, 1 majuscule, 1 minuscule, 1 chiffre.  
Exemple valide : `Admin123`

---

## Tests

### Lancer les tests

```bash
mvn test
```

### Resultats

```
Tests run: 101, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

| Fichier de test | Tests | Ce qui est verifie |
|-----------------|-------|--------------------|
| `ValidatorTest` | 27 | Toutes les regles de validation |
| `StudentTest` | 12 | Constructeurs, getters, setters du POJO |
| `StatsControllerTest` | 16 | Moyenne, max, min, repartitions |
| `AuthControllerTest` | 13 | Login / register avec AuthService mocke |
| `StudentControllerTest` | 15 | CRUD avec DAO mocke (sans BDD) |
| `SearchControllerTest` | 5 | Delegation correcte au DAO |
| `ImportExportServiceTest` | 13 | Export/import CSV et JSON |

### Rapport de couverture JaCoCo

Apres `mvn test`, le rapport HTML est genere dans :

```
target/site/jacoco/index.html
```

Ouvrez ce fichier dans votre navigateur pour voir la couverture ligne par ligne.

---

## Schema de la base de donnees

```sql
CREATE TABLE student (
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    age        INT NOT NULL,
    grade      DOUBLE PRECISION NOT NULL,
    email      VARCHAR(255) NULL
);

CREATE TABLE users (
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL  -- hash BCrypt, jamais le vrai mot de passe
);
```

---

## Securite

**Injection SQL** : toutes les requetes utilisent des `PreparedStatement` avec des placeholders `?`. Les valeurs saisies par l'utilisateur ne sont jamais concatenees dans le SQL.

**Mots de passe** : BCrypt avec sel aleatoire unique par utilisateur. Le mot de passe en clair n'est jamais stocke ni journalise.

**LIKE securise** : le `%` est ajoute cote Java avant injection via `setString()`. L'utilisateur ne peut pas injecter de SQL via les champs de recherche.

---

## Structure des fichiers de configuration

```
LaPlateformeTracker/
├── pom.xml          ← Dependances Maven (JavaFX, PostgreSQL, BCrypt, JUnit, Mockito, JaCoCo)
├── init.sql         ← Script de creation de la base de donnees
├── backups/         ← Sauvegardes JSON (cree automatiquement au premier backup)
└── src/
    ├── main/java/   ← Code source
    └── test/java/   ← Tests unitaires
```

---

## Commandes utiles

```bash
# Lancer l'application
mvn clean javafx:run

# Lancer les tests + generer le rapport JaCoCo
mvn clean test

# Compiler sans lancer
mvn clean compile

# Packager en JAR
mvn clean package
```

---

## Patterns utilises

| Pattern | Ou | Pourquoi |
|---------|-----|---------|
| MVC | Architecture globale | Separation interface / logique / donnees |
| DAO | `StudentDAO` + `StudentDAOImpl` | Tout le SQL isole dans une seule classe |
| Singleton | `DatabaseConnection` | Une seule connexion JDBC pour toute l'application |
| Strategy | `Validator` | Regles de validation centralisees et reutilisables |

---

## Auteur

Projet realise dans le cadre de la formation **LaPlateformeTracker** — 2026.
