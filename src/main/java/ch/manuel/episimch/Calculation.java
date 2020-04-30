//Autor: Manuel Schmocker
//Datum: 12.04.2020

package ch.manuel.episimch;

// calculation kernel

import ch.manuel.geodata.Municipality;
import ch.manuel.graphics.InputCalc;
import ch.manuel.graphics.XY_Chart;
import ch.manuel.graphics.MainFrame;
import ch.manuel.population.Infection;
import ch.manuel.population.Person;
import ch.manuel.population.Population;
import ch.manuel.utilities.MyUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Calculation implements Runnable {
    
    // membervariables
    private static int nbInfectionsStart;       // Anzahl Infizierte beim Start, Tag 0
    private static int nbImmunesStart;          // Anzahl Immune beim Start, Tag 0
    private static int randContPerDay;          // Anzahl Zufallskontakte pro Tag
    private static int permContPerDay;          // Anzahl Kontakte aus Netzwerk pro Tag
    private static float probaTransmition;      // Wahrscheinlichkeit für Übertragung
    private static int dayToRecovery;           // Anzahl Tage bis zu Genesung
    private static int varRecovery;             // Variabilität Genesung in Tage
    
    // calculation
    private static int day;
    private static int maxDays;
    // Gesamtdaten Infektion
    private static int dailyInfections;
    private static int sumInfections;
    private static int dailyImmunes;
    private static int dailyDeath;
    private static int nbTransmitions;
    
    // thread control
    private static volatile boolean exit = false;
    
    // Constructor
    public Calculation() {
        Calculation.day = 0;
    }
    
    
    // start calculation as thread
    @Override
    public void run() {
        // test start 
        boolean isOK = testStart();
        // define infection on day 0
        if( day == 0 || day >= (maxDays-1) ) {
            // init calculations / values
            Calculation.initValues();
            Calculation.initInfections();
            // draw resultats
            MainFrame.repaintCustomPanel();
        } else {
            exit = false;
        }
        // start calculation
        if( isOK ) {
            while( !exit ) {
                // info text
                //int nbInf = Infection.getSumInfections( day ) - Infection.getSumImmunes() - Infection.getSumDeath();
                MainFrame.setStatusText("Calculation: Day " + day + ", Nb of infections: " + sumInfections +
                                        ", mean R0: " + MyUtilities.getStringFromDbl( Infection.getMeanR0() ));
                // calculate contacts
                Calculation.calcContacts();
                // update infections
                Calculation.updateInfections();
                // update data for population
                Calculation.updateData();
                // update chart
                Calculation.updateChart();
                // draw resultats
                MainFrame.repaintCustomPanel();
                // next day
                day++;
                
                // check exit
                if( day >= maxDays ) { exit = true; }
            }
        }
        
        
    }
    
    // FUNCTIONS
    private static boolean testStart() {
        if( !Population.getNetworkIsCreated()) {
            MyUtilities.getErrorMsg("Netzwerk", "Kein soz. Netzwerk vorhanden");
            return false;
        }
        if( !InputCalc.isInputOK() ) {
             MyUtilities.getErrorMsg("Input", "Input Parameter fehlerhaft");
            return false;
        }
        return true;
    }
    // init values
    private static void initValues() {
        // check max days
        if( maxDays > 1 ) {
            Calculation.exit = false;
        }
        // set size of array for results
        Infection.initArray( Calculation.maxDays );
    }
    // prepare start calculation
    private static void initInfections() {
        // 1. RECOVERY & MORTALITY
        // random number
        Random randRec = new Random();
        Random randMort = new Random();
        
        int nb = Population.getNbPersons();
        for( int i = 0; i < nb; i++ ) {
            Person actPers = Population.getPerson(i);
            // set days to recorvery
            int days = dayToRecovery + randRec.nextInt(varRecovery);
            if( days < 1 ) { days = 1; }
            actPers.getInfection().setDaysToRecovery( days );
            // set mortality
            int age = actPers.getAge();
            float mortality = Population.getMortality(age);
            if( randMort.nextFloat() < mortality ) {
                // --> will not recover
                actPers.getInfection().setNoRecovery();
            }
        }
        // 2. RANDOM INFECTIONS + IMMUNES
        Random rand = new Random();
        
        double probInf = (double) nbInfectionsStart / nb;       // probability of infection
        double probImm = (double) nbImmunesStart / nb;          // probability of immunisation
        // counter
        int nbInf = 0;
        int nbImm = 0;
        // limit reached?
        boolean fillUpInf = true;
        boolean fillUpImm = true;
        // probInf + probImm =< 1.0
        for( int i = 0; i < nb; i++ ) {
            
            double probab = rand.nextDouble();
            // 2.1 INFECTIONS
            if( (probab < probInf) && fillUpInf ) {
                Population.getPerson( i ).getInfection().setIsInfected( 0 );
                nbInf++;
                fillUpInf = nbInf < nbInfectionsStart ? true : false;
            }
            // 2.2 IMMUNES
            if( ((1-probImm) < probab) && fillUpImm ) {
                Population.getPerson( i ).getInfection().setIsImmune();
                nbImm++;
                fillUpImm = nbImm < nbImmunesStart ? true : false;
            }   
        }
        
        // 3. CORRECTION
        // if limit not reached
        List<Integer>listPers = new ArrayList<>();
        // create list with persons (without infection)
        for( int i = 0; i < nb; i++ ) {
            Person pers = Population.getPerson( i );
            if( !pers.getInfection().isInfected() ) {
                listPers.add(i);
            }
        }
        // add infections
        for( int i = 0; i < (nbInfectionsStart-nbInf); i++ ) {
            int nbPers = listPers.size();
            int ind = listPers.get( rand.nextInt(nbPers) );
            // set infections
            Population.getPerson( ind ).getInfection().setIsInfected( 0 );
            listPers.remove( ind );
        }
        // add immunes
        for( int i = 0; i < (nbImmunesStart-nbImm); i++ ) {
            int nbPers = listPers.size();
            int ind = listPers.get( rand.nextInt(nbPers) );
            // set immunes
            Population.getPerson( i ).getInfection().setIsImmune();
            listPers.remove( ind );
        }
    }
    
    // calculate contacts
    private static void calcContacts() {
        int nb = Population.getNbPersons();
        Random randTrans = new Random();
        Random randCont = new Random();
        
        // iterate through population
        for (int i = 0; i < nb; i++) {
            // actual Person to test
            Person actPers = Population.getPerson(i);

            // check persons who can transmit a infection (isInfectious)
            if( actPers.getInfection().isInfectious( day )) {
                
                // 1. PERMANENT CONTACTS
                // array contacts per person
                int[] arrContacts = actPers.getListNetwork().stream().mapToInt(Integer::intValue).toArray();
                // itaration through contacts
                for( int j = 0; j < Calculation.permContPerDay; j++ ) { 
                    int tmpInd = randCont.nextInt( arrContacts.length );
                    // temp. Person
                    Person tmpPers = Population.getPerson( arrContacts[tmpInd] );
                    
                    // transmition of infection
                    if( randTrans.nextFloat() < Calculation.probaTransmition ) {
                        // infection is transmitted --> return true
                        if( tmpPers.getInfection().setIsInfected( day ) ) {
                            // add new infection to counter R0
                            actPers.getInfection().transmitionSuccessful();
                        }
                    }
                }
                
                // 2. RANDOM CONTACTS
                for( int j = 0; j < Calculation.randContPerDay; j++ ) {
                    int tmpInd = randCont.nextInt( nb );
                    // no reference to itself: nb != tmpInd
                    // goto next referenc (exept last element)
                    if( i == tmpInd ) {
                        if( i == (nb-1) ) {
                            tmpInd = 0;
                        } else {
                            tmpInd = i+1;
                        }
                    }
                    // temp. Person
                    Person tmpPers = Population.getPerson( tmpInd );
                    
                    // transmition of infection
                    if( randTrans.nextFloat() < Calculation.probaTransmition ) {
                        // infection is transmitted --> return true
                        if( tmpPers.getInfection().setIsInfected( day ) ) {
                            // add new infection to counter R0
                            actPers.getInfection().transmitionSuccessful();
                        }
                    }
                }
            }
        }
    }
    
    // update infection
    private static void updateInfections() {
        int nb = Population.getNbPersons();
        
        // iterate through population
        for (int i = 0; i < nb; i++) {
            // upate infecton-data per person
            Population.getPerson(i).getInfection().updateInfection( day );
        }
    }
    
    // calculate/update infection-data
    public static void updateData() {
        // reset counter for sums
        Calculation.dailyInfections = 0;
        Calculation.sumInfections = 0;
        Calculation.dailyDeath = 0;
        Calculation.dailyImmunes = 0;
        Calculation.nbTransmitions = 0;
        
        // var for max data
        int maxDeath = 0;
        int maxInfected = 0;
        int maxImmune = 0;
        float maxInfectedRel = 0f;
        // values for R0 overall data
        float maxR0 = 0;

        
        int nbMunicip = DataLoader.geoData.getNbMunicip();
        
        for (int i = 0; i < nbMunicip; i++) {
            // temp values for each Municip
            int nbDeath = 0;
            int nbInfected = 0;
            int nbImmune = 0;
            float r0 = 0;
            int countR0 = 0;
            
            int[] inhabitants = DataLoader.geoData.getMunicip(i).getArrListPers();             
            for (int j = 0; j < inhabitants.length; j++ ) {
                Infection actInfec = Population.getPerson( inhabitants[j] ).getInfection();
                
                // count infections
                if( actInfec.isInfected() ) {
                    // municipality (overall)
                    nbInfected++;
                    Calculation.sumInfections++;
                    // count only for today (act day)
                    if( actInfec.getDayOfInfection() == day ) {
                        Calculation.dailyInfections++;
                    }
                }
                
                // count immunisation + R0
                if( actInfec.isImmune() ) {
                    // municipality (overall)
                    nbImmune++;     
                    // R0 municipality (overall)
                    r0 += (float) actInfec.getNbTransmR0();
                    countR0++;
                    // count only for today (act day)
                    if( actInfec.getDayEndOfInfection() == day ) {
                        Calculation.dailyImmunes++;
                        // nb transmitions for specific day
                        Calculation.nbTransmitions += actInfec.getNbTransmR0();
                    }
                }
                // count death
                if( !Population.getPerson( inhabitants[j] ).isAlive() ) {
                    // municipality (overall)
                    nbDeath++;
                    // count only for today (act day)
                    if( actInfec.getDayEndOfInfection() == day ) {
                        Calculation.dailyDeath++;
                    }
                }
            }
            // update data in municipality
            DataLoader.geoData.getMunicip(i).setNbInfections( nbInfected );
            DataLoader.geoData.getMunicip(i).setNbImmune( nbImmune );
            DataLoader.geoData.getMunicip(i).setNbDeath( nbDeath );
            DataLoader.geoData.getMunicip(i).setNbInfectPerInhab( nbInfected );
            // update R0
            if( countR0 == 0 ) {
                DataLoader.geoData.getMunicip(i).setR0( 0 );
            } else {
                // R0 for Municip
                r0 = r0 / (float) countR0;
                DataLoader.geoData.getMunicip(i).setR0(r0 );
            }
            
            // calculate max
            if( nbInfected > maxInfected ) { maxInfected = nbInfected; }
            if( nbImmune > maxImmune ) { maxImmune = nbImmune; }
            if( nbDeath > maxDeath ) { maxDeath = nbDeath; }
            if( r0 > maxR0 ) { maxR0 = r0; }
            float maxRel = DataLoader.geoData.getMunicip(i).getNbInfectPerInhab();
            if( maxRel > maxInfectedRel ) { maxInfectedRel = maxRel; }
        }
        // set daily data
        Infection.setInfectPerDay(day, Calculation.dailyInfections );
        Infection.setImmunesPerDay(day, Calculation.dailyImmunes );
        Infection.setDeathPerDay(day, Calculation.dailyDeath );
        Infection.setTransmPerDay(day, Calculation.nbTransmitions );
        // set max data
        Municipality.setMaxInfections( maxInfected );
        Municipality.setMaxImmune( maxImmune );
        Municipality.setMaxDeath( maxDeath );
        Municipality.setMaxR0( maxR0 );
        Municipality.setMaxInfectPerInhab( maxInfectedRel );
    }
    
    // update chart
    private static void updateChart() {
        int d = Calculation.day;

        int x11 = Calculation.sumInfections;
        int x12 = Infection.getSumDeath();
        int x13 = Infection.getSumImmunes();
        // subplot 1
        XY_Chart.addInfection(d, x11);
        XY_Chart.addDeath(d, x12);
        XY_Chart.addImmune(d, x13);

        // daily cases
        int x21 = Infection.getDailyInfected( d );
        int x22 = Infection.getDailyDeath( d );
        // subplot 2
        XY_Chart.addDailyInf(d, x21);
        XY_Chart.addDailyDeath(d, x22);

        // R0 + Incr. rate
        float x31 = Infection.getMeanR0();
        float x32 = Infection.get7DayR0( d );
        float x33 = Infection.getIncrRate( d );
        // subplot 3
        XY_Chart.addR0(d, x31);
        XY_Chart.add7DayR0(d, x32);
        XY_Chart.addIncrRate(d, x33);
        
    }
    
    // reset calculation
    public static void resetCalc() {
        Calculation.day = 0;
    }
    
    // thread: set stop flag
    public static void stop() {
        Calculation.exit = true;
    }
    
    // setters
    public static void setNbInfectionStart(int nb) {
        Calculation.nbInfectionsStart = nb;
    }
    public static void setNbImmunesStart(int nb) {
        Calculation.nbImmunesStart = nb;
    }
    public static void setNbRandomContacts(int nb) {
        Calculation.randContPerDay = nb;
    }
    public static void setNbPermContacts(int nb) {
        Calculation.permContPerDay = nb;
    }
    public static void setProbaTransmition(float probab) {
        Calculation.probaTransmition = probab;
    }
    public static void setDaysToRecov(int recov) {
        Calculation.dayToRecovery = recov;
    }
    public static void setVarRecov(int varRecov) {
        Calculation.varRecovery = varRecov;
    }
    public static void setMaxCalcDays(int days) {
        Calculation.maxDays = days;
    }
    
}
