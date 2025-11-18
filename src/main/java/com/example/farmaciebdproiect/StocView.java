package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class StocView {

    private final SimpleIntegerProperty lotID;
    private final SimpleStringProperty numeMedicament;
    private final SimpleStringProperty dozaj;
    private final SimpleStringProperty codLot;
    private final SimpleIntegerProperty cantitateStoc;
    private final SimpleStringProperty dataExpirare; // Este un String
    private final SimpleDoubleProperty pretVanzare;

    public StocView(int lotID, String numeMedicament, String dozaj, String codLot, int cantitateStoc, LocalDate dataExpirare, double pretVanzare) {
        this.lotID = new SimpleIntegerProperty(lotID);
        this.numeMedicament = new SimpleStringProperty(numeMedicament);
        this.dozaj = new SimpleStringProperty(dozaj);
        this.codLot = new SimpleStringProperty(codLot);
        this.cantitateStoc = new SimpleIntegerProperty(cantitateStoc);
        // Convertim LocalDate in String
        this.dataExpirare = new SimpleStringProperty(dataExpirare.toString());
        this.pretVanzare = new SimpleDoubleProperty(pretVanzare);
    }

    // Gettere
    public int getLotID() { return lotID.get(); }
    public String getNumeMedicament() { return numeMedicament.get(); }
    public String getDozaj() { return dozaj.get(); }
    public String getCodLot() { return codLot.get(); }
    public int getCantitateStoc() { return cantitateStoc.get(); }
    public String getDataExpirare() { return dataExpirare.get(); }
    public double getPretVanzare() { return pretVanzare.get(); }


    public void setLotID(int lotID) {
        this.lotID.set(lotID);
    }

    public void setCantitateStoc(int cantitateStoc){
        this.cantitateStoc.set(cantitateStoc);
    }

    public void setDataExpirare(LocalDate dataExpirare){
        this.dataExpirare.set(dataExpirare.toString());
    }

    public void setPretVanzare(double pretVanzare){
        this.pretVanzare.set(pretVanzare);
    }
}