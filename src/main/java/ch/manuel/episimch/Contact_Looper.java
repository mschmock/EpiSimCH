//Autor: Manuel Schmocker
//Datum: 07.05.2020

package ch.manuel.episimch;

// calculation kernel

import ch.manuel.population.Person;
import ch.manuel.population.Population;
import java.util.Random;

// looper for contacts as thread

public class Contact_Looper implements Runnable {

    private int nbThread;
    private int nbStart;
    private int nbEnd;
            
    private static int nbPop;
    private static int day;
    private static int permContPerDay;
    private static int randContPerDay;
    private static float probaTransmition;
    
    private final Random randTrans;
    private final Random randCont;
    
    // Constructor
    public Contact_Looper( int nb, int max ) {
        nbPop = Population.getNbPersons();
        
        randTrans = new Random();
        randCont = new Random();
        
        nbThread = nb;
        nbStart = (int) Math.floor( (double) (nb-1)/max* nbPop );
        nbEnd = (int) Math.floor( (double) nb/max* nbPop );
        //System.out.println("s: " + nbStart + ", e: " + nbEnd);
    }
    
    
    @Override
    public void run() {
        calcContacts();
    }
    
    // calculate contacts
    private void calcContacts() {
        
        // iterate through population
        for (int i = nbStart; i < nbEnd; i++) {
            // actual Person to test
            Person actPers = Population.getPerson(i);

            // check persons who can transmit a infection (isInfectious)
            if( actPers.getInfection().isInfectious( day )) {
                
                // 1. PERMANENT CONTACTS
                // array contacts per person
                int[] arrContacts = actPers.getListNetwork().stream().mapToInt(Integer::intValue).toArray();
                // itaration through contacts
                for( int j = 0; j < permContPerDay; j++ ) { 
                    int tmpInd = randCont.nextInt( arrContacts.length );
                    // temp. Person
                    Person tmpPers = Population.getPerson( arrContacts[tmpInd] );
                    
                    // transmition of infection
                    if( randTrans.nextFloat() < probaTransmition ) {
                        // infection is transmitted --> return true
                        if( tmpPers.getInfection().setIsInfected( day ) ) {
                            // add new infection to counter R0
                            actPers.getInfection().transmitionSuccessful();
                        }
                    }
                }
                
                // 2. RANDOM CONTACTS
                for( int j = 0; j < randContPerDay; j++ ) {
                    int tmpInd = randCont.nextInt( nbPop );
                    // no reference to itself: nb != tmpInd
                    // goto next referenc (exept last element)
                    if( i == tmpInd ) {
                        if( i == (nbPop-1) ) {
                            tmpInd = 0;
                        } else {
                            tmpInd = i+1;
                        }
                    }
                    // temp. Person
                    Person tmpPers = Population.getPerson( tmpInd );
                    
                    // transmition of infection
                    if( randTrans.nextFloat() < probaTransmition ) {
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
    
    // setter 
    protected static void setDay( int day ) {
        Contact_Looper.day = day;
    }
    protected static void setPermContPerDay( int nb ) {
        Contact_Looper.permContPerDay = nb;
    }
    protected static void setRandContPerDay( int nb ) {
        Contact_Looper.randContPerDay = nb;
    }
    protected static void setProbaTransmition( float proba ) {
        Contact_Looper.probaTransmition = proba;
    }
}
