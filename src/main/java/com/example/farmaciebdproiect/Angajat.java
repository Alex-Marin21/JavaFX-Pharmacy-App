package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Aceasta clasa reprezinta un rand din tabela Angajati
public class Angajat {

    // Folosim 'Property' pentru ca JavaFX sa le poata "observa"
    private final SimpleIntegerProperty angajatID;
    private final SimpleStringProperty nume;
    private final SimpleStringProperty prenume;
    private final SimpleStringProperty codParafa;

    // Constructor
    public Angajat(int angajatID, String nume, String prenume, String codParafa) {
        this.angajatID = new SimpleIntegerProperty(angajatID);
        this.nume = new SimpleStringProperty(nume);
        this.prenume = new SimpleStringProperty(prenume);
        this.codParafa = new SimpleStringProperty(codParafa);
    }

    // Gettere (necesare pentru TableView)
    public int getAngajatID() {
        return angajatID.get();
    }

    public String getNume() {
        return nume.get();
    }

    public String getPrenume() {
        return prenume.get();
    }

    public String getCodParafa() {
        return codParafa.get();
    }
}