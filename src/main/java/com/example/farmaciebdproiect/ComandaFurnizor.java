package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Reprezinta un furnizor care poate livra un produs
public class ComandaFurnizor {
    private final SimpleStringProperty numeFurnizor;
    private final SimpleIntegerProperty timpLivrare;

    public ComandaFurnizor(String numeFurnizor, int timpLivrare) {
        this.numeFurnizor = new SimpleStringProperty(numeFurnizor);
        this.timpLivrare = new SimpleIntegerProperty(timpLivrare);
    }

    public String getNumeFurnizor() { return numeFurnizor.get(); }
    public int getTimpLivrare() { return timpLivrare.get(); }
}