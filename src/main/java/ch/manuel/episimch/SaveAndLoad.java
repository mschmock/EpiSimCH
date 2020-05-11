//Autor: Manuel Schmocker
//Datum: 11.05.2020

package ch.manuel.episimch;

import ch.manuel.episimch.gui.MainFrame;
import ch.manuel.population.Person;
import ch.manuel.population.Population;
import ch.manuel.utilities.MyUtilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Save und Load network

public class SaveAndLoad implements Runnable {

    private static boolean selectLoading;
    
    
    @Override
    public void run() {
        // save or load?
        if( selectLoading ) {
            // load datas from file
            loadNetwerk();
            MainFrame.setStatusText("Loading finished");
        } else {
            // load datas from file
            saveNetwork();
            MainFrame.setStatusText("Saving finished");
        }
    }
    
    // setter / getter
    public static void selectLoading() {
        SaveAndLoad.selectLoading = true;
    }
    public static void selectSaving() {
        SaveAndLoad.selectLoading = false;
    }
    
     // SAVE & LOAD
    private static void saveNetwork() {
        String fileExt = ".data";
        String path = MyUtilities.getSaveFileDialog(new java.awt.Frame(), "Datei speichern", "D:\\", "network" + fileExt);
        
        //Speichervorgang wird nur fortgesetzt, wenn der Pfad OK ist
        if ( !(path == null) ) {
            //Endung ergänzen, falls notwendig
            String endDatei = path.substring(path.length() - fileExt.length(), path.length());
            if ( !endDatei.equals( fileExt ) ){
                path = path + fileExt;
            }
            
            // Textausgabe in die gewählte Datei
            try{
                BufferedWriter bw = new BufferedWriter( new FileWriter( path ) );
                
                // prepare date for writing
                int nb = Population.getNbPersons();
                
                // first line
                bw.write( "lines=" + nb ); 
                bw.newLine();
                
                // iterate through population
                int saveProgr = 0;
                for (int i = 0; i < nb; i++) {
                    // actual Person
                    Person actPers = Population.getPerson(i);
                    int[] arrContacts = actPers.getListNetwork().stream().mapToInt(Integer::intValue).toArray();
                    // itaration through contacts
                    String tmpStr = "";
                    for( int j = 0; j < arrContacts.length; j++ ) {
                        if( j == 0 ) {
                            tmpStr += arrContacts[j];
                        } else {
                            tmpStr += ";" + arrContacts[j];
                        }
                    }
                    // write
                    bw.write( tmpStr ); 
                    bw.newLine();
                    
                    // show info text
                    int progr = Math.round( (float) i / nb * 100f );
                    if( progr > saveProgr ) {
                        saveProgr = progr;
                        MainFrame.setStatusText("Speichern: " + progr + "%, bitte warten...");
                    }
                }
                // close writer
                bw.close();
            } catch ( IOException e ) {
                System.err.println( "Konnte Datei nicht erstellen" );
            }             
        }
    }
    
    private static void loadNetwerk() {
        String line = "";
        String fileExt = ".data";
        int count = 0;
        int nbLines;
        int loadProgr = 0;
        
        // nb population
        int nb = Population.getNbPersons();
                
        // Dialog öffnen
        String nameDatei = MyUtilities.getOpenFileDialog(new java.awt.Frame(), "Datei öffnen", "D:\\", "*" + fileExt);
        
        // Fall "null" tritt ein, wenn im Dialog abgebrochen wird
        if ( nameDatei != null ){
            try {
                BufferedReader br = new BufferedReader( new FileReader( nameDatei ) );
                while( ( line = br.readLine() ) != null ) {

                    // first line: get nb of lines in total
                    if( count == 0 ) {
                        nbLines = testFirstLn( line );
                        if( nbLines == 0 ) {
                            MyUtilities.getErrorMsg("Datei", "Erste Linie muss Anzahl Linien enthalten");
                            // stop loop
                            break;
                        }
                        // number of lines must be correct
                        if( nbLines != nb ) {
                            MyUtilities.getErrorMsg("Datei", "Anz. in Linie 1 fehlerhaft. " + nb + " erwartet");
                            // stop loop
                            break;
                        }
                        
                    // next lines
                    } else {
                        int[] arrInt = testLine( line );
                        if( arrInt == null ) {
                            MyUtilities.getErrorMsg("Datei", "Wert in Linie " + count + " fehlerhaft");
                            // stop loop
                            break;
                        } else {
                            // write network
                            int index = count-1;
                            // file cannot be larger than population
                            if( index < nb ) {
                                // add network
                                Population.getPerson( index ).clearNetwork();
                                for( int i = 0; i < arrInt.length; i++ ) {
                                    Population.getPerson( index ).addNetworkLink( arrInt[i] );
                                }
                            }
                        }
                    }
                    // count lines
                    count++;
                    // show info text
                    int progr = Math.round( (float) count / nb * 100f );
                    if( progr > loadProgr ) {
                        loadProgr = progr;
                        MainFrame.setStatusText("Laden: " + progr + "%, bitte warten...");
                    }
                }
                
                // lines ok
                if( (count-1) == nb ) {
                    Population.setNetworkIsCreated( true );
                    // open InputNetwork panel
                    MainFrame.openInputPanel();
                } else {
                    Population.setNetworkIsCreated( false );
                    MyUtilities.getErrorMsg("Datei", "Anz. in Linie fehlerhaft");
                }
                
            } catch (IOException e) {
                System.err.println("Fehler beim Laden der Datei");
                MyUtilities.getErrorMsg("Datei korrupt", "Datei kann nicht geöffnen werden");
            }
        }
    }
    
      
    
    // returns null, if input is not ok
    private static int testFirstLn( String str ) {
        String[] substr;
        substr = str.split("=");
        
        // 2 Elements
        if( substr.length != 2 ) {
            return 0;
        }
        
        // 1. Element: "lines"
        if( !substr[0].equals("lines") ) {
            return 0;
        }
        
        // 2. Element: int with nb of lines
        if( !MyUtilities.isInteger( substr[1] ) ) {
            return 0;
        }
        
        // all OK
        return Integer.parseInt( substr[1] );
    }
    // return null on error
    private static int[] testLine( String str ) {
        String[] substr;
        substr = str.split(";");
        
        // all elements must be int
        for (String elem : substr) {
            if (!MyUtilities.isInteger(elem)) {
                return null;
            }
        }
        
        // all OK
        int[] ret = new int[substr.length];
        for( int i = 0; i < substr.length; i++ ) {
            ret[i] = Integer.parseInt( substr[i] );
        }
        return ret;
    }
    
}
