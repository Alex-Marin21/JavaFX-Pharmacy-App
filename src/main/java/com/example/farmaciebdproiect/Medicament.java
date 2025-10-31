package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Medicament {
    private final SimpleIntegerProperty medicamentID;
    private final SimpleStringProperty numeMedicament;
    private final SimpleStringProperty dozaj;
    // Puteti adauga si restul campurilor (FormaFarmaceutica etc.) daca doriti

    public Medicament(int id, String nume, String dozaj) {
        this.medicamentID = new SimpleIntegerProperty(id);
        this.numeMedicament = new SimpleStringProperty(nume);
        this.dozaj = new SimpleStringProperty(dozaj);
    }

    // Getters (necesari pentru PropertyValueFactory)
    public int getMedicamentID() {
        return medicamentID.get();
    }
    public String getNumeMedicament() {
        return numeMedicament.get();
    }
    public String getDozaj() {
        return dozaj.get();
    }

    // Setters (necesari pentru a seta ID-ul dupa insert)
    public void setMedicamentID(int id) {
        this.medicamentID.set(id);
    }
}