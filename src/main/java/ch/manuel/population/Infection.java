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
    
    // K0
    private int nbTransmK0;
    // Results
    private static int nbDailyInfections;
    // Results: daily datas
    private static int[] resInfections;
    private static int[] resImmunes;
    private static int[] resDeath;
    private static int[] resK0Transm;
    private static int[] resK0Pers;
    
            
    // Constructor
    public Infection( Person pers ) {
        this.pers = pers;
        this.dayOfInfection = -100;
        this.isInfected = false;
        this.isImmune = false;
        this.willRecover = true;
        this.nbTransmK0 = 0;
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
        resInfections = new int[size];
        resImmunes = new int[size];
        resDeath = new int[size];
        resK0Transm = new int[size];
        resK0Pers = new int[size];
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
    public static void setDaysToTransmition( int days ) {
        Infection.daysToTransmition = days;
    }
    public void transmitionSuccessful() {
        this.nbTransmK0++;
    }
    public static void setResInfection( int day, int nb ) {
        Infection.resInfections[day] = nb;
        //System.out.println("Inf: " + nb);
    }
    public static void setResImmues( int day, int nb ) {
        Infection.resImmunes[day] = nb;
    }
    public static void setResDeath( int day, int nb ) {
        Infection.resDeath[day] = nb;
    }
    public static void setResR0Transm( int day, int k0 ) {
        Infection.resK0Transm[day] = k0;
    }
    public static void setResR0Count( int day, int k0 ) {
        Infection.resK0Pers[day] = k0;
    }
    public static void setDailyInfections( int nb ) {
        Infection.nbDailyInfections = nb;
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
        return this.nbTransmK0;
    }
    public int getDayOfInfection() {
        return this.dayOfInfection;
    }
    public int getDayEndOfInfection() {
        return this.dayEndOfInfection;
    }
    public static int getDailyInfections() {
        return Infection.nbDailyInfections;
    }
    public static int getDailyDeath( int d ) {
        if( d < 0 ) { return 0; }
        return  resDeath[d];
    }
    public static int getSumInfections( int d ) {
        int sum1 = 0;
        if( d < 0 ) { return 0; }
        int maxI = d < resInfections.length ? d : resInfections.length;
        for( int i = 0; i < maxI; i++ ) {
            sum1 += resInfections[i];
        }
        return sum1;
    }
    public static int getSumImmunes() {
        int sum = 0;
        for( int i = 0; i < resImmunes.length; i++ ) {
            sum += resImmunes[i];
        }
        return sum;
    }
    public static int getSumDeath() {
        int sum = 0;
        for( int i = 0; i < resDeath.length; i++ ) {
            sum += resDeath[i];
        }
        return sum;
    }
    public static float getMeanR0() {
        int sum1 = 0;
        int sum2 = 0;
        for( int i = 0; i < resK0Transm.length; i++ ) {
            sum1 += resK0Transm[i];
            sum2 += resK0Pers[i];
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
                sum1 += resK0Transm[i];
                sum2 += resK0Pers[i];
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
        int val1 = resInfections[d-1];
        int val2 = resInfections[d];
        
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
        this.nbTransmK0 = 0;
    }
}
