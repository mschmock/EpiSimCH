//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.geodata;

// Klasse zum Verwalten der Daten pro Gemeinde

import ch.manuel.utilities.MyUtilities;
import java.util.ArrayList;
import java.util.List;

public class Municipality {
    // Class attributes
    private String name;                        // Name Gemeinde
    private int id;                             // BFS id nummer
    private int index;                          // index in Liste (Class GeoData)
    private int centerN;                        // Zentrum: Koordinate LV 95 N
    private int centerE;                        // Zentrum: Koordinate LV 95 E
    private int minN;                           // Umgrenzung: min Koordinate LV 95 N
    private int maxN;                           // Umgrenzung: max Koordinate LV 95 N
    private int minE;                           // Umgrenzung: min Koordinate LV 95 E
    private int maxE;                           // Umgrenzung: max Koordinate LV 95 E
    private int population;                     // Einwohner
    private int haushalte;                      // Anz. Haushalte
    // Gemeindegrenzen
    private List<int[]> polyX;                  // Polygone Gemeindegrenze, X-Werte
    private List<int[]> polyY;                  // Polygone Gemeindegrenze, Y-Werte
    // Liste mit Personen
    private int[] listIndexPers;                // Index in Class population
    // Daten aus Infektion
    private int nbInfections;                   // Anzahl Infektionen in der Gemeinde
    private float nbInfectionsRel;              // Anzahl Infektionen in der Gemeinde pro 1000 Einwohner
    private int nbDeath;                        // Anzahl Tote in der Gemeinde
    private int nbImmune;                       // Anzahl Immunisierte in der Gemeinde
    private float K0;                           // Basisreproduktionszahl K0 in der Gemeinde
    private static int maxInfections;           // max Infektionen gesamt -> static
    private static float maxInfectionsRel;        // max Infektionen pro 1000 Einwohnler -> static
    private static int maxDeath;                // max Tote gesamt -> static
    private static int maxImmune;               // max Immunisierte gesamt -> static
    private static float maxK0;                 // max Wert für Baissrep. K0 -> static
    
    // Constructor
    public Municipality(String name) {
        this.name = name;
        
        // initialisieren
        polyX = new ArrayList<>();
        polyY = new ArrayList<>();
    }
    
    // Methodes: setter
    public void setListPers(int[] arr) {
        this.listIndexPers = arr;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setCenterN(int centerN) {
        this.centerN = centerN;
    }
    public void setCenterE(int centerE) {
        this.centerE = centerE;
    }
    public void setMinN(int minN) {
        this.minN = minN;
    }
    public void setMaxN(int maxN) {
        this.maxN = maxN;
    }
    public void setMinE(int minE) {
        this.minE = minE;
    }
    public void setMaxE(int maxE) {
        this.maxE = maxE;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    public void setHouseholds(int nb ) {
        this.haushalte = nb;
    }
    public void setNbInfections( int nb ) {
        this.nbInfections = nb;
    }
    public void setNbInfectPerInhab( int nb ) {
        this.nbInfectionsRel = (float) nb / this.population * 1000f;
    }
    public void setNbDeath( int nb ) {
        this.nbDeath = nb;
    }
    public void setNbImmune( int nbImmune ) {
        this.nbImmune = nbImmune;
    }
    public void setR0( float K0 ) {
        this.K0 = K0;
    }
    public static void setMaxInfections( int nb ) {
        Municipality.maxInfections = nb;
    }     
    public static void setMaxInfectPerInhab( float val ) {
        Municipality.maxInfectionsRel = val;
    }     
    public static void setMaxDeath( int nb ) {
        Municipality.maxDeath = nb;
    }
    public static void setMaxImmune( int nb ) {
        Municipality.maxImmune = nb;
    }
    public static void setMaxR0( float val ) {
        Municipality.maxK0 = val;
    }
    
    public boolean setPolygon(String polygon) {
        
        boolean isValid = true;
        
        // READ INPUT: "x1 y1, x2 y2, ..."
        // Gemeinde mit mehreren Polygons:
        // separator: ")), (("
        if( polygon.contains(")), ((") ) {

            String[] polygonSep = polygon.split("\\)\\), \\(\\(");
            // loop through polygons
            for( String poly : polygonSep ) {
                // parse polygon: stringToPoly
                if( !stringToPoly( poly ) ) {
                    isValid = false;
                    break;
                }
            }
        // no separator -> nur ein polygon
        } else {
            // parse polygon: stringToPoly
            isValid = stringToPoly( polygon );
        }
        
        return isValid;
    }
    
    private boolean stringToPoly(String polygon) {
            
        boolean isValid = true;
        
        // Trennzeichen für Seen: "), (" -> ignorieren
        if( polygon.contains("), (") ) {
            // nur 1. Element verwenden
            polygon = polygon.split("\\), \\(")[0];
        }
        
        // temp arry with x and y values
        List<Integer> xVals = new ArrayList<Integer>();
        List<Integer> yVals = new ArrayList<Integer>();

        // Split ","
        if( polygon.contains(",") ) {
            String[] points = polygon.split(",");

            // Split " "
            for( String a : points) {
                if( a.contains(" ") ) {
                    String[] b = a.trim().split("\\s+");

                    // only two elements
                    if( b.length != 2 ) {return false;}

                    // test if numeric
                    // x-value
                    if( MyUtilities.isNumeric( b[0] )) {
                        xVals.add((int) Double.parseDouble( b[0] ) );
                    } else {
                        // not valid
                        return false;

                    }
                    // y-value
                    if( MyUtilities.isNumeric( b[1] )) {
                        yVals.add((int) Double.parseDouble( b[1] ) );
                    } else {
                        // not valid
                        return false;

                    }
                } else {
                    // not valid
                    return false;

                }
            }
        } else {
            //not valid
            return false;
        }

        // ok? 
        // store data in member variable
        if( isValid ) {
            this.polyX.add( xVals.stream().mapToInt(Integer::intValue).toArray() );
            this.polyY.add( yVals.stream().mapToInt(Integer::intValue).toArray() );
            //this.polyX = xVals.toArray( new Integer[xVals.size()] );
            //this.polyY = yVals.toArray( new Integer[yVals.size()] );

        }

        return isValid;
    }
    
    // Methodes: getter
    public int[] getArrListPers() {
        return this.listIndexPers;
    }
    public int getIndex() {
        return this.index;
    }
    public String getName() {
        return this.name;
    }
    public int getID() {
        return this.id;
    }
    public int getCenterN() {
        return this.centerN;
    }
    public int getCenterE() {
        return this.centerE;
    }
    public int getMinN() {
        return this.minN;
    }
    public int getMaxN() {
        return this.maxN;
    }
    public int getMinE() {
        return this.minE;
    }
    public int getMaxE() {
        return this.maxE;
    }
    public int getPopulation() {
        return this.population;
    }
    public float nbPerHousehold() {
        return this.population / this.haushalte;
    }
    public List<int[]> getPolyX() {
        return this.polyX;
    }
    public List<int[]> getPolyY() {
        return this.polyY;
    }
    public int getNbInfections() {
        return this.nbInfections;
    }
    public float getNbInfectPerInhab() {
        return this.nbInfectionsRel;
    }
    public int getNbDeath() {
        return this.nbDeath;
    }
    public int getNbImmune() {
        return this.nbImmune;
    }
    public float getK0() {
        return this.K0;
    }
    public static int getMaxInfections() {
        return Municipality.maxInfections;
    }     
    public static float getMaxInfectPerInhab() {
        return Municipality.maxInfectionsRel;
    }     
    public static int getMaxDeath() {
        return Municipality.maxDeath;
    }
    public static int getMaxImmune() {
        return Municipality.maxImmune;
    }
    public static float getMaxK0() {
        return Municipality.maxK0;
    }
    
}
