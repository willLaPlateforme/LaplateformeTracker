package laplateforme_tracker.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contrôleur de l'écran principal.
 * Gère uniquement la navigation entre les différents panneaux.
 * Les données affichées sont fictives (pour le prototype visuel).
 */
public class MainView {

    // ===== PANNEAUX (un seul visible à la fois) =====
    @FXML private VBox panneauListe;
    @FXML private VBox panneauRecherche;
    @FXML private VBox panneauStatistiques;
    @FXML private VBox panneauImportExport;
    @FXML private VBox panneauSauvegarde;

    // ===== BARRE DU HAUT =====
    @FXML private Label labelUtilisateur;

    // ===== TABLEAU LISTE =====
    @FXML private TableView<String[]> tableEtudiants;
    @FXML private TableColumn<String[], String> colNom;
    @FXML private TableColumn<String[], String> colPrenom;
    @FXML private TableColumn<String[], String> colAge;
    @FXML private TableColumn<String[], String> colMoyenne;
    @FXML private TableColumn<String[], String> colMention;

    // ===== TABLEAU RECHERCHE =====
    @FXML private TableView<String[]> tableRecherche;
    @FXML private TableColumn<String[], String> colRechNom;
    @FXML private TableColumn<String[], String> colRechPrenom;
    @FXML private TableColumn<String[], String> colRechAge;
    @FXML private TableColumn<String[], String> colRechMoyenne;
    @FXML private TableColumn<String[], String> colRechMention;

    // ===== LABELS STATISTIQUES =====
    @FXML private Label labelNbEtudiants;
    @FXML private Label labelMoyenneClasse;
    @FXML private Label labelMeilleureMoyenne;
    @FXML private Label labelMoinsBonneMoyenne;
    @FXML private Label labelNbAvecMoyenne;
    @FXML private Label labelAge1618;
    @FXML private Label labelAge1921;
    @FXML private Label labelAge2225;
    @FXML private Label labelAge26plus;
    @FXML private Label labelMentionTB;
    @FXML private Label labelMentionB;
    @FXML private Label labelMentionAB;
    @FXML private Label labelMentionP;
    @FXML private Label labelMentionI;

    // ===== SAUVEGARDE =====
    @FXML private Label labelStatutSauvegarde;
    @FXML private Label labelDerniereSauvegarde;

    // ===== DONNÉES FICTIVES (à remplacer par les vrais services) =====
    private static final String[][] ETUDIANTS_FICTIFS = {
        {"Dupont",    "Marie",    "20", "15.50", "Bien"},
        {"Martin",    "Lucas",    "22", "12.00", "Assez Bien"},
        {"Bernard",   "Sophie",   "21", "17.25", "Très Bien"},
        {"Moreau",    "Thomas",   "19", "9.50",  "Insuffisant"},
        {"Petit",     "Camille",  "23", "13.75", "Assez Bien"},
        {"Laurent",   "Hugo",     "20", "10.00", "Passable"},
        {"Simon",     "Léa",      "21", "16.00", "Très Bien"},
        {"Michel",    "Antoine",  "24", "11.50", "Passable"},
        {"Leroy",     "Inès",     "19", "14.50", "Bien"},
        {"Roux",      "Baptiste", "22", "8.00",  "Insuffisant"},
    };

    // ===== INITIALISATION =====

    /**
     * Reçoit le nom d'utilisateur depuis LoginController pour l'afficher.
     */
    public void setNomUtilisateur(String nom) {
        labelUtilisateur.setText("Connecté : " + nom);
    }

    /**
     * Appelé automatiquement par JavaFX après le chargement du FXML.
     * Initialise les tableaux et affiche le premier panneau.
     */
    @FXML
    public void initialize() {
        configurerTableaux();
        remplirDonneesFictives();
        remplirStatistiquesFictives();
        // On affiche la liste par défaut au démarrage
        afficherListe();
    }

    // ===== NAVIGATION =====

    /** Affiche le panneau Liste des étudiants */
    @FXML
    public void afficherListe() {
        cacherTousLesPanneaux();
        panneauListe.setVisible(true);
    }

    /** Affiche le panneau Recherche avancée */
    @FXML
    public void afficherRecherche() {
        cacherTousLesPanneaux();
        panneauRecherche.setVisible(true);
    }

    /** Affiche le panneau Statistiques */
    @FXML
    public void afficherStatistiques() {
        cacherTousLesPanneaux();
        panneauStatistiques.setVisible(true);
    }

    /** Affiche le panneau Import / Export */
    @FXML
    public void afficherImportExport() {
        cacherTousLesPanneaux();
        panneauImportExport.setVisible(true);
    }

    /** Affiche le panneau Sauvegarde automatique */
    @FXML
    public void afficherSauvegarde() {
        cacherTousLesPanneaux();
        panneauSauvegarde.setVisible(true);
    }

    /** Retourne à l'écran de connexion */
    @FXML
    public void seDeconnecter() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) labelUtilisateur.getScene().getWindow();
            stage.setScene(new Scene(root, 450, 380));
            stage.setResizable(false);
            stage.setTitle("Gestion Étudiants");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== MÉTHODES PRIVÉES =====

    /** Cache tous les panneaux */
    private void cacherTousLesPanneaux() {
        panneauListe.setVisible(false);
        panneauRecherche.setVisible(false);
        panneauStatistiques.setVisible(false);
        panneauImportExport.setVisible(false);
        panneauSauvegarde.setVisible(false);
    }

    /** Configure les colonnes des deux tableaux */
    private void configurerTableaux() {
        // Tableau principal
        colNom.setCellValueFactory(data     -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colPrenom.setCellValueFactory(data  -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colAge.setCellValueFactory(data     -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        colMoyenne.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
        colMention.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[4]));

        // Tableau recherche
        colRechNom.setCellValueFactory(data     -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colRechPrenom.setCellValueFactory(data  -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colRechAge.setCellValueFactory(data     -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        colRechMoyenne.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
        colRechMention.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[4]));
    }

    /** Remplit les tableaux avec les données fictives */
    private void remplirDonneesFictives() {
        tableEtudiants.setItems(FXCollections.observableArrayList(
                java.util.Arrays.asList(ETUDIANTS_FICTIFS)
        ));
        // Le tableau de recherche affiche les 4 premiers par défaut
        tableRecherche.setItems(FXCollections.observableArrayList(
                java.util.Arrays.asList(ETUDIANTS_FICTIFS).subList(0, 4)
        ));
    }

    /** Remplit les labels de statistiques avec des valeurs fictives */
    private void remplirStatistiquesFictives() {
        labelNbEtudiants.setText("10");
        labelMoyenneClasse.setText("12.80 / 20");
        labelMeilleureMoyenne.setText("17.25 / 20  (Bernard Sophie)");
        labelMoinsBonneMoyenne.setText("8.00 / 20  (Roux Baptiste)");
        labelNbAvecMoyenne.setText("6 étudiants sur 10");

        labelAge1618.setText("16-18 ans : 0 étudiant(s)");
        labelAge1921.setText("19-21 ans : 6 étudiant(s)");
        labelAge2225.setText("22-25 ans : 4 étudiant(s)");
        labelAge26plus.setText("26+ ans   : 0 étudiant(s)");

        labelMentionTB.setText("Très Bien   : 2");
        labelMentionB.setText("Bien        : 2");
        labelMentionAB.setText("Assez Bien  : 2");
        labelMentionP.setText("Passable    : 2");
        labelMentionI.setText("Insuffisant : 2");

        labelStatutSauvegarde.setText("Inactive");
        labelDerniereSauvegarde.setText("Jamais");
    }
}