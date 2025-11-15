package com.example.farmaciebdproiect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clasa utilitara pentru a rula interogarile simple (JOIN)
 * si complexe (Subcereri) cerute de proiect.
 *
 * Rezultatele sunt afisate in consola (System.out.println).
 */
public class DatabaseQueries {

    // --- 4. INTEROGARI SIMPLE (CU JOIN) ---

    /**
     * JOIN (x3): Afișează toate vânzările (ID, Data, Total)
     * procesate de un anumit angajat.
     */
    public static void getVanzariPentruAngajat(String numeAngajat) {
        System.out.println("\n--- [QUERY 1 - JOIN] Vanzarile pentru angajatul: " + numeAngajat + " ---");
        String sql = "SELECT v.VanzareID, v.DataVanzare, v.TotalBon " +
                "FROM Vanzari v " +
                "JOIN Angajati a ON v.AngajatID = a.AngajatID " +
                "WHERE a.Nume = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeAngajat);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(
                            "  VanzareID: " + rs.getInt("VanzareID") +
                                    ", Data: " + rs.getDate("DataVanzare") +
                                    ", Total: " + rs.getDouble("TotalBon") + " Lei"
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JOIN (x3): Afișează medicamentele, loturile și cantitățile
     * vândute pentru un anumit ID de bon (VanzareID).
     */
    public static void getDetaliiVanzare(int vanzareID) {
        System.out.println("\n--- [QUERY 2 - JOIN] Detaliile vanzarii cu ID: " + vanzareID + " ---");
        String sql = "SELECT m.NumeMedicament, s.CodLot, vd.CantitateVanduta " +
                "FROM VanzariDetalii vd " +
                "JOIN StocLoturi s ON vd.LotID = s.LotID " +
                "JOIN Medicamente m ON s.MedicamentID = m.MedicamentID " +
                "WHERE vd.VanzareID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vanzareID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(
                            "  Medicament: " + rs.getString("NumeMedicament") +
                                    ", Lot: " + rs.getString("CodLot") +
                                    ", Cantitate: " + rs.getInt("CantitateVanduta")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JOIN (x4): Afișează stocul curent (Medicament, Lot, Cantitate)
     * care provine de la un anumit furnizor.
     */
    public static void getStocDeLaFurnizor(String numeFurnizor) {
        System.out.println("\n--- [QUERY 3 - JOIN] Stocul primit de la: " + numeFurnizor + " ---");
        String sql = "SELECT m.NumeMedicament, s.CodLot, s.CantitateStoc " +
                "FROM StocLoturi s " +
                "JOIN Medicamente m ON s.MedicamentID = m.MedicamentID " +
                "JOIN MedicamenteFurnizori mf ON m.MedicamentID = mf.MedicamentID " +
                "JOIN Furnizori f ON mf.FurnizorID = f.FurnizorID " +
                "WHERE f.NumeFurnizor = ? AND s.CantitateStoc > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeFurnizor);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(
                            "  Medicament: " + rs.getString("NumeMedicament") +
                                    ", Lot: " + rs.getString("CodLot") +
                                    ", Cantitate: " + rs.getInt("CantitateStoc")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --- 5. INTEROGARI COMPLEXE (CU SUBCERERI) ---

    /**
     * Subcerere (WHERE NOT IN): Afișează medicamentele din nomenclator
     * care nu au niciun lot în stoc (cantitate 0).
     */
    public static void getMedicamenteFaraStoc() {
        System.out.println("\n--- [QUERY 4 - COMPLEXA] Medicamente FARA stoc ---");
        String sql = "SELECT NumeMedicament, Dozaj FROM Medicamente " +
                "WHERE MedicamentID NOT IN (SELECT DISTINCT MedicamentID FROM StocLoturi WHERE CantitateStoc > 0)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        "  Nume: " + rs.getString("NumeMedicament") +
                                ", Dozaj: " + rs.getString("Dozaj")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Subcerere (WHERE NOT IN): Afișează angajații care
     * nu au înregistrat nicio vânzare până acum.
     */
    public static void getAngajatiFaraVanzari() {
        System.out.println("\n--- [QUERY 5 - COMPLEXA] Angajati FARA vanzari ---");
        String sql = "SELECT Nume, Prenume FROM Angajati " +
                "WHERE AngajatID NOT IN (SELECT DISTINCT AngajatID FROM Vanzari)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        "  Nume: " + rs.getString("Nume") +
                                ", Prenume: " + rs.getString("Prenume")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Subcerere (WHERE > (SELECT AVG...)): Afișează loturile
     * a căror cantitate este mai mare decât media stocului.
     */
    public static void getLoturiPesteMedie() {
        System.out.println("\n--- [QUERY 6 - COMPLEXA] Loturi cu stoc peste medie ---");
        String sql = "SELECT m.NumeMedicament, s.CodLot, s.CantitateStoc " +
                "FROM StocLoturi s " +
                "JOIN Medicamente m ON s.MedicamentID = m.MedicamentID " +
                "WHERE s.CantitateStoc > (SELECT AVG(CantitateStoc) FROM StocLoturi)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        "  Medicament: " + rs.getString("NumeMedicament") +
                                ", Lot: " + rs.getString("CodLot") +
                                ", Cantitate: " + rs.getInt("CantitateStoc")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Subcerere (WHERE = (SELECT MAX...)): Afișează furnizorii
     * care livrează cel mai scump produs aflat în stoc.
     */
    public static void getFurnizorCelMaiScumpProdus() {
        System.out.println("\n--- [QUERY 7 - COMPLEXA] Furnizor(i) pentru cel mai scump produs ---");
        String sql = "SELECT DISTINCT f.NumeFurnizor " +
                "FROM Furnizori f " +
                "JOIN MedicamenteFurnizori mf ON f.FurnizorID = mf.FurnizorID " +
                "JOIN StocLoturi s ON mf.MedicamentID = s.MedicamentID " +
                "WHERE s.PretVanzare = (SELECT MAX(PretVanzare) FROM StocLoturi)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("  Nume Furnizor: " + rs.getString("NumeFurnizor"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}