package com.example.farmaciebdproiect;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode; // <-- IMPORT NOU
import javafx.scene.input.KeyEvent; // <-- IMPORT NOU
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Angajat angajatLogat;

    // --- FXML PENTRU STIL ---
    @FXML
    private VBox loginContainer; // fx:id="loginContainer" (pe VBox-ul principal)
    @FXML
    private Label titluLogin; // fx:id="titluLogin" (pe textul "Autentificare")
    // ------------------------

    @FXML
    private TextField numeField;

    @FXML
    private PasswordField parafaField;

    @FXML
    private Button loginButton;

    @FXML
    private Label mesajLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (loginContainer != null) {
            loginContainer.getStyleClass().add("login-container");
        }
        if (titluLogin != null) {
            titluLogin.getStyleClass().add("header-title");
        }

        // Setam pictogramele
        if (loginButton != null) {
            loginButton.setGraphic(createIcon("Images/icons8-man-50.png", 16));
            loginButton.setId("loginButton"); // Setam ID pentru CSS
        }

        // --- BLOC NOU: Adaugare "Enter" pentru login ---

        if (numeField != null) {
            numeField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    parafaField.requestFocus();
                }
            });
        }

        if (parafaField != null) {
            parafaField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            });
        }
    }

    @FXML
    protected void onLoginButtonClick() {
        String nume = numeField.getText();
        String parafa = parafaField.getText();

        if (nume.isEmpty() || parafa.isEmpty()) {
            mesajLabel.setTextFill(Color.RED);
            mesajLabel.setText("Numele si codul parafa sunt obligatorii!");
            return;
        }

        // --- Logica de conectare mutata pe un fir separat ---

        loginButton.setDisable(true);
        mesajLabel.setTextFill(Color.BLUE);
        mesajLabel.setText("Verificare...");

        Task<Angajat> loginTask = new Task<Angajat>() {
            @Override
            protected Angajat call() throws SQLException {
                return validateLogin(nume, parafa);
            }
        };

        loginTask.setOnSucceeded(event -> {
            angajatLogat = loginTask.getValue();

            if (angajatLogat != null) {
                mesajLabel.setTextFill(Color.GREEN);
                mesajLabel.setText("Login reusit! Bine ai venit, " + angajatLogat.getNume() + "!");

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard-view.fxml"));
                    Parent dashboardRoot = fxmlLoader.load();

                    DashboardController dashboardController = fxmlLoader.getController();
                    dashboardController.initData(angajatLogat);

                    Stage dashboardStage = new Stage();
                    dashboardStage.setTitle("Gestiune Farmacie");

                    // Incarcam CSS-ul si in fereastra noua
                    Scene dashboardScene = new Scene(dashboardRoot, 1200, 800);
                    dashboardScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                    dashboardStage.setScene(dashboardScene);

                    dashboardStage.setMaximized(true); // Deschide maximizat
                    dashboardStage.show();

                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mesajLabel.setTextFill(Color.RED);
                mesajLabel.setText("Nume sau cod parafa incorect!");
            }
            loginButton.setDisable(false);
        });

        loginTask.setOnFailed(event -> {
            mesajLabel.setTextFill(Color.RED);
            mesajLabel.setText("Eroare de conexiune la baza de date!");
            loginButton.setDisable(false);
            loginTask.getException().printStackTrace();
        });

        new Thread(loginTask).start();
    }


    private Angajat validateLogin(String nume, String codParafa) throws SQLException {
        String sqlQuery = "SELECT AngajatID, Nume, Prenume, CodParafa FROM Angajati WHERE Nume = ? AND CodParafa = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, nume);
            pstmt.setString(2, codParafa);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Angajat(
                            rs.getInt("AngajatID"),
                            rs.getString("Nume"),
                            rs.getString("Prenume"),
                            rs.getString("CodParafa")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    private ImageView createIcon(String path, int size) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(size);
            imgView.setFitWidth(size);
            imgView.setPreserveRatio(true);
            return imgView;
        } catch (Exception e) {
            System.err.println("Nu pot încărca imaginea: " + path);
            e.printStackTrace();
            return null;
        }
    }
}