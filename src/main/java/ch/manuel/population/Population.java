//Autor: Manuel Schmocker
//Datum: 11.04.2020

package ch.manuel.population;

import ch.manuel.episimch.DataLoader;
import ch.manuel.geodata.GeoData;
import ch.manuel.episimch.gui.InputNetwork;
import ch.manuel.episimch.gui.MainFrame;
import ch.manuel.utilities.MyUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//Klasse zum Verwalten der Bevölkerung (Personen)

public class Population implements Runnable {
    
    private static List<Person> listPersons;            // Liste mit Personen
    private static double[] ageDistribution;
    private static float[] mortalityDist;
    private static final int INTERVAL_AGE = 5;          // Intervall Altersverteilung in Inputdatei: 5 Jahre
    private static String message;                      // Nachricht aus createNetwork
    private static boolean startOptimization;
    
    // Membervariablen: Netzwerk
    private static int sizePermGroup;
    private static int radiusPermGrup;
    private static final int IN_MUNICIP = 5;
    private static boolean netwerkIsCreated;
    
    
    // Constructor
    public Population() {
        // initialisation
        startOptimization = false;
        listPersons = new ArrayList<>();
        ageDistribution = new double[21];
        mortalityDist = new float[9];
        netwerkIsCreated = false;
        message = "";
    }
    
    // THREADS
    // Runnable
    @Override
    public void run() {
        if( startOptimization ) {
            // only optimization
            Population.optimizeNetwork();
        } else {
           // create network
            Population.createNetwork(); 
        }
        // Network created
        Population.setNetworkIsCreated( true );
    }
    
    // FUNKTIONEN
    private static void createNetwork() {
        int nbMunicip = DataLoader.geoData.getNbMunicip();
        
        // Verteilung der Kontakte
        int nbContactIn = (sizePermGroup > IN_MUNICIP) ? IN_MUNICIP : sizePermGroup;            // Kontakte innerhalb Gemeinde (max 5)
        
        // random number
        Random rand = new Random();
        
        // ---------
        // 1. KONTAKTE INNERHALB DER GEMEINDE
        for (int i = 0; i < nbMunicip; i++) {

            // create list with inhabitants of municipality (possible contacts)
            List<Integer>listContacts = new ArrayList<>();
            //get indexes for municipality i
            int[] arrContacts = DataLoader.geoData.getMunicip(i).getArrListPers();  
            for (int j = 0; j < arrContacts.length; j++ ) {
                listContacts.add( arrContacts[j] );  
            }
            
            // iterate through list
            while( listContacts.size() > 1 ) {
                // actPers -> last element of list
                Person actPers = Population.getPerson( listContacts.get( listContacts.size()-1 ) );
                int nbCont = actPers.nbOfConnections();
                
                // create links
                if( nbCont < nbContactIn ) {
                    for ( int k = 0; k < (nbContactIn-nbCont); k++ ) {
                        int tmpInd = rand.nextInt( listContacts.size()-1 );             // without last element
                        Person tmpPers = Population.getPerson( listContacts.get( tmpInd ) );
                        
                        // set connection 
                        actPers.addNetworkLink( listContacts.get( tmpInd ) );
                        tmpPers.addNetworkLink( listContacts.get( listContacts.size()-1 ) );
                        
                        // remove tmpPers from index, when target reached
                        if( tmpPers.nbOfConnections() >= nbContactIn ) {
                            listContacts.remove( tmpInd );
                        }
                        // exit loop, if only one element left
                        if( listContacts.size() < 2 ) {
                            break;
                        }
                    }
                }
                // remove actPers from index, when target reached
                listContacts.remove( listContacts.size()-1 );
            }
            // info text
            MainFrame.dialog2.setText("Netzwerk innerhalb Gemeinde\n"
                                   + "Gemeinde " + (i+1) + " von " + nbMunicip);
        }
        
        
        // ---------
        // 2. KONTAKTE AUSSERHALB DER GEMEINDE                 
        for (int i = 0; i < nbMunicip; i++) {

            List<Integer>listContacts = new ArrayList<>();
            int listSize = 0;
            
            // 2.1: create list with possible contacts (in proximity of municipality)
            for (int j = 0; j < nbMunicip; j++) {
                
                int km = DataLoader.geoData.getDistKM(i, j);
                if( km < radiusPermGrup) {
                    //get indexes for municipality j
                    int[] arrContacts = DataLoader.geoData.getMunicip(j).getArrListPers();             
                    for (int k = 0; k < arrContacts.length; k++ ) { 
                        // if max group size not yet reached -> add to list
                        if( Population.getPerson( arrContacts[k] ).nbOfConnections() < sizePermGroup ) {
                            listContacts.add( arrContacts[k] ); 
                        }
                    }
                }
            }
            listSize = listContacts.size();
            
            // 2.2: create list with inhabitants of municipality
            List<Integer>listInhabit = new ArrayList<>();
            //get indexes for municipality i
            int[] arrInhabit = DataLoader.geoData.getMunicip(i).getArrListPers();       
            for (int j = 0; j < arrInhabit.length; j++ ) {
                // if max group size not yet reached -> add to list
                if( Population.getPerson( arrInhabit[j] ).nbOfConnections() < sizePermGroup ) {
                    listInhabit.add( arrInhabit[j] );  
                }
            }

            // 2.3: iterate through list -> listInhabit
            boolean contactIsEmpty = false;
            while( listInhabit.size() > 1 ) {
                
                // index of act person
                int actInd = listInhabit.size()-1;
                
                // actPers -> last element of list Inhabitants
                Person actPers = Population.getPerson( listInhabit.get( actInd ) );
                int nbCont = actPers.nbOfConnections();
                
                // no extern contact possible
                if ( contactIsEmpty ) {    
                    // no contacts left... -> exit while
                    Population.message += actPers.getMunicip().getName() + ": Limit für ext. Verbindungen erreicht!\n"; 
                    break;
                
                // create links
                } else if( nbCont < sizePermGroup ) {
                    for ( int k = 0; k < (sizePermGroup-nbCont); k++ ) {
                        int tmpInd = rand.nextInt( listContacts.size() );
                        Person tmpPers = Population.getPerson( listContacts.get( tmpInd ) );
                        
                        // set connection 
                        actPers.addNetworkLink( listContacts.get( tmpInd ) );
                        tmpPers.addNetworkLink( listInhabit.get( actInd ) );
                        
                        // remove tmpPers from index, when target reached
                        if( tmpPers.nbOfConnections() >= sizePermGroup ) {
                            listContacts.remove( tmpInd );
                            // no contacts available?
                            if( listContacts.size() == 0 ) {
                                contactIsEmpty = true;
                            }
                        }                       
                    }
                }
                // remove actPers from index, when target reached
                listInhabit.remove( actInd );
            }
            // info text
            MainFrame.dialog2.setText("Netzwerk ausserhalb Gemeinde\n"
                                   + "Gemeinde " + (i+1) + " von " + nbMunicip
                                   + "\nAnzahl verfügbare Kontakte: " + listSize
                                   + "\nSuchradius: " + radiusPermGrup + " km");
        }
    }
    
    // verification of network
    private static void verificNetwork() {
        // info text
        MainFrame.dialog2.setText("Starte Überprüfung Netzwerk\n");
        // test values
        int nbSizeNotReached = 0;
        int minSize = Population.sizePermGroup;
        int maxSize = 0;
        int nbSelfRef = 0;
        long sum = 0;
        
        for ( int i = 0; i < Population.listPersons.size(); i++ ) {
            
            // actual Person to test
            Person actPers = Population.listPersons.get(i);
            // array network
            int[] net = actPers.getListNetwork().stream().mapToInt(Integer::intValue).toArray();
            
            // search min
            if( net.length < minSize) {minSize = net.length; }
            // search max
            if( net.length > maxSize) {maxSize = net.length; }
            // elements with wrong size
            if( net.length != Population.sizePermGroup ) {
                nbSizeNotReached++;
            }
            // sum
            sum += net.length;
            
            // reference to itself
            for ( int j = 0; j < net.length; j++) {
                if( i == net[j]) {nbSelfRef++; }
            }
            
        }
        // mean
        double mean = (double)sum / Population.listPersons.size() ;
        // info text
        MainFrame.dialog2.setText(Population.message +
                                  "Resultat:\n" +
                                  "Min Verbindungen: " + minSize + 
                                "\nMax Verbindungen: " + maxSize +
                                "\nMittelwert: " + mean +
                                "\nAnzahl Soll != Ist: " + nbSizeNotReached +
                                "\nAnzahl Ref. auf sich selbst: " + nbSelfRef);
    }
   
    // optimization of network
    private static void optimizeNetwork() {
        
        if( Population.netwerkIsCreated ) {
            int nbMunicip = DataLoader.geoData.getNbMunicip();
             
            // random number
            Random rand = new Random();
            // info text
            MainFrame.dialog2.setText("Start optimization...");
                
            // 1. DELETE REFERENCE TO ITSELF
            int counter = 0;
            for ( int i = 0; i < Population.listPersons.size(); i++ ) {
                // actual Person to test
                Person actPers = Population.listPersons.get(i);
                
                // list index: network
                int listInd = actPers.nbOfConnections() - 1;
                // reference to itself
                while( listInd >= 0) {
                    int ref = actPers.getListNetwork().get( listInd );
                    if( i == ref ) {
                        // delete ref to itself
                        actPers.getListNetwork().remove( listInd );
                        counter++;
                    }
                    listInd--;
                }
            }
            
            // info text
            MainFrame.dialog2.setText("Referenzen gelöscht: " + counter + "\n"
                                    + "Neue Verbindungen hinzufügen...");
                
            // 2. ADD NEW REFERENCES
            counter = 0;
            for ( int i = 0; i < Population.listPersons.size(); i++ ) {
                // actual Person to test
                Person actPers = Population.listPersons.get(i);
                int nbConn = actPers.nbOfConnections();
                
                if( nbConn < (Population.sizePermGroup) ) {
                    int indMun = actPers.getIndexMunicip();
                    
                    // create list with possible municipalities
                    // list municipalities within range
                    List<Integer>listMun = new ArrayList<>();
                    for (int j = 0; j < nbMunicip; j++) {
                        // get distance to 
                        int km = DataLoader.geoData.getDistKM(indMun, j);
                        if( km < radiusPermGrup) {
                            listMun.add(j);
                        }
                    }
                        
                    // choose random municipality
                    int index = listMun.get( rand.nextInt( listMun.size() ));
                    // choose random inhabitant
                    int[] arrInhabit = DataLoader.geoData.getMunicip( index ).getArrListPers();
                    Person tmpPers = Population.getPerson( arrInhabit[rand.nextInt( arrInhabit.length )] );
                    // set connection 
                    actPers.addNetworkLink( tmpPers.getIndex() );
                    tmpPers.addNetworkLink( actPers.getIndex() );
                    counter++;

                }
            }
            // info text
            MainFrame.dialog2.setText("Neue Verbindungen: " + counter + "\n"
                                    + "Überzählige Verbindungen löschen...");
                
            // 3. DELETE TO MUCH REFERENCES
            counter = 0;
            for ( int i = 0; i < Population.listPersons.size(); i++ ) {
                // actual Person to test
                Person actPers = Population.listPersons.get(i);
                int nbConn = actPers.nbOfConnections();
                if( nbConn > (Population.sizePermGroup) ) {
                    int tmpInd = rand.nextInt(nbConn);
                    actPers.getListNetwork().remove( tmpInd );
                    counter++;
                }
            }
            // info text
            MainFrame.dialog2.setText("Neue Verbindungen: " + counter + "\n"
                                    + "Überzählige Verbindungen löschen...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // verification
        Population.verificNetwork();
    }
    
    // create population (objects)
    public void createPop() {
        
        int nbMunicip = GeoData.getNbMunicip();
        
        // create population for each municipality
        for (int i = 0; i < nbMunicip; i++) {
            int pop = GeoData.getMunicip( i ).getPopulation();
            
            // create array with indexes for Municip i
            int[] tmpList = new int[pop];
            
            // calculate distribution
            int[] dist = new int[101];
            int sum = 0;
            for (int j = 0; j <= 100; j++) {
                dist[j] = Math.round( pop * Population.getFractionOfPop(j) );
                sum += dist[j];
            }
            // correction of differencies
            if( pop != sum ) {
                int diff = pop - sum;
                
                int nbPos = 50;      // auf Anzahl Positionen verteilen (von 0 Jahren bis x Jahren)
                int counter = 0;
                while( diff != 0) {
                    
                    if( diff > 0){
                        dist[counter % nbPos]++;
                        diff--;
                    } else {
                        if( dist[counter % nbPos] > 0 ) {
                            dist[counter % nbPos]--;
                            diff++;
                        }
                    }
                    counter++;
                    
                    // abbruch
                    if( counter > 1000 ) {
                        // absichtlich fehlerhafter Eintrag
                        dist[100] = 1000000;
                    }
                }
            }
            
            // personen von 0 bis 100 Jahren
            int counter = 0;
            for (int j = 0; j <= 100; j++) {
                // distribution corrected
                int nb = dist[j];
                
                // create person
                for (int k = 0; k < nb; k++) {
                    // create new Person
                    Population.listPersons.add( new Person(j, GeoData.getMunicip( i ) ));
                    // update index list in object
                    int lastIndex = Population.getLastIndex();
                    Population.listPersons.get( lastIndex ).setIndex( lastIndex );
                    Population.listPersons.get( lastIndex ).setIndexMunicip( i );
                    // list with indexes per municipality
                    tmpList[counter] = lastIndex;
                    counter++;
                }
            }
            GeoData.getMunicip( i ).setListPers( tmpList );
        }
    }
    
    // reset infection
    public static void resetInfection() {
        for ( int i = 0; i < Population.listPersons.size(); i++ ) {
            // reset infection
            Population.listPersons.get(i).getInfection().resetInfection();
            // reset population alive
            Population.listPersons.get(i).resetLife();
        }
    }  
    
    // setter
    public static void setSizePermGroup(int size) {
        Population.sizePermGroup = size;
    }
    public static void setRadiusPermGrup(int km) {
        Population.radiusPermGrup = km;
    }
    public static void setStartOptimization( boolean bool ) {
        Population.startOptimization = bool;
    }
    public static void setNetworkIsCreated(boolean bool) {
        if( bool ) {
            Population.verificNetwork();
        }
        Population.netwerkIsCreated = bool;
        InputNetwork.setIsCreated(bool );
    }

    
    // set age distribution
    public void setAgeDistr( int lowerBound, double fract ) {
        // 0    0 ... 4 Jahre
        // 1    5 ... 9 Jahre
        // 2    10...14 Jahre
        // 20   100 Jahre
        if( lowerBound < 101 ) {
            int index = Math.floorDiv(lowerBound, INTERVAL_AGE );
        
            ageDistribution[index] = fract;
        }
        
    }
    // set distribution of mortality
    public static void setMortDistr( float[] mort ) {
        if( mort.length == 9 ) {
            Population.mortalityDist = mort;
        } else {
            MyUtilities.getErrorMsg("Error Input", "Array mit Sterblichkeit hat falsche grösse");
        }
    }
    // get Anteil Alter in Gesamtbevölkerung
    public static float getFractionOfPop( int age ) {
        if( age < 0 ) {
            return 0;
        } else if ( age < 100 ) {
            int index = Math.floorDiv(age, INTERVAL_AGE );
            return (float) ageDistribution[index] / INTERVAL_AGE;
        } else if ( age == 100 ) {
            return (float) ageDistribution[20];
        } else {
            return 0;
        }
    }
    // get Sterblichkeit für Altersklasse
    public static float getMortality( int age ) {
        int index = Math.floorDiv( age, 10 );
        if( age < 0 ) {
            return 0;
        } else if ( age < 80 ) {
            return mortalityDist[index];
        } else {
            return mortalityDist[8];
        }
    }
                
    // getter
    public static int getNbPersons() {
        return Population.listPersons.size();
    }
    private static int getLastIndex() {
        return Population.listPersons.size() - 1;
    }
    public static Person getPerson(int index) {
        return Population.listPersons.get(index);
    }
    public static boolean getNetworkIsCreated() {
        return Population.netwerkIsCreated;
    }
 
}
