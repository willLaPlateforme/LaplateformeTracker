# LaplateformeTracker

LaplateformeTracker/
│
├── src/
│   └── main/
│       └── java/
│           └── com/laplateforme/tracker/
│               │
│               ├── Main.java                    ← point d'entrée
│               │
│               ├── model/
│               │   ├── Student.java             ← POJO entité
│               │   ├── User.java                ← authentification
│               │   └── dao/
│               │       ├── StudentDAO.java       ← interface CRUD
│               │       └── StudentDAOImpl.java   ← implémentation JDBC
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
│               │   ├── ImportExportService.java  ← CSV / JSON / XML
│               │   ├── BackupService.java        ← sauvegarde auto
│               │   └── AuthService.java
│               │
│               └── util/
│                   ├── DatabaseConnection.java   ← singleton JDBC
│                   └── Validator.java
│
├── resources/
│   ├── fxml/                                    ← fichiers JavaFX UI
│   └── db/
│       └── init.sql                             ← script création BDD
│
├── pom.xml                                      ← Maven (deps JDBC, JavaFX)
└── README.md
