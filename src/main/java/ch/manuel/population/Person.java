//Autor: Manuel Schmocker
//Datum: 11.04.2020

package ch.manuel.population;

//Klasse zum Verwalten einer Person (Alter, Wohngemeinde, Netzwerk, Infektion)

import ch.manuel.geodata.Municipality;
import java.util.ArrayList;
import java.util.List;


public class Person {
    
    // Membervariablen
    private int age;
    private int index;                      // Index in Liste (Class Population)
    private int indexGemeinde;
    private Municipality municip;
    
    // Network
    private List<Integer> listNetwork;       // Liste mit indexes aus Personenliste in Population.java
    
    // Infection + Gesundheit
    private boolean isAlive;
    private Infection infection;
    
    // Constructor
    public Person( int age, Municipality home ) {
        // initialisation list
        listNetwork = new ArrayList<>();
        // initialisation of infection
        infection = new Infection( this );
        isAlive = true;
        
        this.age = age;
        this.municip = home;
    }
    
    // FUNCTIONS
    public void addNetworkLink( int index ) {
        this.listNetwork.add( index );
    }
    public void clearNetwork() {
        this.listNetwork.clear();
    }
    
    // setter
    public void setIndex(int index) {
        this.index = index;
    }
    public void setIndexMunicip(int index) {
        this.indexGemeinde = index;
    }
    public void dies() {
        this.isAlive = false;
    }
    public void resetLife() {
        this.isAlive = true;
    }
    
    // getter
    public int getIndex() {
        return this.index;
    }
    public int getIndexMunicip() {
        return this.indexGemeinde;
    }
    public int nbOfConnections() {
        return this.listNetwork.size();
    }
    public int getAge() {
        return this.age;
    }
    public Municipality getMunicip() {
        return this.municip;
    }
    public List<Integer> getListNetwork() {
        return this.listNetwork;
    }
    public Infection getInfection() {
        return this.infection;
    }
    public boolean isAlive() {
        return this.isAlive;
    }
}
