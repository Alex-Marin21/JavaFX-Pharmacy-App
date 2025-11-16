package com.example.farmaciebdproiect;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Date;

// Clasa model pentru a afisa datele în noul tabel de raport
public class VanzareRaport {

    private final SimpleIntegerProperty vanzareID;
    private final SimpleStringProperty dataVanzare;
    private final SimpleDoubleProperty totalBon;

    public VanzareRaport(int vanzareID, Date dataVanzare, double totalBon) {
        this.vanzareID = new SimpleIntegerProperty(vanzareID);
        this.dataVanzare = new SimpleStringProperty(dataVanzare.toString());
        this.totalBon = new SimpleDoubleProperty(totalBon);
    }

    // Gettere necesare pentru TableView
    public int getVanzareID() {
        return vanzareID.get();
    }

    public String getDataVanzare() {
        return dataVanzare.get();
    }

    public double getTotalBon() {
        return totalBon.get();
    }
}