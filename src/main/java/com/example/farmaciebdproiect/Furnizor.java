package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Furnizor {
    private final SimpleIntegerProperty furnizorID;
    private final SimpleStringProperty numeFurnizor;
    private final SimpleStringProperty cui;
    private final SimpleStringProperty telefon;

    public Furnizor(int furnizorID, String numeFurnizor, String cui, String telefon) {
        this.furnizorID = new SimpleIntegerProperty(furnizorID);
        this.numeFurnizor = new SimpleStringProperty(numeFurnizor);
        this.cui = new SimpleStringProperty(cui);
        this.telefon = new SimpleStringProperty(telefon);
    }

    // Gettere
    public int getFurnizorID() { return furnizorID.get(); }
    public String getNumeFurnizor() { return numeFurnizor.get(); }
    public String getCui() { return cui.get(); }
    public String getTelefon() { return telefon.get(); }
    public void setFurnizorID(int furnizorID) {
        this.furnizorID.set(furnizorID); // Corect: setezi valoarea din interiorul proprietății
    }
}