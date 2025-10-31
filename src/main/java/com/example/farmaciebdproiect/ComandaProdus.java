package com.example.farmaciebdproiect;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Reprezinta un produs cu stoc total scazut
public class ComandaProdus {
    private final SimpleIntegerProperty medicamentID;
    private final SimpleStringProperty numeMedicament;
    private final SimpleIntegerProperty totalStoc;

    public ComandaProdus(int medicamentID, String numeMedicament, int totalStoc) {
        this.medicamentID = new SimpleIntegerProperty(medicamentID);
        this.numeMedicament = new SimpleStringProperty(numeMedicament);
        this.totalStoc = new SimpleIntegerProperty(totalStoc);
    }

    public int getMedicamentID() { return medicamentID.get(); }
    public String getNumeMedicament() { return numeMedicament.get(); }
    public int getTotalStoc() { return totalStoc.get(); }
}