package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Reprezinta un rand din bonul curent (cosul de cumparaturi)
public class BonItem {

    private final SimpleIntegerProperty lotID;
    private final SimpleStringProperty numeMedicament;
    private final SimpleIntegerProperty cantitateVanduta;
    private final SimpleDoubleProperty pretUnitar;
    private final SimpleDoubleProperty pretTotal;
    private final int stocDisponibil; // Il tinem minte pt validari

    public BonItem(int lotID, String numeMedicament, int cantitateVanduta, double pretUnitar, int stocDisponibil) {
        this.lotID = new SimpleIntegerProperty(lotID);
        this.numeMedicament = new SimpleStringProperty(numeMedicament);
        this.cantitateVanduta = new SimpleIntegerProperty(cantitateVanduta);
        this.pretUnitar = new SimpleDoubleProperty(pretUnitar);
        this.pretTotal = new SimpleDoubleProperty(pretUnitar * cantitateVanduta);
        this.stocDisponibil = stocDisponibil;
    }

    // Gettere
    public int getLotID() { return lotID.get(); }
    public String getNumeMedicament() { return numeMedicament.get(); }
    public int getCantitateVanduta() { return cantitateVanduta.get(); }
    public double getPretUnitar() { return pretUnitar.get(); }
    public double getPretTotal() { return pretTotal.get(); }
    public int getStocDisponibil() { return stocDisponibil; }

    // Settere
    public void setCantitateVanduta(int cantitate) {
        this.cantitateVanduta.set(cantitate);
        this.pretTotal.set(this.pretUnitar.get() * cantitate);
    }
}