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

