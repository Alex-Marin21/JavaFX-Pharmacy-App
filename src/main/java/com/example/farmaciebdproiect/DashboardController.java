package com.example.farmaciebdproiect;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label titluStocCurent;
    @FXML private Label titluStocDisponibil;
    @FXML private Label titluBonCurent;
    @FXML private Label titluProduseCritice;
    @FXML private Label titluFurnizoriDisponibili;
    @FXML private Label titluListaFurnizori;
    @FXML private Label titluListaAngajati;

    @FXML
    private Label titluVanzariAngajat;
    @FXML
    private TableView<VanzareRaport> vanzariAngajatTable;
    @FXML
    private TableColumn<VanzareRaport, Integer> colVanzareID;
    @FXML
    private TableColumn<VanzareRaport, String> colVanzareData;
    @FXML
    private TableColumn<VanzareRaport, Double> colVanzareTotal;

    @FXML private Tab stocuriTab;
    @FXML private Tab comenziTab;
    @FXML private Tab vanzariTab;
    @FXML private Tab furnizoriTab;
    @FXML private Tab angajatiTab;

    private final ObservableList<ComandaProdus> comenziProduseList = FXCollections.observableArrayList();
    private final ObservableList<Medicament> produseEpuizateList = FXCollections.observableArrayList();
    private final ObservableList<ComandaFurnizor> comenziFurnizoriList = FXCollections.observableArrayList();
    @FXML
    private TableView<ComandaProdus> comenziProduseTable;
    @FXML
    private Label titluProduseEpuizate;
    @FXML
    private TableView<Medicament> produseEpuizateTable;
    @FXML
    private TableColumn<Medicament, String> colEpuizatNume;
    @FXML
    private TableColumn<Medicament, String> colEpuizatDozaj;
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

    private final ObservableList<VanzareRaport> vanzariAngajatList = FXCollections.observableArrayList();

    private final ObservableList<Angajat> angajatiList = FXCollections.observableArrayList();
    private final java.util.Set<Integer> angajatiInactiviIDs = new java.util.HashSet<>();
    private final javafx.css.PseudoClass inactivePseudoClass = javafx.css.PseudoClass.getPseudoClass("inactive");
    private final ObservableList<Furnizor> furnizoriList = FXCollections.observableArrayList();
    private final ObservableList<StocView> stocFurnizorList = FXCollections.observableArrayList();
    private final ObservableList<StocView> stocuriList = FXCollections.observableArrayList();
    private final ObservableList<BonItem> bonCurentList = FXCollections.observableArrayList();
    private final ObservableList<StocView> stocVanzariList = FXCollections.observableArrayList();
    private Angajat angajatCurent;

    @FXML
    private TableView<Angajat> angajatiTable;
    private boolean angajatiDataLoaded = false;

    @FXML
    private TableView<Furnizor> furnizoriTable;
    @FXML
    private Label titluStocFurnizor;
    @FXML
    private TableView<StocView> stocFurnizorTable;
    @FXML
    private TableColumn<StocView, String> colStocFurnNume;
    @FXML
    private TableColumn<StocView, String> colStocFurnLot;
    @FXML
    private TableColumn<StocView, Integer> colStocFurnCant;
    private boolean furnizoriDataLoaded = false;
    @FXML
    private Button adaugaFurnizorButton;
    @FXML
    private Button stergeFurnizorButton;

    @FXML
    private TableView<StocView> stocuriTable;
    private boolean stocuriDataLoaded = false;
    @FXML
    private Button adaugaLotButton;

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

    private final ObservableList<Medicament> medicamenteList = FXCollections.observableArrayList();
    private boolean medicamenteDataLoaded = false;
    @FXML private Label titluListaMedicamente;
    @FXML private TableView<Medicament> medicamenteTable;
    @FXML private TableColumn<Medicament, Integer> colMedID;
    @FXML private TableColumn<Medicament, String> colMedNume;
    @FXML private TableColumn<Medicament, String> colMedDozaj;
    @FXML private Button adaugaMedicamentButton;
    @FXML private Button stergeMedicamentButton;

    public void initData(Angajat angajat) {
        this.angajatCurent = angajat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // --- Bloc de stiluri si pictograme ---
        if (stocuriTab != null) stocuriTab.setGraphic(createIcon("Images/icons8-pills-50.png", 20));
        if (comenziTab != null) comenziTab.setGraphic(createIcon("Images/icons8-truck.gif", 20));
        if (vanzariTab != null) vanzariTab.setGraphic(createIcon("Images/icons8-cart.gif", 20));
        if (furnizoriTab != null) furnizoriTab.setGraphic(createIcon("Images/icons8-truck.gif", 20));
        if (angajatiTab != null) angajatiTab.setGraphic(createIcon("Images/icons8-man-50.png", 20));

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
        if (adaugaLotButton != null) {
            adaugaLotButton.setId("butonAdauga");
            adaugaLotButton.setGraphic(createIcon("Images/icons8-add-50.png", 16));
        }

        if (titluStocCurent != null) titluStocCurent.getStyleClass().add("header-title");
        if (titluStocDisponibil != null) titluStocDisponibil.getStyleClass().add("header-title");
        if (titluBonCurent != null) titluBonCurent.getStyleClass().add("header-title");
        if (titluProduseCritice != null) titluProduseCritice.getStyleClass().add("header-title");
        if (titluFurnizoriDisponibili != null) titluFurnizoriDisponibili.getStyleClass().add("header-title");
        if (titluListaFurnizori != null) titluListaFurnizori.getStyleClass().add("header-title");
        if (titluListaAngajati != null) titluListaAngajati.getStyleClass().add("header-title");
        if (titluListaMedicamente != null) titluListaMedicamente.getStyleClass().add("header-title");

        // --- Setup Fila Angajati ---
        if (angajatiTab != null) {
            colVanzareID.setCellValueFactory(new PropertyValueFactory<>("vanzareID"));
            colVanzareData.setCellValueFactory(new PropertyValueFactory<>("dataVanzare"));
            colVanzareTotal.setCellValueFactory(new PropertyValueFactory<>("totalBon"));
            vanzariAngajatTable.setItems(vanzariAngajatList);

            // Listener pentru selectia randului din tabelul de angajati
            angajatiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadVanzariPentruAngajat(newSelection);
                } else {
                    vanzariAngajatList.clear();
                }
            });

            // Listener pentru deschiderea tab-ului
            angajatiTab.setOnSelectionChanged(event -> {
                if (angajatiTab.isSelected() && !angajatiDataLoaded) {
                    System.out.println("Se incarca datele angajatilor...");
                    loadAngajatiData();
                    angajatiDataLoaded = true;
                }
            });

            // Factory pentru a colora randurile pe baza starii CSS "inactive"
            angajatiTable.setRowFactory(tv -> new TableRow<Angajat>() {
                @Override
                protected void updateItem(Angajat angajat, boolean empty) {
                    super.updateItem(angajat, empty);

                    pseudoClassStateChanged(inactivePseudoClass, false);

                    if (angajat != null && !empty) {
                        boolean isInactive = angajatiInactiviIDs.contains(angajat.getAngajatID());
                        pseudoClassStateChanged(inactivePseudoClass, isInactive);
                    }
                }
            });
        }

        // --- Setup Fila Furnizori ---
        if (furnizoriTab != null) {
            colStocFurnNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
            colStocFurnLot.setCellValueFactory(new PropertyValueFactory<>("codLot"));
            colStocFurnCant.setCellValueFactory(new PropertyValueFactory<>("cantitateStoc"));
            stocFurnizorTable.setItems(stocFurnizorList);

            // Listener pentru selectia randului din tabelul de furnizori
            furnizoriTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadStocPentruFurnizor(newSelection);
                } else {
                    stocFurnizorList.clear();
                    titluStocFurnizor.setText("Stoc de la Furnizor");
                }
            });

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

            if (!medicamenteDataLoaded) {
                loadMedicamenteData();
                medicamenteDataLoaded = true;
            }
        }

        // --- Setup Fila Vanzari ---
        if (vanzariTab != null) {
            setupColoaneVanzari();
            vanzariTab.setOnSelectionChanged(event -> {
                if (vanzariTab.isSelected() && !stocVanzariDataLoaded) {
                    System.out.println("Se incarca datele de stoc pentru vanzari...");
                    loadStocuriData(stocVanzariTable, stocVanzariList);
                    stocVanzariDataLoaded = true;
                } else if (vanzariTab.isSelected()) {
                    stocVanzariList.setAll(stocuriList);
                }
            });
            bonCurentTable.setItems(bonCurentList);
        }

        // --- Setup Fila Comenzi ---
        if (comenziTab != null) {

            // Configurare Tabela 1 (Stanga Sus - Stoc Critic)
            colComandaNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
            colComandaStoc.setCellValueFactory(new PropertyValueFactory<>("totalStoc"));
            comenziProduseTable.setItems(comenziProduseList);

            // Configurare Tabela 2 (Stanga Jos - Stoc Epuizat)
            colEpuizatNume.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
            colEpuizatDozaj.setCellValueFactory(new PropertyValueFactory<>("dozaj"));
            produseEpuizateTable.setItems(produseEpuizateList);

            // Configurare Tabela 3 (Dreapta - Furnizori)
            colComandaFurnizor.setCellValueFactory(new PropertyValueFactory<>("numeFurnizor"));
            colComandaTimp.setCellValueFactory(new PropertyValueFactory<>("timpLivrare"));
            comenziFurnizoriTable.setItems(comenziFurnizoriList);

            // Listener pentru deschiderea Tab-ului
            comenziTab.setOnSelectionChanged(event -> {
                if (comenziTab.isSelected() && !comenziDataLoaded) {
                    System.out.println("Se incarca datele pentru comenzi...");
                    loadComenziProduseData();
                    comenziDataLoaded = true;
                }
            });

            // Listener Tabela 1 (Stoc Critic)
            comenziProduseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    produseEpuizateTable.getSelectionModel().clearSelection();

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
                        afiseazaAlerta("Eroare Baza Date", null, "Nu s-au putut incarca furnizorii.", Alert.AlertType.ERROR);
                    });
                    new Thread(loadFurnizoriTask).start();
                } else {
                    if (produseEpuizateTable.getSelectionModel().getSelectedItem() == null) {
                        comenziFurnizoriList.clear();
                    }
                }
            });

            // Listener Tabela 2 (Stoc Epuizat)
            produseEpuizateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    comenziProduseTable.getSelectionModel().clearSelection();

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
                        afiseazaAlerta("Eroare Baza Date", null, "Nu s-au putut incarca furnizorii.", Alert.AlertType.ERROR);
                    });
                    new Thread(loadFurnizoriTask).start();
                } else {
                    if (comenziProduseTable.getSelectionModel().getSelectedItem() == null) {
                        comenziFurnizoriList.clear();
                    }
                }
            });
        }
    }

    // Incarca vanzarile pentru un angajat selectat (din Tab-ul Angajati)
    private void loadVanzariPentruAngajat(Angajat angajat) {
        vanzariAngajatList.clear();

        String sql = "SELECT v.VanzareID, v.DataVanzare, v.TotalBon " +
                "FROM Vanzari v " +
                "JOIN Angajati a ON v.AngajatID = a.AngajatID " +
                "WHERE a.AngajatID = ? " +
                "ORDER BY v.DataVanzare DESC";

        Task<ObservableList<VanzareRaport>> loadVanzariTask = new Task<>() {
            @Override
            protected ObservableList<VanzareRaport> call() throws Exception {
                ObservableList<VanzareRaport> vanzari = FXCollections.observableArrayList();
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, angajat.getAngajatID());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            vanzari.add(new VanzareRaport(
                                    rs.getInt("VanzareID"),
                                    rs.getDate("DataVanzare"),
                                    rs.getDouble("TotalBon")
                            ));
                        }
                    }
                }
                return vanzari;
            }
        };

        loadVanzariTask.setOnSucceeded(e -> {
            vanzariAngajatList.setAll(loadVanzariTask.getValue());
            if (vanzariAngajatList.isEmpty()) {
                titluVanzariAngajat.setText("Fara vanzari pentru " + angajat.getNume());
            } else {
                titluVanzariAngajat.setText("Vanzari pentru " + angajat.getNume());
            }
        });

        loadVanzariTask.setOnFailed(e -> {
            afiseazaAlerta("Eroare Baza Date", null, "Nu s-au putut incarca vanzarile.", Alert.AlertType.ERROR);
            loadVanzariTask.getException().printStackTrace();
        });

        new Thread(loadVanzariTask).start();
    }

    // Incarca stocul pentru un furnizor selectat (din Tab-ul Furnizori)
    private void loadStocPentruFurnizor(Furnizor furnizor) {
        stocFurnizorList.clear();

        String sql = "SELECT m.NumeMedicament, m.Dozaj, s.LotID, s.CodLot, s.CantitateStoc, s.DataExpirare, s.PretVanzare " +
                "FROM StocLoturi s " +
                "JOIN Medicamente m ON s.MedicamentID = m.MedicamentID " +
                "JOIN MedicamenteFurnizori mf ON m.MedicamentID = mf.MedicamentID " +
                "JOIN Furnizori f ON mf.FurnizorID = f.FurnizorID " +
                "WHERE f.FurnizorID = ? AND s.CantitateStoc > 0";

        Task<ObservableList<StocView>> loadStocTask = new Task<>() {
            @Override
            protected ObservableList<StocView> call() throws Exception {
                ObservableList<StocView> stoc = FXCollections.observableArrayList();
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, furnizor.getFurnizorID());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            stoc.add(new StocView(
                                    rs.getInt("LotID"),
                                    rs.getString("NumeMedicament"),
                                    rs.getString("Dozaj"),
                                    rs.getString("CodLot"),
                                    rs.getInt("CantitateStoc"),
                                    rs.getDate("DataExpirare").toLocalDate(),
                                    rs.getDouble("PretVanzare")
                            ));
                        }
                    }
                }
                return stoc;
            }
        };

        loadStocTask.setOnSucceeded(e -> {
            stocFurnizorList.setAll(loadStocTask.getValue());
            titluStocFurnizor.setText("Stoc de la " + furnizor.getNumeFurnizor());
        });

        loadStocTask.setOnFailed(e -> {
            afiseazaAlerta("Eroare Baza Date", null, "Nu s-a putut incarca stocul furnizorului.", Alert.AlertType.ERROR);
            loadStocTask.getException().printStackTrace();
        });

        new Thread(loadStocTask).start();
    }

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

    // Incarca datele pentru Tab-ul Comenzi (Stoc Critic si Stoc Epuizat)
    private void loadComenziProduseData() {
        comenziProduseList.clear();
        produseEpuizateList.clear();

        // Interogare Stoc Critic (intre 1 si 9 bucati)
        String sqlCritic = "SELECT m.MedicamentID, m.NumeMedicament, SUM(s.CantitateStoc) AS TotalStoc " +
                "FROM Medicamente m " +
                "LEFT JOIN StocLoturi s ON m.MedicamentID = s.MedicamentID " +
                "GROUP BY m.MedicamentID, m.NumeMedicament " +
                "HAVING ISNULL(SUM(s.CantitateStoc), 0) < 10 AND ISNULL(SUM(s.CantitateStoc), 0) > 0 " +
                "ORDER BY TotalStoc ASC";

        // Interogare Stoc Epuizat (stoc 0 sau null)
        String sqlEpuizat = "SELECT MedicamentID, NumeMedicament, Dozaj FROM Medicamente " +
                "WHERE MedicamentID NOT IN (SELECT DISTINCT MedicamentID FROM StocLoturi WHERE CantitateStoc > 0)";

        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DatabaseConnection.getConnection()) {

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(sqlCritic)) {
                        while (rs.next()) {
                            comenziProduseList.add(new ComandaProdus(
                                    rs.getInt("MedicamentID"),
                                    rs.getString("NumeMedicament"),
                                    rs.getInt("TotalStoc")
                            ));
                        }
                    }

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(sqlEpuizat)) {
                        while (rs.next()) {
                            produseEpuizateList.add(new Medicament(
                                    rs.getInt("MedicamentID"),
                                    rs.getString("NumeMedicament"),
                                    rs.getString("Dozaj")
                            ));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                return null;
            }
        };

        loadTask.setOnFailed(e -> {
            afiseazaAlerta("Eroare Baza Date", null, "Eroare la incarcarea listelor de comanda.", Alert.AlertType.ERROR);
            loadTask.getException().printStackTrace();
        });

        new Thread(loadTask).start();
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
        String numeProdusSelectat = null;

        // Determina produsul selectat din oricare tabel (Critic sau Epuizat)
        ComandaProdus produsCritic = comenziProduseTable.getSelectionModel().getSelectedItem();
        Medicament produsEpuizat = produseEpuizateTable.getSelectionModel().getSelectedItem();

        if (produsCritic != null) {
            numeProdusSelectat = produsCritic.getNumeMedicament();
        } else if (produsEpuizat != null) {
            numeProdusSelectat = produsEpuizat.getNumeMedicament();
        }

        // Verifica daca un produs a fost selectat
        if (numeProdusSelectat == null) {
            afiseazaAlerta("Eroare Comanda", null, "Va rugam selectati un PRODUS de comandat (din oricare lista).", Alert.AlertType.WARNING);
            return;
        }

        // Verifica daca un furnizor a fost selectat
        ComandaFurnizor furnizorSelectat = comenziFurnizoriTable.getSelectionModel().getSelectedItem();
        if (furnizorSelectat == null) {
            afiseazaAlerta("Eroare Comanda", null, "Va rugam selectati un FURNIZOR de la care sa comandati.", Alert.AlertType.WARNING);
            return;
        }

        afiseazaAlerta("Comanda Plasata (Simulare)",
                "Comanda a fost generata cu succes!",
                "Produs: " + numeProdusSelectat + "\n" +
                        "Furnizor: " + furnizorSelectat.getNumeFurnizor() + "\n" +
                        "Timp estimat: " + furnizorSelectat.getTimpLivrare() + " zile.",
                Alert.AlertType.INFORMATION);
    }

    private void loadAngajatiData() {
        angajatiList.clear();
        angajatiInactiviIDs.clear();

        String sqlAngajati = "SELECT AngajatID, Nume, Prenume, CodParafa FROM Angajati";

        // Incarca angajatii inactivi (fara vanzari) pentru colorare
        String sqlInactivi = "SELECT AngajatID FROM Angajati " +
                "WHERE AngajatID NOT IN (SELECT DISTINCT AngajatID FROM Vanzari)";

        try (Connection conn = DatabaseConnection.getConnection()) {

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlAngajati)) {

                while (rs.next()) {
                    Angajat angajat = new Angajat(
                            rs.getInt("AngajatID"),
                            rs.getString("Nume"),
                            rs.getString("Prenume"),
                            rs.getString("CodParafa")
                    );
                    angajatiList.add(angajat);
                }
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rsInactivi = stmt.executeQuery(sqlInactivi)) {

                while (rsInactivi.next()) {
                    angajatiInactiviIDs.add(rsInactivi.getInt("AngajatID"));
                }
            }

            angajatiTable.setItems(angajatiList);
            angajatiTable.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            afiseazaAlerta("Eroare Baza Date", null, "Eroare la incarcarea datelor angajatilor.", Alert.AlertType.ERROR);
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
                loadStocuriData(stocuriTable, stocuriList);
                stocVanzariList.setAll(stocuriList);
            } else {
                afiseazaAlerta("Eroare Baza de Date", "A aparut o eroare la salvarea vanzarii. Verificati consola.", "", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onAdaugaFurnizorClick() {
        Dialog<Furnizor> dialog = new Dialog<>();
        dialog.setTitle("Adauga Furnizor Nou");
        dialog.setHeaderText("Introduceti detaliile noului furnizor.");

        ButtonType okButtonType = new ButtonType("Adauga", ButtonBar.ButtonData.OK_DONE);
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
                    afiseazaAlerta("Eroare", null, "Numele si CUI-ul sunt obligatorii!", Alert.AlertType.ERROR);
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
                                throw new SQLException("Eroare la obtinerea ID-ului pentru furnizor.");
                            }
                        }
                    }
                }
            };

            insertTask.setOnSucceeded(e -> {
                furnizoriList.add(insertTask.getValue());
                afiseazaAlerta("Succes", null, "Furnizorul a fost adaugat.", Alert.AlertType.INFORMATION);
            });
            insertTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare Baza Date", null, "Eroare la adaugarea furnizorului: " + e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(insertTask).start();
        });
    }

    @FXML
    private void onStergeFurnizorClick() {
        Furnizor furnizorSelectat = furnizoriTable.getSelectionModel().getSelectedItem();

        if (furnizorSelectat == null) {
            afiseazaAlerta("Eroare", null, "Va rugam selectati un furnizor din tabel.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare Stergere");
        confirm.setHeaderText("Sunteti sigur ca doriti sa stergeti furnizorul: " + furnizorSelectat.getNumeFurnizor() + "?");
        confirm.setContentText("Actiunea va esua daca furnizorul este inca asociat cu medicamente.");

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
                                throw new Exception("Stergere Blocata! Furnizorul este asociat cu " + rs.getInt(1) + " medicamente.");
                            }
                        }
                    }

                    String deleteSql = "DELETE FROM Furnizori WHERE FurnizorID = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {

                        pstmtDelete.setInt(1, furnizorSelectat.getFurnizorID());
                        int rowsAffected = pstmtDelete.executeUpdate();
                        if (rowsAffected == 0) {
                            throw new Exception("Niciun rand nu a fost sters (eroare necunoscuta).");
                        }
                    }
                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                furnizoriList.remove(furnizorSelectat);
                afiseazaAlerta("Succes", null, "Furnizorul a fost sters.", Alert.AlertType.INFORMATION);
            });
            deleteTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare la Stergere", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(deleteTask).start();
        }
    }

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
            afiseazaAlerta("Eroare Baza Date", null, "Eroare la incarcarea nomenclatorului de medicamente.", Alert.AlertType.ERROR);
        }
    }

    // Functie helper pentru a actualiza listele UI fara a reincarca totul
    private void actualizeazaItemInListaUI(ObservableList<StocView> lista, StocView lotModificat) {
        boolean gasitInLista = false;

        // Cauta daca lotul exista deja in lista UI
        for (StocView itemExistent : lista) {
            if (itemExistent.getLotID() == lotModificat.getLotID()) {

                // Daca exista, actualizeaza cantitatea (UPDATE)
                itemExistent.setCantitateStoc(lotModificat.getCantitateStoc());
                itemExistent.setDataExpirare(LocalDate.parse(lotModificat.getDataExpirare()));
                itemExistent.setPretVanzare(lotModificat.getPretVanzare());

                gasitInLista = true;
                break;
            }
        }

        if (!gasitInLista) {
            // Daca nu exista, adauga-l in lista (INSERT)
            lista.add(lotModificat);
        }
    }

    @FXML
    private void onAdaugaMedicamentClick() {
        Dialog<Pair<Medicament, ObservableList<Furnizor>>> dialog = new Dialog<>();
        dialog.setTitle("Adauga Medicament Nou");
        dialog.setHeaderText("Completati detaliile medicamentului si selectati furnizorii.");

        ButtonType okButtonType = new ButtonType("Adauga", ButtonBar.ButtonData.OK_DONE);
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
        if (!furnizoriDataLoaded) { loadFurnizoriData(); }
        furnizoriListView.setItems(furnizoriList);
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
                                throw new SQLException("Eroare la obtinerea ID-ului pentru medicament.");
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
                afiseazaAlerta("Succes", null, "Medicamentul a fost adaugat.", Alert.AlertType.INFORMATION);
            });
            insertTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare Baza Date", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(insertTask).start();
        });
    }

    @FXML
    private void onStergeMedicamentClick() {
        Medicament medSelectat = medicamenteTable.getSelectionModel().getSelectedItem();
        if (medSelectat == null) {
            afiseazaAlerta("Eroare", null, "Va rugam selectati un medicament de sters.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare Stergere");
        confirm.setHeaderText("Stergeti medicamentul: " + medSelectat.getNumeMedicament() + "?");
        confirm.setContentText("Actiunea va esua daca medicamentul inca mai are loturi in stoc.");

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
                                    throw new Exception("Stergere Blocata! Medicamentul exista in stoc (" + rs.getInt(1) + " loturi).");
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
                afiseazaAlerta("Succes", null, "Medicamentul a fost sters.", Alert.AlertType.INFORMATION);
            });
            deleteTask.setOnFailed(e -> {
                afiseazaAlerta("Eroare la Stergere", null, e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(deleteTask).start();
        }
    }

    @FXML
    private void onAdaugaLotClick() {
        Dialog<StocView> dialog = new Dialog<>();
        dialog.setTitle("Receptie Marfa (Adauga Lot Nou)");
        dialog.setHeaderText("Introduceti detaliile pentru noul lot primit.");

        ButtonType okButtonType = new ButtonType("Adauga", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Medicament> medicamentComboBox = new ComboBox<>();
        if (!medicamenteDataLoaded) { loadMedicamenteData(); }
        medicamentComboBox.setItems(medicamenteList);
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

        TextField codLotField = new TextField();
        codLotField.setPromptText("Codul de pe cutie");
        TextField cantitateField = new TextField();
        cantitateField.setPromptText("ex: 100");
        DatePicker dataExpirarePicker = new DatePicker();
        dataExpirarePicker.setValue(LocalDate.now().plusYears(1));
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
        grid.add(new Label("Pret Vanzare:"), 0, 4);
        grid.add(pretVanzareField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Converteste dialogul in obiect StocView la apasarea butonului OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (medicamentComboBox.getValue() == null || codLotField.getText().isEmpty() || cantitateField.getText().isEmpty() || pretVanzareField.getText().isEmpty()) {
                    afiseazaAlerta("Eroare", null, "Toate campurile sunt obligatorii!", Alert.AlertType.ERROR);
                    return null;
                }
                try {
                    int cant = Integer.parseInt(cantitateField.getText());
                    double pret = Double.parseDouble(pretVanzareField.getText());
                    if (cant <= 0 || pret <= 0) throw new NumberFormatException();

                    Medicament medSelectat = medicamentComboBox.getValue();
                    return new StocView(
                            0,
                            medSelectat.getNumeMedicament(),
                            medSelectat.getDozaj(),
                            codLotField.getText(),
                            cant,
                            dataExpirarePicker.getValue(),
                            pret
                    );
                } catch (NumberFormatException e) {
                    afiseazaAlerta("Eroare", null, "Cantitatea si Pretul trebuie sa fie numere pozitive!", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<StocView> result = dialog.showAndWait();

        result.ifPresent(lotNou -> {
            Medicament medSelectat = medicamentComboBox.getValue();

            // Task-ul de salvare in BD ruleaza pe un fir separat
            Task<StocView> insertLotTask = new Task<>() {
                @Override
                protected StocView call() throws Exception {
                    Connection conn = null;
                    try {
                        conn = DatabaseConnection.getConnection();
                        conn.setAutoCommit(false);

                        int medicamentID = medSelectat.getMedicamentID();

                        String codLotCuratat = lotNou.getCodLot().trim().toUpperCase();
                        if (codLotCuratat.isEmpty()) {
                            throw new Exception("Codul lotului nu poate fi gol.");
                        }

                        int cantitateAdaugata = lotNou.getCantitateStoc();
                        Date dataExpirareNoua = Date.valueOf(lotNou.getDataExpirare());
                        double pretVanzareNou = lotNou.getPretVanzare();

                        // Verifica daca lotul exista deja in BD (UPDATE) sau e nou (INSERT)
                        String sqlSelect = "SELECT LotID, CantitateStoc FROM StocLoturi WHERE MedicamentID = ? AND UPPER(TRIM(CodLot)) = ?";
                        PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
                        pstmtSelect.setInt(1, medicamentID);
                        pstmtSelect.setString(2, codLotCuratat);

                        ResultSet rs = pstmtSelect.executeQuery();

                        if (rs.next()) {
                            // Cazul 1: Lotul EXISTA (UPDATE)
                            System.out.println("DEBUG: Lotul " + codLotCuratat + " gasit. Se face UPDATE.");

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

                            lotNou.setLotID(lotIDExistent);
                            lotNou.setCantitateStoc(cantitateTotala);
                            lotNou.setDataExpirare(dataExpirareNoua.toLocalDate());
                            lotNou.setPretVanzare(pretVanzareNou);

                        } else {
                            // Cazul 2: Lotul NU EXISTA (INSERT)
                            System.out.println("DEBUG: Lotul " + codLotCuratat + " NU a fost gasit. Se face INSERT.");

                            String sqlInsert = "INSERT INTO StocLoturi (MedicamentID, CodLot, CantitateStoc, DataExpirare, PretVanzare) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

                            pstmtInsert.setInt(1, medicamentID);
                            pstmtInsert.setString(2, codLotCuratat);
                            pstmtInsert.setInt(3, cantitateAdaugata);
                            pstmtInsert.setDate(4, dataExpirareNoua);
                            pstmtInsert.setDouble(5, pretVanzareNou);
                            pstmtInsert.executeUpdate();

                            try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    lotNou.setLotID(generatedKeys.getInt(1));
                                } else {
                                    throw new SQLException("Eroare la obtinerea ID-ului pentru noul lot.");
                                }
                            }
                            pstmtInsert.close();
                        }

                        rs.close();
                        pstmtSelect.close();
                        conn.commit();

                        return lotNou;

                    } catch (Exception e) {
                        if (conn != null) conn.rollback();
                        System.err.println("DEBUG: Eroare majora in 'call()' la adaugare lot:");
                        e.printStackTrace();
                        throw new Exception("Eroare la salvarea lotului: " + e.getMessage(), e);
                    } finally {
                        if (conn != null) {
                            conn.setAutoCommit(true);
                            conn.close();
                        }
                    }
                }
            };

            insertLotTask.setOnSucceeded(e -> {
                StocView lotModificat = insertLotTask.getValue();

                // Actualizeaza listele UI inteligent, fara a reincarca tot tabelul
                actualizeazaItemInListaUI(stocuriList, lotModificat);
                actualizeazaItemInListaUI(stocVanzariList, lotModificat);

                stocuriTable.refresh();
                stocVanzariTable.refresh();

                afiseazaAlerta("Succes", null, "Lotul a fost adaugat sau actualizat in stoc.", Alert.AlertType.INFORMATION);
            });

            insertLotTask.setOnFailed(e ->  {
                afiseazaAlerta("Eroare Baza Date", null, "Eroare la adaugarea lotului: " + e.getSource().getException().getMessage(), Alert.AlertType.ERROR);
                e.getSource().getException().printStackTrace();
            });

            new Thread(insertLotTask).start();
        });
    }

    private boolean proceseazaVanzareaInBD() {
        Connection conn = null;
        try {
            // Folosim o tranzactie pentru a asigura integritatea datelor
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insereaza antetul vanzarii
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

            // 2. Insereaza detaliile vanzarii si actualizeaza stocul (in batch)
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

    // --- Functii ajutatoare ---

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

    private void afiseazaAlerta(String titlu, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
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
            System.err.println("Nu pot incarca imaginea: " + path);
            e.printStackTrace();
            return null;
        }
    }
}