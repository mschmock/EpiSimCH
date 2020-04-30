//Autor: Manuel Schmocker
//Datum: 20.04.2020

package ch.manuel.population;

// class to handle infection


public class Infection {
    
    // membervaribles
    private Person pers;
    private int dayOfInfection;
    private int dayEndOfInfection;
    private int daysToRecovery;
    private boolean isInfected;
    private boolean isImmune;
    private boolean willRecover;
    private static int daysToTransmition;       // Anzahl Tage bis eine Übertragung möglich wird
    private int nbTransmR0;                     // nb transmition for this person 

    // Results: daily datas
    private static int[] infectionPerDay;
    private static int[] ImmunesPerDay;
    private static int[] DeathPerDay;
    private static int[] TransmPerDay;
   
            
    // Constructor
    public Infection( Person pers ) {
        this.pers = pers;
        this.dayOfInfection = -100;
        this.isInfected = false;
        this.isImmune = false;
        this.willRecover = true;
        this.nbTransmR0 = 0;
    }
    
    
    // FUNCTIONS
    public void updateInfection(int actDay) {
        // actDay: aktuelle Tag im Berechnungszyklus
        if( this.isInfected ) {
            // Ende Infektion erreicht?
            int daysSick = actDay - dayOfInfection;
            if( daysSick >= daysToRecovery ) {
                // recover or die
                if( this.willRecover ) {
                    this.isInfected = false;
                    this.isImmune = true; 
                } else {
                    // person dies
                    pers.dies();
                    this.isInfected = false;
                    this.isImmune = false; 
                }
                this.dayEndOfInfection = actDay;
            }
        }
    }
    public static void initArray(int size) {
        infectionPerDay = new int[size];
        ImmunesPerDay = new int[size];
        DeathPerDay = new int[size];
        TransmPerDay = new int[size];
    }
    
    // setter
    public boolean setIsInfected(int day) {
        // is not immune and alive
        if( !this.isImmune && pers.isAlive()) {
            // is not infected
            if( !this.isInfected ) {
                // set infection
                this.isInfected = true;
                this.dayOfInfection = day;
                
                // new infection
                return true;
            }
        }
        return false;
    }
    public void setDaysToRecovery(int days) {
        this.daysToRecovery = days;
    }
    public void setNoRecovery() {
        this.willRecover = false;
    }
    public void setIsImmune() {
        this.isImmune = true;
    }
    public static void setDaysToTransmition( int days ) {
        Infection.daysToTransmition = days;
    }
    public void transmitionSuccessful() {
        this.nbTransmR0++;
    }
    // SET DAILY DATA
    public static void setInfectPerDay( int day, int nb ) {
        Infection.infectionPerDay[day] = nb;
    }
    public static void setImmunesPerDay( int day, int nb ) {
        Infection.ImmunesPerDay[day] = nb;
    }
    public static void setDeathPerDay( int day, int nb ) {
        Infection.DeathPerDay[day] = nb;
    }
    public static void setTransmPerDay( int day, int k0 ) {
        Infection.TransmPerDay[day] = k0;
    }
    
    // getter
    public boolean isInfectious( int actDay ) {
        boolean isInfect = false;
        if( this.isInfected ) {
            int daysSick = actDay-this.dayOfInfection;
            // transmition is possible?
            if( daysSick > Infection.daysToTransmition ) {
                isInfect = true;
            }
        }
        
        return isInfect;
    } 
    public boolean isInfected() {
        return this.isInfected;
    }
    public boolean isImmune() {
        return this.isImmune;
    }
    public int getNbTransmR0() {
        return this.nbTransmR0;
    }
    public int getDayOfInfection() {
        return this.dayOfInfection;
    }
    public int getDayEndOfInfection() {
        return this.dayEndOfInfection;
    }
    // GET DAILY DATA
    public static int getDailyDeath( int d ) {
        if( d < 0 ) { return 0; }
        return  DeathPerDay[d];
    }
    public static int getDailyInfected( int d ) {
        if( d < 0 ) { return 0; }
        return infectionPerDay[d];
    }
    public static int getSumImmunes() {
        int sum = 0;
        for( int i = 0; i < ImmunesPerDay.length; i++ ) {
            sum += ImmunesPerDay[i];
        }
        return sum;
    }
    public static int getSumDeath() {
        int sum = 0;
        for( int i = 0; i < DeathPerDay.length; i++ ) {
            sum += DeathPerDay[i];
        }
        return sum;
    }
    public static float getMeanR0() {
        // don't count first day -> Immunes from startup (initial parameter)
        // thouse immunes do not have any transmitions
        int sum1 = 0;
        int sum2 = 0;
        for( int i = 1; i < TransmPerDay.length; i++ ) {
            sum1 += TransmPerDay[i];
            sum2 += ImmunesPerDay[i];
        }
        if( sum2 == 0 ) {
            return 0f;
        } else {
            return (float) sum1 / (float) sum2;
        }
        
    }
    public static float get7DayR0( int day ) {
        int sum1 = 0;
        int sum2 = 0;
        if( day < 7 ) {
            return 0f;
        } else {
            for( int i = (day-7); i < day; i++ ) {
                sum1 += TransmPerDay[i];
                sum2 += ImmunesPerDay[i];
            }
            if( sum2 == 0 ) {
                return 0f;
            } else {
                return (float) sum1 / (float) sum2;
            }
        }
    }
    public static float getIncrRate( int d ) {
        if( d < 2 ) { return 0; }
        int val1 = infectionPerDay[d-1];
        int val2 = infectionPerDay[d];
        
        if( val1 == 0 ) { 
            return 1f; 
        } else {
            return (float) val2 / (float) val1;
        }
    }

    // reset infection
    public void resetInfection() {
        this.dayOfInfection = -100;
        this.isInfected = false;
        this.isImmune = false;
        this.willRecover = true;
        this.nbTransmR0 = 0;
    }
}
