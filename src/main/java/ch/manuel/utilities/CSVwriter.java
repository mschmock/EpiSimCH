//Autor: Manuel Schmocker
//Datum: 10.05.2020

package ch.manuel.utilities;

// CSV writer

import ch.manuel.episimch.Calculation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// creats txt-file (.csv-file) with results
public class CSVwriter {
    
    // list with results
    private static final List<String> listResults = new ArrayList<>();;  
    
    // add element to results
    public static void addResult( String csvResult ) {
        listResults.add( csvResult );
    }
    
    // Write results to file
    public static void saveResults() {
        String fileExt = ".csv";
        String path = MyUtilities.getSaveFileDialog(new java.awt.Frame(), "Save CSV-file", "D:\\", "results" + fileExt);
        
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
                
                // Headers
                bw.write( "INPUT DATA:"); 
                bw.newLine();
                int nbInf = Calculation.getNbImmunesStart();
                bw.write( "\"Infencted on day 0:\";" + nbInf); 
                bw.newLine();
                int nbImm = Calculation.getNbImmunesStart();
                bw.write( "\"Immunes on day 0:\";" + nbImm); 
                bw.newLine();
                int daysRecov = Calculation.getDaysToRecov();
                bw.write( "\"Days to recover:\";" + daysRecov); 
                bw.newLine();
                int daysVarRecov = Calculation.getVarRecov();
                bw.write( "\"Variability for recover (days):\";" + daysVarRecov); 
                bw.newLine();
                int dayOfChange = Calculation.getDayChange();
                bw.write( "\"Day of change:\";0;" + dayOfChange); 
                bw.newLine();
                String[] permCont = Arrays.stream( Calculation.getNbPermContacts() ).mapToObj(String::valueOf).toArray(String[]::new);
                bw.write( "\"Number of permanent contacts:\";" + String.join(";", permCont)); 
                bw.newLine();
                String[] randCont = Arrays.stream( Calculation.getNbRandomContacts() ).mapToObj(String::valueOf).toArray(String[]::new);
                bw.write( "\"Number of random contacts:\";" + String.join(";", randCont)); 
                bw.newLine();
                
                // Body
                bw.write( "Day;Sum infections;Sum death;Sum immunes;Daily infections;Daily death;R0;R0(7-days);Incr. rate"); 
                bw.newLine();
                for( String result : listResults ) {
                    bw.write( result );
                    bw.newLine();
                }
                
                // close writer
                bw.close();
            } catch ( IOException e ) {
                System.err.println( "Konnte Datei nicht erstellen" );
            }  
        }
    }


    
}
