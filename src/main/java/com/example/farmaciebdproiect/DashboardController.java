package com.example.farmaciebdproiect;

// IMPORTURILE NECESARE
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Pair;
import javafx.scene.control.DatePicker; // IMPORT NOU
import javafx.scene.control.ComboBox; // IMPORT NOU
import javafx.util.StringConverter; // IMPORT NOU

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {

    // --- FXML PENTRU STIL ---
    @FXML private Label titluStocCurent;
    @FXML private Label titluStocDisponibil;
    @FXML private Label titluBonCurent;
    @FXML private Label titluProduseCritice;
    @FXML private Label titluFurnizoriDisponibili;
    @FXML private Label titluListaFurnizori;
    @FXML private Label titluListaAngajati;

    // Tab-uri
    @FXML private Tab stocuriTab;
    @FXML private Tab comenziTab;
    @FXML private Tab vanzariTab;
    @FXML private Tab furnizoriTab;
    @FXML private Tab angajatiTab;
    // ------------------------


    // BLOCUL DE COMENZI
    private final ObservableList<ComandaProdus> comenziProduseList = FXCollections.observableArrayList();
    private final ObservableList<ComandaFurnizor> comenziFurnizoriList = FXCollections.observableArrayList();
    @FXML
    private TableView<ComandaProdus> comenziProduseTable;
    @FXML
    private TableView<ComandaFurnizor> comenziFurnizoriTable;
    @FXML
    private TableColumn<ComandaProdus, String> colComandaNume;
    @FXML
    private TableColumn<ComandaProdus, Integer> colComandaStoc;
    @FXML
    private TableColumn<ComandaFurnizor, String> colComandaFurnizor;
    @FXML
    private TableColumn<ComandaFurnizor, Integer> colComandaTimp;
    @FXML
    private Button plaseazaComandaButton;
    private boolean comenziDataLoaded = false;


    private final ObservableList<Angajat> angajatiList = FXCollections.observableArrayList();
    private final ObservableList<Furnizor> furnizoriList = FXCollections.observableArrayList();
    private final ObservableList<StocView> stocuriList = FXCollections.observableArrayList();
    private final ObservableList<BonItem> bonCurentList = FXCollections.observableArrayList();
    private final ObservableList<StocView> stocVanzariList = FXCollections.observableArrayList();
    private Angajat angajatCurent;

    // --- Fila Angajati ---
    @FXML
    private TableView<Angajat> angajatiTable;
    private boolean angajatiDataLoaded = false;

    // --- Fila Furnizori ---
    @FXML
    private TableView<Furnizor> furnizoriTable;
    private boolean furnizoriDataLoaded = false;
    @FXML
    private Button adaugaFurnizorButton;
    @FXML
    private Button stergeFurnizorButton;

    // --- Fila Stocuri ---
    @FXML
    private TableView<StocView> stocuriTable;
    private boolean stocuriDataLoaded = false;
    @FXML
    private Button adaugaLotButton; // BUTONUL NOU PENTRU STOC

    // --- Fila VANZARI ---
    @FXML
    private TableView<StocView> stocVanzariTable;
    @FXML
    private TableColumn<StocView, String> colStocNume;
    @FXML
    private TableColumn<StocView, String> colStocLot;
    @FXML
    private TableColumn<StocView, Integer> colStocCant;
    @FXML
    private TableColumn<StocView, Double> colStocPret;
    @FXML
    private TableColumn<StocView, String> colStocExp;
    @FXML
    private TextField cantitateVanzareField;
    @FXML
    private Button adaugaPeBonButton;
    @FXML
    private TableView<BonItem> bonCurentTable;
    @FXML
    private TableColumn<BonItem, String> colBonNume;
    @FXML
    private TableColumn<BonItem, Integer> colBonCant;
    @FXML
    private TableColumn<BonItem, Double> colBonPretU;
    @FXML
    private TableColumn<BonItem, Double> colBonPretT;
    @FXML
    private Label totalBonLabel;
    @FXML
    private Button stergeProdusButton;
    @FXML
    private Button finalizeazaVanzareButton;
    private boolean stocVanzariDataLoaded = false;

    // --- Fila Medicamente ---
    private final ObservableList<Medicament> medicamenteList = FXCollections.observableArrayList();
    private boolean medicamenteDataLoaded = false;
    @FXML private Label titluListaMedicamente;
    @FXML private TableView<Medicament> medicamenteTable;
    @FXML private TableColumn<Medicament, Integer> colMedID;
    @FXML private TableColumn<Medicament, String> colMedNume;
    @FXML private TableColumn<Medicament, String> colMedDozaj;
    @FXML private Button adaugaMedicamentButton;
    @FXML private Button stergeMedicamentButton;

    // Metoda primeste angajatul de la LoginController
    public void initData(Angajat angajat) {
        this.angajatCurent = angajat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // --- BLOC STILURI SI PICTOGRAME ---

        // 1. Setăm pictogramele pe Tab-uri
        if (stocuriTab != null) stocuriTab.setGraphic(createIcon("Images/icons8-pills-50.png", 20));
        if (comenziTab != null) comenziTab.setGraphic(createIcon("Images/icons8-truck.gif", 20));
        if (vanzariTab != null) vanzariTab.setGraphic(createIcon("Images/icons8-cart.gif", 20));
        if (furnizoriTab != null) furnizoriTab.setGraphic(createIcon("Images/icons8-truck.gif", 20));
        if (angajatiTab != null) angajatiTab.setGraphic(createIcon("Images/icons8-man-50.png", 20));

        // 2. Setăm ID-uri si pictograme pe butoane
        if (adaugaPeBonButton != null) {
            adaugaPeBonButton.setId("butonAdauga");
            adaugaPeBonButton.setGraphic(createIcon("Images/icons8-add-50.png", 16));
        }
        if (stergeProdusButton != null) {
            stergeProdusButton.setId("butonSterge");
            stergeProdusButton.setGraphic(createIcon("Images/icons8-delete-50.png", 16));
        }
        if (finalizeazaVanzareButton != null) {
            finalizeazaVanzareButton.setId("butonFinalizeaza");
            finalizeazaVanzareButton.setGraphic(createIcon("Images/icons8-check.gif", 16));
        }
        if (plaseazaComandaButton != null) {
            plaseazaComandaButton.setId("butonComanda");
            plaseazaComandaButton.setGraphic(createIcon("Images/icons8-truck.gif", 16));
        }
        if (adaugaFurnizorButton != null) {
            adaugaFurnizorButton.setId("butonAdauga");
            adaugaFurnizorButton.setGraphic(createIcon("Images/icons8-add-50.png", 16));
        }
        if (stergeFurnizorButton != null) {
            stergeFurnizorButton.setId("butonSterge");
            stergeFurnizorButton.setGraphic(createIcon("Images/icons8-delete-50.png", 16));
        }
        if (adaugaMedicamentButton != null) {
            adaugaMedicamentButton.setId("butonAdauga");
            adaugaMedicamentButton.setGraphic(createIcon("Images/icons8-add-50.png", 16));
        }
        if (stergeMedicamentButton != null) {
            stergeMedicamentButton.setId("butonSterge");
            stergeMedicamentButton.setGraphic(createIcon("Images/icons8-delete-50.png", 16));
        }
        // BUTONUL NOU
        if (adaugaLotButton != null) {
            adaugaLotButton.setId("butonAdauga"); // Stil verde
            adaugaLotButton.setGraphic(createIcon("Images/icons8-add-50.png", 16));
        }


        // 3. Setăm clasa de stil pe titluri
        if (titluStocCurent != null) titluStocCurent.getStyleClass().add("header-title");
        if (titluStocDisponibil != null) titluStocDisponibil.getStyleClass().add("header-title");
        if (titluBonCurent != null) titluBonCurent.getStyleClass().add("header-title");
        if (titluProduseCritice != null) titluProduseCritice.getStyleClass().add("header-title");
        if (titluFurnizoriDisponibili != null) titluFurnizoriDisponibili.getStyleClass().add("header-title");
        if (titluListaFurnizori != null) titluListaFurnizori.getStyleClass().add("header-title");
        if (titluListaAngajati != null) titluListaAngajati.getStyleClass().add("header-title");
        if (titluListaMedicamente != null) titluListaMedicamente.getStyleClass().add("header-title");

        // --- SFARSIT BLOC STILURI ---


        // --- Setup Fila Angajati ---
        if (angajatiTab != null) {
            angajatiTab.setOnSelectionChanged(event -> {
                if (angajatiTab.isSelected() && !angajatiDataLoaded) {
                    System.out.println("Se incarca datele angajatilor...");
                    loadAngajatiData();
                    angajatiDataLoaded = true;
                }
            });
        }

        // --- Setup Fila Furnizori ---
        if (furnizoriTab != null) {
            furnizoriTab.setOnSelectionChanged(event -> {
                if (furnizoriTab.isSelected() && !furnizoriDataLoaded) {
                    System.out.println("Se incarca datele furnizorilor...");
                    loadFurnizoriData();
                    furnizoriDataLoaded = true;
                }
            });
        }


        // --- Setup Fila Stocuri ---
        if (stocuriTable != null) {
            System.out.println("Se incarca datele de stoc...");
            loadStocuriData(stocuriTable, stocuriList);
            stocuriDataLoaded = true;
        }

        // --- Setup Fila Medicamente ---
        if (medicamenteTable != null) {
            colMedID.setCellValueFactory(new PropertyValueFactory<>("medicamentID"));
            colMedNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
            colMedDozaj.setCellValueFactory(new PropertyValueFactory<>("dozaj"));
            medicamenteTable.setItems(medicamenteList);

            // Incarcam datele o singura data la inceput
            if (!medicamenteDataLoaded) {
                loadMedicamenteData();
                medicamenteDataLoaded = true;
            }
        }


        // --- Setup Fila VANZARI ---
        if (vanzariTab != null) {
            setupColoaneVanzari();
            vanzariTab.setOnSelectionChanged(event -> {
                if (vanzariTab.isSelected() && !stocVanzariDataLoaded) {
                    System.out.println("Se incarca datele de stoc pentru vanzari...");
                    loadStocuriData(stocVanzariTable, stocVanzariList);
                    stocVanzariDataLoaded = true;
                } else if (vanzariTab.isSelected()) {
                    // Re-incarcam stocul in caz ca s-a facut o receptie
                    stocVanzariList.setAll(stocuriList);
                }
            });
            bonCurentTable.setItems(bonCurentList);
        }

        // --- Setup Fila COMENZI (MODIFICATĂ PENTRU A NU SE BLOCA) ---
        if (comenziTab != null) {
            colComandaNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
            colComandaStoc.setCellValueFactory(new PropertyValueFactory<>("totalStoc"));
            colComandaFurnizor.setCellValueFactory(new PropertyValueFactory<>("numeFurnizor"));
            colComandaTimp.setCellValueFactory(new PropertyValueFactory<>("timpLivrare"));

            comenziProduseTable.setItems(comenziProduseList);
            comenziFurnizoriTable.setItems(comenziFurnizoriList);

            comenziTab.setOnSelectionChanged(event -> {
                if (comenziTab.isSelected() && !comenziDataLoaded) {
                    System.out.println("Se incarca datele pentru comenzi...");
                    loadComenziProduseData();
                    comenziDataLoaded = true;
                }
            });

            // --- FIX-UL PENTRU DEBLOCARE ---
            comenziProduseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Task<ObservableList<ComandaFurnizor>> loadFurnizoriTask = new Task<>() {
                        @Override
                        protected ObservableList<ComandaFurnizor> call() throws Exception {
                            return getFurnizoriPentruProdus(newSelection.getMedicamentID());
                        }
                    };
                    loadFurnizoriTask.setOnSucceeded(e -> {
                        comenziFurnizoriList.setAll(loadFurnizoriTask.getValue());
                    });
                    loadFurnizoriTask.setOnFailed(e -> {
                        loadFurnizoriTask.getException().printStackTrace();
                        afiseazaAlerta("Eroare Bază Date", null, "Nu s-au putut încărca furnizorii.", Alert.AlertType.ERROR);
                    });
                    new Thread(loadFurnizoriTask).start();
                } else {
                    comenziFurnizoriList.clear();
                }
            });
        }
    }


    // Functie de setup pentru coloanele din Fila Vanzari
    private void setupColoaneVanzari() {
        colStocNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
        colStocLot.setCellValueFactory(new PropertyValueFactory<>("codLot"));
        colStocCant.setCellValueFactory(new PropertyValueFactory<>("cantitateStoc"));
        colStocPret.setCellValueFactory(new PropertyValueFactory<>("pretVanzare"));
        colStocExp.setCellValueFactory(new PropertyValueFactory<>("dataExpirare"));

        colBonNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
        colBonCant.setCellValueFactory(new PropertyValueFactory<>("cantitateVanduta"));
        colBonPretU.setCellValueFactory(new PropertyValueFactory<>("pretUnitar"));
        colBonPretT.setCellValueFactory(new PropertyValueFactory<>("pretTotal"));
    }


    // --- METODE DE INCARCARE DATE ---

    private void loadStocuriData(TableView<StocView> table, ObservableList<StocView> list) {
        list.clear();
        StringBuilder notificariStoc = new StringBuilder();
        String sqlQuery = "SELECT m.MedicamentID, m.NumeMedicament, m.Dozaj, s.LotID, s.CodLot, s.CantitateStoc, s.DataExpirare, s.PretVanzare " +
                "FROM StocLoturi s " +
                "JOIN Medicamente m ON s.MedicamentID = m.MedicamentID " +
                "WHERE s.CantitateStoc > 0 " +
                "ORDER BY s.DataExpirare ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                int cantitate = rs.getInt("CantitateStoc");
                String numeMedicament = rs.getString("NumeMedicament");

                if (cantitate < 10) {
                    notificariStoc.append("- ").append(numeMedicament)
                            .append(" (Lot: ").append(rs.getString("CodLot"))
                            .append(") are doar ").append(cantitate).append(" bucati ramase.\n");
                }

                LocalDate dataExpirare = rs.getDate("DataExpirare").toLocalDate();
                StocView stoc = new StocView(
                        rs.getInt("LotID"),
                        rs.getString("NumeMedicament"),
                        rs.getString("Dozaj"),
                        rs.getString("CodLot"),
                        cantitate,
                        dataExpirare,
                        rs.getDouble("PretVanzare")
                );
                list.add(stoc);
            }
            table.setItems(list);

            // Dupa ce incarcam stocul principal, il copiem si in cel de vanzari
            if (table == stocuriTable) {
                stocVanzariList.setAll(list);
            }

            if (!notificariStoc.isEmpty()) {
                if (table == stocuriTable) {
                    afiseazaAlerta("Notificari Stoc Scazut", "Urmatoarele produse sunt pe terminate:", notificariStoc.toString(), Alert.AlertType.WARNING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare la incarcarea datelor de stoc.");
        }
    }


    private void loadComenziProduseData() {
        comenziProduseList.clear();
        String sqlQuery = "SELECT m.MedicamentID, m.NumeMedicament, SUM(s.CantitateStoc) AS TotalStoc " +
                "FROM Medicamente m " +
                "JOIN StocLoturi s ON m.MedicamentID = s.MedicamentID " +
                "WHERE s.CantitateStoc > 0 " +
                "GROUP BY m.MedicamentID, m.NumeMedicament " +
                "HAVING SUM(s.CantitateStoc) < 10 " +
                "ORDER BY TotalStoc ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                comenziProduseList.add(new ComandaProdus(
                        rs.getInt("MedicamentID"),
                        rs.getString("NumeMedicament"),
                        rs.getInt("TotalStoc")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare la incarcarea produselor pentru comanda.");
        }
    }

    private ObservableList<ComandaFurnizor> getFurnizoriPentruProdus(int medicamentID) throws SQLException {
        ObservableList<ComandaFurnizor> furnizoriLocali = FXCollections.observableArrayList();
        String sqlQuery = "SELECT f.NumeFurnizor, mf.TimpLivrareEstimat " +
                "FROM Furnizori f " +
                "JOIN MedicamenteFurnizori mf ON f.FurnizorID = mf.FurnizorID " +
                "WHERE mf.MedicamentID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, medicamentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    furnizoriLocali.add(new ComandaFurnizor(
                            rs.getString("NumeFurnizor"),
                            rs.getInt("TimpLivrareEstimat")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare la incarcarea furnizorilor pentru produs.");
            throw new SQLException("Eroare la incarcarea furnizorilor.", e);
        }
        return furnizoriLocali;
    }


    @FXML
    private void onPlaseazaComandaClick() {
        ComandaProdus produsSelectat = comenziProduseTable.getSelectionModel().getSelectedItem();
        ComandaFurnizor furnizorSelectat = comenziFurnizoriTable.getSelectionModel().getSelectedItem();

        if (produsSelectat == null) {
            afiseazaAlerta("Eroare Comanda", null, "Va rugam selectati un PRODUS de comandat.", Alert.AlertType.WARNING);
            return;
        }
        if (furnizorSelectat == null) {
            afiseazaAlerta("Eroare Comanda", null, "Va rugam selectati un FURNIZOR de la care sa comandati.", Alert.AlertType.WARNING);
            return;
        }

        afiseazaAlerta("Comanda Plasata (Simulare)",
                "Comanda a fost generata cu succes!",
                "Produs: " + produsSelectat.getNumeMedicament() + "\n" +
                        "Furnizor: " + furnizorSelectat.getNumeFurnizor() + "\n" +
                        "Timp estimat: " + furnizorSelectat.getTimpLivrare() + " zile.",
                Alert.AlertType.INFORMATION);
    }


    private void loadAngajatiData() {
        angajatiList.clear();
        String sqlQuery = "SELECT AngajatID, Nume, Prenume, CodParafa FROM Angajati";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                Angajat angajat = new Angajat(
                        rs.getInt("AngajatID"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getString("CodParafa")
                );
                angajatiList.add(angajat);
            }
            angajatiTable.setItems(angajatiList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadFurnizoriData() {
        furnizoriList.clear();
        String sqlQuery = "SELECT FurnizorID, NumeFurnizor, CUI, Telefon FROM Furnizori";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                Furnizor furnizor = new Furnizor(
                        rs.getInt("FurnizorID"),
                        rs.getString("NumeFurnizor"),
                        rs.getString("CUI"),
                        rs.getString("Telefon")
                );
                furnizoriList.add(furnizor);
            }
            furnizoriTable.setItems(furnizoriList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --- METODE PENTRU FILA VANZARI ---

    @FXML
    private void onAdaugaPeBonClick() {
        StocView produsSelectat = stocVanzariTable.getSelectionModel().getSelectedItem();
        if (produsSelectat == null) {
            afiseazaAlerta("Eroare", null, "Nu ati selectat niciun produs din stoc!", Alert.AlertType.WARNING);
            return;
        }

        int cantitateDeVandut;
        try {
            cantitateDeVandut = Integer.parseInt(cantitateVanzareField.getText());
            if (cantitateDeVandut <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            afiseazaAlerta("Eroare", null, "Cantitatea trebuie sa fie un numar pozitiv!", Alert.AlertType.WARNING);
            return;
        }

        if (cantitateDeVandut > produsSelectat.getCantitateStoc()) {
            afiseazaAlerta("Stoc Insuficient", null, "Stoc disponibil: " + produsSelectat.getCantitateStoc() + ". Ati cerut: " + cantitateDeVandut, Alert.AlertType.WARNING);
            return;
        }

        for (BonItem item : bonCurentList) {
            if (item.getLotID() == produsSelectat.getLotID()) {
                int cantitateNoua = item.getCantitateVanduta() + cantitateDeVandut;
                if (cantitateNoua > produsSelectat.getCantitateStoc()) {
                    afiseazaAlerta("Stoc Insuficient", null, "Stoc total disponibil: " + produsSelectat.getCantitateStoc() + ".", Alert.AlertType.WARNING);
                    return;
                }
                item.setCantitateVanduta(cantitateNoua);
                bonCurentTable.refresh();
                calculeazaTotalBon();
                cantitateVanzareField.clear();
                return;
            }
        }

        BonItem itemNou = new BonItem(
                produsSelectat.getLotID(),
                produsSelectat.getNumeMedicament(),
                cantitateDeVandut,
                produsSelectat.getPretVanzare(),
                produsSelectat.getCantitateStoc()
        );
        bonCurentList.add(itemNou);
        calculeazaTotalBon();
        cantitateVanzareField.clear();
    }


    @FXML
    private void onStergeProdusClick() {
        BonItem produsSelectat = bonCurentTable.getSelectionModel().getSelectedItem();
        if (produsSelectat == null) {
            afiseazaAlerta("Eroare", null, "Nu ati selectat niciun produs de sters de pe bon!", Alert.AlertType.WARNING);
            return;
        }
        bonCurentList.remove(produsSelectat);
        calculeazaTotalBon();
    }


    @FXML
    private void onFinalizeazaVanzareClick() {
        if (bonCurentList.isEmpty()) {
            afiseazaAlerta("Eroare", null, "Bonul este gol!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Finalizare Vanzare");
        confirm.setHeaderText("Finalizati vanzarea in valoare de " + totalBonLabel.getText() + "?");
        confirm.setContentText("Stocul va fi actualizat. Aceasta actiune este ireversibila.");

        Optional<ButtonType> rezultat = confirm.showAndWait();
        if (rezultat.isPresent() && rezultat.get() == ButtonType.OK) {
            if (proceseazaVanzareaInBD()) {
                afiseazaAlerta("Succes", "Vanzarea a fost inregistrata cu succes!", "", Alert.AlertType.INFORMATION);
                bonCurentList.clear();
                calculeazaTotalBon();
                // Reincarcam ambele liste de stoc
                loadStocuriData(stocuriTable, stocuriList);
                stocVanzariList.setAll(stocuriList); // Actualizam si lista de vanzari
            } else {
                afiseazaAlerta("Eroare Baza de Date", "A aparut o eroare la salvarea vanzarii. Verificati consola.", "", Alert.AlertType.ERROR);
            }
        }
    }

    // --- METODE PENTRU FILA FURNIZORI ---

    @FXML
    private void onAdaugaFurnizorClick() {
        Dialog<Furnizor> dialog = new Dialog<>();
        dialog.setTitle("Adaugă Furnizor Nou");
        dialog.setHeaderText("Introduceți detaliile noului furnizor.");

        ButtonType okButtonType = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField numeField = new TextField();
        numeField.setPromptText("Nume Furnizor");
        TextField cuiField = new TextField();
        cuiField.setPromptText("CUI (ex: RO123456)");
        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon");

        grid.add(new Label("Nume:"), 0, 0);
        grid.add(numeField, 1, 0);
        grid.add(new Label("CUI:"), 0, 1);
        grid.add(cuiField, 1, 1);
        grid.add(new Label("Telefon:"), 0, 2);
        grid.add(telefonField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (numeField.getText().isEmpty() || cuiField.getText().isEmpty()) {
                    afiseazaAlerta("Eroare", null, "Numele și CUI-ul sunt obligatorii!", Alert.AlertType.ERROR);
                    return null;
                }
                return new Furnizor(0, numeField.getText(), cuiField.getText(), telefonField.getText());
            }
            return null;
        });

        Optional<Furnizor> result = dialog.showAndWait();

        result.ifPresent(furnizorNou -> {
            Task<Furnizor> insertTask = new Task<>() {
                @Override
                protected Furnizor call() throws Exception {
                    String sql = "INSERT INTO Furnizori (NumeFurnizor, CUI, Telefon) VALUES (?, ?, ?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                        pstmt.setString(1, furnizorNou.getNumeFurnizor());
                        pstmt.setString(2, furnizorNou.getCui());
                        pstmt.setString(3, furnizorNou.getTelefon());
                        pstmt.executeUpdate();

                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                furnizorNou.setFurnizorID(generatedKeys.getInt(1));
                                return furnizorNou;
                            } else {
                                throw new SQLException("Eroare la obținerea ID-ului pentru furnizor.");
                            }
                        }
                    }
                }
            };

            insertTask.setOnSucceeded(e -> {
                furnizoriList.add(insertTask.getValue());
                afiseazaAlerta("Succes", null, "Furnizorul a fost adăugat.", Alert.AlertType.INFORMATION);
            });
            insertTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare Bază Date", null, "Eroare la adăugarea furnizorului: " + e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(insertTask).start();
        });
    }


    @FXML
    private void onStergeFurnizorClick() {
        Furnizor furnizorSelectat = furnizoriTable.getSelectionModel().getSelectedItem();

        if (furnizorSelectat == null) {
            afiseazaAlerta("Eroare", null, "Vă rugăm selectați un furnizor din tabel.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare Ștergere");
        confirm.setHeaderText("Sunteți sigur că doriți să ștergeți furnizorul: " + furnizorSelectat.getNumeFurnizor() + "?");
        confirm.setContentText("Acțiunea va eșua dacă furnizorul este încă asociat cu medicamente.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    String checkSql = "SELECT COUNT(*) FROM MedicamenteFurnizori WHERE FurnizorID = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmtCheck = conn.prepareStatement(checkSql)) {

                        pstmtCheck.setInt(1, furnizorSelectat.getFurnizorID());
                        try (ResultSet rs = pstmtCheck.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                throw new Exception("Ștergere Blocată! Furnizorul este asociat cu " + rs.getInt(1) + " medicamente.");
                            }
                        }
                    }

                    String deleteSql = "DELETE FROM Furnizori WHERE FurnizorID = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {

                        pstmtDelete.setInt(1, furnizorSelectat.getFurnizorID());
                        int rowsAffected = pstmtDelete.executeUpdate();
                        if (rowsAffected == 0) {
                            throw new Exception("Niciun rând nu a fost șters (eroare necunoscută).");
                        }
                    }
                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                furnizoriList.remove(furnizorSelectat);
                afiseazaAlerta("Succes", null, "Furnizorul a fost șters.", Alert.AlertType.INFORMATION);
            });
            deleteTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare la Ștergere", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(deleteTask).start();
        }
    }

    // --- METODE PENTRU FILA MEDICAMENTE ---

    private void loadMedicamenteData() {
        medicamenteList.clear();
        String sqlQuery = "SELECT MedicamentID, NumeMedicament, Dozaj FROM Medicamente ORDER BY NumeMedicament";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                medicamenteList.add(new Medicament(
                        rs.getInt("MedicamentID"),
                        rs.getString("NumeMedicament"),
                        rs.getString("Dozaj")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            afiseazaAlerta("Eroare Bază Date", null, "Eroare la încărcarea nomenclatorului de medicamente.", Alert.AlertType.ERROR);
        }
    }

    private void actualizeazaItemInListaUI(ObservableList<StocView> lista, StocView lotModificat) {
        boolean gasitInLista = false;

        for (StocView itemExistent : lista) {
            // Cautam item-ul dupa LotID (care este unic)
            if (itemExistent.getLotID() == lotModificat.getLotID()) {

                // 1. L-AM GASIT (cazul UPDATE)
                // Actualizam proprietatile item-ului DEJA existent in lista.
                // Deoarece StocView foloseste JavaFX Properties, tabelul se va actualiza automat.

                itemExistent.setCantitateStoc(lotModificat.getCantitateStoc());
                // Trebuie sa parsam inapoi data din String in LocalDate pentru setter
                itemExistent.setDataExpirare(LocalDate.parse(lotModificat.getDataExpirare()));
                itemExistent.setPretVanzare(lotModificat.getPretVanzare());

                gasitInLista = true;
                break; // Am terminat
            }
        }

        if (!gasitInLista) {
            // 2. NU L-AM GASIT (cazul INSERT)
            // Acesta este un lot complet nou, deci il adaugam in lista
            lista.add(lotModificat);
        }
    }


    @FXML
    private void onAdaugaMedicamentClick() {
        Dialog<Pair<Medicament, ObservableList<Furnizor>>> dialog = new Dialog<>();
        dialog.setTitle("Adaugă Medicament Nou");
        dialog.setHeaderText("Completați detaliile medicamentului și selectați furnizorii.");

        ButtonType okButtonType = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField numeField = new TextField();
        numeField.setPromptText("Nume Medicament");
        TextField dozajField = new TextField();
        dozajField.setPromptText("ex: 500mg");

        ListView<Furnizor> furnizoriListView = new ListView<>();
        // Incarcam lista de furnizori (daca nu e deja incarcata)
        if (!furnizoriDataLoaded) { loadFurnizoriData(); }
        furnizoriListView.setItems(furnizoriList); // Folosim lista deja incarcata
        furnizoriListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        furnizoriListView.setPrefHeight(150);
        furnizoriListView.setCellFactory(lv -> new ListCell<Furnizor>() {
            @Override
            protected void updateItem(Furnizor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNumeFurnizor());
            }
        });

        grid.add(new Label("Nume:"), 0, 0);
        grid.add(numeField, 1, 0);
        grid.add(new Label("Dozaj:"), 0, 1);
        grid.add(dozajField, 1, 1);
        grid.add(new Label("Furnizori:"), 0, 2);
        grid.add(furnizoriListView, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (numeField.getText().isEmpty()) {
                    return null;
                }
                Medicament med = new Medicament(0, numeField.getText(), dozajField.getText());
                ObservableList<Furnizor> furnizoriSelectati = furnizoriListView.getSelectionModel().getSelectedItems();
                return new Pair<>(med, furnizoriSelectati);
            }
            return null;
        });

        Optional<Pair<Medicament, ObservableList<Furnizor>>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            Medicament medNou = pair.getKey();
            ObservableList<Furnizor> furnizoriSelectati = pair.getValue();

            Task<Medicament> insertTask = new Task<>() {
                @Override
                protected Medicament call() throws Exception {
                    Connection conn = null;
                    try {
                        conn = DatabaseConnection.getConnection();
                        conn.setAutoCommit(false);

                        String sqlMed = "INSERT INTO Medicamente (NumeMedicament, Dozaj) VALUES (?, ?)";
                        PreparedStatement pstmtMed = conn.prepareStatement(sqlMed, Statement.RETURN_GENERATED_KEYS);
                        pstmtMed.setString(1, medNou.getNumeMedicament());
                        pstmtMed.setString(2, medNou.getDozaj());
                        pstmtMed.executeUpdate();

                        int medicamentID = -1;
                        try (ResultSet generatedKeys = pstmtMed.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                medicamentID = generatedKeys.getInt(1);
                                medNou.setMedicamentID(medicamentID);
                            } else {
                                throw new SQLException("Eroare la obținerea ID-ului pentru medicament.");
                            }
                        }
                        pstmtMed.close();

                        if (!furnizoriSelectati.isEmpty()) {
                            String sqlLink = "INSERT INTO MedicamenteFurnizori (MedicamentID, FurnizorID) VALUES (?, ?)";
                            PreparedStatement pstmtLink = conn.prepareStatement(sqlLink);

                            for (Furnizor f : furnizoriSelectati) {
                                pstmtLink.setInt(1, medicamentID);
                                pstmtLink.setInt(2, f.getFurnizorID());
                                pstmtLink.addBatch();
                            }
                            pstmtLink.executeBatch();
                            pstmtLink.close();
                        }

                        conn.commit();
                        return medNou;

                    } catch (Exception e) {
                        if (conn != null) conn.rollback();
                        throw new Exception("Eroare la salvarea medicamentului: " + e.getMessage(), e);
                    } finally {
                        if (conn != null) { conn.setAutoCommit(true); conn.close(); }
                    }
                }
            };

            insertTask.setOnSucceeded(e -> {
                medicamenteList.add(insertTask.getValue());
                afiseazaAlerta("Succes", null, "Medicamentul a fost adăugat.", Alert.AlertType.INFORMATION);
            });
            insertTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare Bază Date", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(insertTask).start();
        });
    }


    @FXML
    private void onStergeMedicamentClick() {
        Medicament medSelectat = medicamenteTable.getSelectionModel().getSelectedItem();
        if (medSelectat == null) {
            afiseazaAlerta("Eroare", null, "Vă rugăm selectați un medicament de șters.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare Ștergere");
        confirm.setHeaderText("Ștergeți medicamentul: " + medSelectat.getNumeMedicament() + "?");
        confirm.setContentText("Acțiunea va eșua dacă medicamentul încă mai are loturi în stoc.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Connection conn = null;
                    try {
                        conn = DatabaseConnection.getConnection();

                        String checkSql = "SELECT COUNT(*) FROM StocLoturi WHERE MedicamentID = ?";
                        try (PreparedStatement pstmtCheck = conn.prepareStatement(checkSql)) {
                            pstmtCheck.setInt(1, medSelectat.getMedicamentID());
                            try (ResultSet rs = pstmtCheck.executeQuery()) {
                                if (rs.next() && rs.getInt(1) > 0) {
                                    throw new Exception("Ștergere Blocată! Medicamentul există în stoc (" + rs.getInt(1) + " loturi).");
                                }
                            }
                        }

                        conn.setAutoCommit(false);

                        String sqlLink = "DELETE FROM MedicamenteFurnizori WHERE MedicamentID = ?";
                        try (PreparedStatement pstmtLink = conn.prepareStatement(sqlLink)) {
                            pstmtLink.setInt(1, medSelectat.getMedicamentID());
                            pstmtLink.executeUpdate();
                        }

                        String sqlMed = "DELETE FROM Medicamente WHERE MedicamentID = ?";
                        try (PreparedStatement pstmtMed = conn.prepareStatement(sqlMed)) {
                            pstmtMed.setInt(1, medSelectat.getMedicamentID());
                            int rows = pstmtMed.executeUpdate();
                            if (rows == 0) { throw new Exception("Nu s-a sters niciun medicament."); }
                        }

                        conn.commit();
                        return null;

                    } catch (Exception e) {
                        if (conn != null) conn.rollback();
                        throw new Exception(e.getMessage(), e);
                    } finally {
                        if (conn != null) { conn.setAutoCommit(true); conn.close(); }
                    }
                }
            };

            deleteTask.setOnSucceeded(e -> {
                medicamenteList.remove(medSelectat);
                afiseazaAlerta("Succes", null, "Medicamentul a fost șters.", Alert.AlertType.INFORMATION);
            });
            deleteTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare la Ștergere", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(deleteTask).start();
        }
    }

    @FXML
    private Button testQueriesButton; // <-- ADAUGĂ ASTA

    // ... (restul codului din DashboardController) ...

    /**
     * Metoda apelata de butonul de test.
     * Ruleaza toate interogarile din clasa DatabaseQueries
     * si afiseaza rezultatele in consola.
     */
    @FXML
    private void onTestQueriesClick() {
        System.out.println("==================================================");
        System.out.println("         INCEPUT RULARE INTEROGARI TEST           ");
        System.out.println("==================================================");

        // Apelez metodele statice din clasa nou creata

        // --- JOIN-uri ---
        // ATENTIE: Schimba "Popescu" cu un nume de angajat care EXISTA la tine in BD
        DatabaseQueries.getVanzariPentruAngajat("Popescu");

        // ATENTIE: Schimba '1' cu un VanzareID care EXISTA la tine in BD
        DatabaseQueries.getDetaliiVanzare(1);

        // ATENTIE: Schimba "Nume Furnizor" cu un furnizor care EXISTA la tine in BD
        DatabaseQueries.getStocDeLaFurnizor("Pharma Invest");

        // --- Subcereri ---
        DatabaseQueries.getMedicamenteFaraStoc();
        DatabaseQueries.getAngajatiFaraVanzari();
        DatabaseQueries.getLoturiPesteMedie();
        DatabaseQueries.getFurnizorCelMaiScumpProdus();

        System.out.println("\n==================================================");
        System.out.println("          FINAL RULARE INTEROGARI TEST            ");
        System.out.println("==================================================");

        afiseazaAlerta("Interogări Rulate",
                "Rezultatele au fost afișate în consolă.",
                "Verifică fereastra 'Run' sau 'Output' din IDE-ul tău (NetBeans/IntelliJ) pentru a vedea rezultatele.",
                Alert.AlertType.INFORMATION);
    }


// ... (functiile ajutatoare, gen createIcon, afiseazaAlerta etc.)

    // --- METODA NOUA PENTRU RECEPTIE MARFA ---

    @FXML
    private void onAdaugaLotClick() {
        Dialog<StocView> dialog = new Dialog<>();
        dialog.setTitle("Recepție Marfă (Adaugă Lot Nou)");
        dialog.setHeaderText("Introduceți detaliile pentru noul lot primit.");

        ButtonType okButtonType = new ButtonType("Adaugă", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 1. Dropdown pentru Medicament
        ComboBox<Medicament> medicamentComboBox = new ComboBox<>();
        if (!medicamenteDataLoaded) { loadMedicamenteData(); } // Asiguram ca lista e incarcata
        medicamentComboBox.setItems(medicamenteList);
        // Formatare ca sa afiseze Nume + Dozaj
        medicamentComboBox.setCellFactory(lv -> new ListCell<Medicament>() {
            @Override
            protected void updateItem(Medicament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNumeMedicament() + " (" + item.getDozaj() + ")");
            }
        });
        medicamentComboBox.setButtonCell(new ListCell<Medicament>() {
            @Override
            protected void updateItem(Medicament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNumeMedicament() + " (" + item.getDozaj() + ")");
            }
        });

        // 2. Restul campurilor
        TextField codLotField = new TextField();
        codLotField.setPromptText("Codul de pe cutie");
        TextField cantitateField = new TextField();
        cantitateField.setPromptText("ex: 100");
        DatePicker dataExpirarePicker = new DatePicker();
        dataExpirarePicker.setValue(LocalDate.now().plusYears(1)); // Default 1 an
        TextField pretVanzareField = new TextField();
        pretVanzareField.setPromptText("ex: 25.50");

        grid.add(new Label("Medicament:"), 0, 0);
        grid.add(medicamentComboBox, 1, 0);
        grid.add(new Label("Cod Lot:"), 0, 1);
        grid.add(codLotField, 1, 1);
        grid.add(new Label("Cantitate:"), 0, 2);
        grid.add(cantitateField, 1, 2);
        grid.add(new Label("Data Expirare:"), 0, 3);
        grid.add(dataExpirarePicker, 1, 3);
        grid.add(new Label("Preț Vânzare:"), 0, 4);
        grid.add(pretVanzareField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // --- Procesare Rezultat ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Validare
                if (medicamentComboBox.getValue() == null || codLotField.getText().isEmpty() || cantitateField.getText().isEmpty() || pretVanzareField.getText().isEmpty()) {
                    afiseazaAlerta("Eroare", null, "Toate câmpurile sunt obligatorii!", Alert.AlertType.ERROR);
                    return null;
                }
                try {
                    int cant = Integer.parseInt(cantitateField.getText());
                    double pret = Double.parseDouble(pretVanzareField.getText());
                    if (cant <= 0 || pret <= 0) throw new NumberFormatException();

                    Medicament medSelectat = medicamentComboBox.getValue();
                    return new StocView(
                            0, // ID-ul lotului (0 deocamdata)
                            medSelectat.getNumeMedicament(),
                            medSelectat.getDozaj(),
                            codLotField.getText(),
                            cant,
                            dataExpirarePicker.getValue(),
                            pret
                    );
                } catch (NumberFormatException e) {
                    afiseazaAlerta("Eroare", null, "Cantitatea și Prețul trebuie să fie numere pozitive!", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<StocView> result = dialog.showAndWait();

        // --- Salvare in Baza de Date ---
        result.ifPresent(lotNou -> {
            Medicament medSelectat = medicamentComboBox.getValue(); // Preluam medicamentul selectat

            Task<StocView> insertLotTask = new Task<>() {
                @Override
                protected StocView call() throws Exception {
                    Connection conn = null;
                    try {
                        conn = DatabaseConnection.getConnection();
                        conn.setAutoCommit(false); // Incepem tranzactia

                        int medicamentID = medSelectat.getMedicamentID();

                        // --- MODIFICARE CHEIE 1: Curatam codul de lot introdus de utilizator ---
                        String codLotCuratat = lotNou.getCodLot().trim().toUpperCase();
                        if (codLotCuratat.isEmpty()) {
                            throw new Exception("Codul lotului nu poate fi gol.");
                        }

                        int cantitateAdaugata = lotNou.getCantitateStoc();
                        Date dataExpirareNoua = Date.valueOf(lotNou.getDataExpirare());
                        double pretVanzareNou = lotNou.getPretVanzare();

                        // --- MODIFICARE CHEIE 2: Folosim UPPER si TRIM si in SQL ---
                        String sqlSelect = "SELECT LotID, CantitateStoc FROM StocLoturi WHERE MedicamentID = ? AND UPPER(TRIM(CodLot)) = ?";
                        PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
                        pstmtSelect.setInt(1, medicamentID);
                        pstmtSelect.setString(2, codLotCuratat); // Folosim codul curatat

                        ResultSet rs = pstmtSelect.executeQuery();

                        if (rs.next()) {
                            // --- 2. LOTUL EXISTA ( facem UPDATE ) ---

                            // --- DEBUG ---
                            System.out.println("DEBUG: Lotul " + codLotCuratat + " gasit. Se face UPDATE.");
                            // --- SFARSIT DEBUG ---

                            int lotIDExistent = rs.getInt("LotID");
                            int cantitateVeche = rs.getInt("CantitateStoc");
                            int cantitateTotala = cantitateVeche + cantitateAdaugata;

                            String sqlUpdate = "UPDATE StocLoturi SET CantitateStoc = ?, DataExpirare = ?, PretVanzare = ? WHERE LotID = ?";
                            PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                            pstmtUpdate.setInt(1, cantitateTotala);
                            pstmtUpdate.setDate(2, dataExpirareNoua);
                            pstmtUpdate.setDouble(3, pretVanzareNou);
                            pstmtUpdate.setInt(4, lotIDExistent);
                            pstmtUpdate.executeUpdate();
                            pstmtUpdate.close();

                            // Actualizam obiectul 'lotNou' cu datele corecte
                            lotNou.setLotID(lotIDExistent);
                            lotNou.setCantitateStoc(cantitateTotala); // Aici folosim setter-ul adaugat
                            lotNou.setDataExpirare(dataExpirareNoua.toLocalDate()); // Actualizam si data
                            lotNou.setPretVanzare(pretVanzareNou); // Actualizam si pretul

                        } else {
                            // --- 3. LOTUL NU EXISTA ( facem INSERT ) ---

                            // --- DEBUG ---
                            System.out.println("DEBUG: Lotul " + codLotCuratat + " NU a fost gasit. Se face INSERT.");
                            // --- SFARSIT DEBUG ---

                            String sqlInsert = "INSERT INTO StocLoturi (MedicamentID, CodLot, CantitateStoc, DataExpirare, PretVanzare) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

                            pstmtInsert.setInt(1, medicamentID);
                            pstmtInsert.setString(2, codLotCuratat); // Folosim codul curatat
                            pstmtInsert.setInt(3, cantitateAdaugata);
                            pstmtInsert.setDate(4, dataExpirareNoua);
                            pstmtInsert.setDouble(5, pretVanzareNou);
                            pstmtInsert.executeUpdate();

                            try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    lotNou.setLotID(generatedKeys.getInt(1));
                                    // Restul datelor (cantitate, pret etc.) sunt deja corecte in 'lotNou'
                                } else {
                                    throw new SQLException("Eroare la obținerea ID-ului pentru noul lot.");
                                }
                            }
                            pstmtInsert.close();
                        }

                        rs.close();
                        pstmtSelect.close();
                        conn.commit(); // Finalizam tranzactia

                        return lotNou; // Returnam lotul (fie el actualizat sau nou)

                    } catch (Exception e) {
                        if (conn != null) conn.rollback(); // Anulam tranzactia in caz de eroare

                        // --- DEBUG ---
                        System.err.println("DEBUG: Eroare majora in 'call()' la adaugare lot:");
                        e.printStackTrace();
                        // --- SFARSIT DEBUG ---

                        throw new Exception("Eroare la salvarea lotului: " + e.getMessage(), e); // Aruncam eroarea
                    } finally {
                        if (conn != null) {
                            conn.setAutoCommit(true);
                            conn.close();
                        }
                    }
                }
            };

            insertLotTask.setOnSucceeded(e -> {
                // Preluam lotul care a fost modificat sau inserat in BD
                StocView lotModificat = insertLotTask.getValue();

                // Folosim o functie ajutatoare pentru a actualiza listele
                // in mod inteligent (fara a crea duplicate)
                actualizeazaItemInListaUI(stocuriList, lotModificat);
                actualizeazaItemInListaUI(stocVanzariList, lotModificat);

                // Fortam un refresh al tabelelor (recomandat)
                stocuriTable.refresh();
                stocVanzariTable.refresh();

                afiseazaAlerta("Succes", null, "Lotul a fost adăugat sau actualizat în stoc.", Alert.AlertType.INFORMATION);
            });

            insertLotTask.setOnFailed(e ->  {
                afiseazaAlerta("Eroare Bază Date", null, "Eroare la adăugarea lotului: " + e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
                e.getSource().getException().printStackTrace();
            });

            new Thread(insertLotTask).start();
        });
    }


    private boolean proceseazaVanzareaInBD() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlInsertVanzare = "INSERT INTO Vanzari (DataVanzare, AngajatID, TotalBon) VALUES (GETDATE(), ?, ?)";
            double totalBon = calculeazaTotalBon();

            PreparedStatement pstmtVanzare = conn.prepareStatement(sqlInsertVanzare, Statement.RETURN_GENERATED_KEYS);
            pstmtVanzare.setInt(1, angajatCurent.getAngajatID());
            pstmtVanzare.setDouble(2, totalBon);
            pstmtVanzare.executeUpdate();

            int vanzareID = -1;
            ResultSet generatedKeys = pstmtVanzare.getGeneratedKeys();
            if (generatedKeys.next()) {
                vanzareID = generatedKeys.getInt(1);
            }
            if (vanzareID == -1) {
                throw new SQLException("Eroare la crearea antetului de vanzare.");
            }
            pstmtVanzare.close();

            String sqlInsertDetalii = "INSERT INTO VanzariDetalii (VanzareID, LotID, CantitateVanduta, PretUnitarInregistrat) VALUES (?, ?, ?, ?)";
            String sqlUpdateStoc = "UPDATE StocLoturi SET CantitateStoc = CantitateStoc - ? WHERE LotID = ?";
            PreparedStatement pstmtDetalii = conn.prepareStatement(sqlInsertDetalii);
            PreparedStatement pstmtUpdateStoc = conn.prepareStatement(sqlUpdateStoc);

            for (BonItem item : bonCurentList) {
                pstmtDetalii.setInt(1, vanzareID);
                pstmtDetalii.setInt(2, item.getLotID());
                pstmtDetalii.setInt(3, item.getCantitateVanduta());
                pstmtDetalii.setDouble(4, item.getPretUnitar());
                pstmtDetalii.addBatch();

                pstmtUpdateStoc.setInt(1, item.getCantitateVanduta());
                pstmtUpdateStoc.setInt(2, item.getLotID());
                pstmtUpdateStoc.addBatch();
            }

            pstmtDetalii.executeBatch();
            pstmtUpdateStoc.executeBatch();

            conn.commit();

            pstmtDetalii.close();
            pstmtUpdateStoc.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // --- FUNCTII AJUTATOARE ---

    private double calculeazaTotalBon() {
        double total = 0.0;
        for (BonItem item : bonCurentList) {
            total += item.getPretTotal();
        }
        totalBonLabel.setText(String.format("%.2f Lei", total));
        return total;
    }


    private void afiseazaAlerta(String titlu, String header, String mesaj, Alert.AlertType tip) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(header);
        alert.setContentText(mesaj);
        alert.getDialogPane().setMinWidth(500);
        alert.showAndWait();
    }


    // Functie ajutatoare pentru a afisa alerte rapide
    private void afiseazaAlerta(String titlu, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    /**
     * Metoda ajutatoare pentru a crea un ImageView dintr-o cale.
     */
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