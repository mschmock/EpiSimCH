//Autor: Manuel Schmocker
//Datum: 13.04.2020

package ch.manuel.episimch;

import ch.manuel.utilities.MyUtilities;
import ch.manuel.geodata.GeoData;
import ch.manuel.geodata.Municipality;
import ch.manuel.graphics.MainFrame;
import ch.manuel.population.Person;
import ch.manuel.population.Population;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Class: Load geodatas from resources
public class DataLoader implements Runnable {
    
    // Membervariablen
    public static GeoData geoData;
    public static Population population;
    public static Calculation calculation;
    private static boolean loadOK;
    private static final Charset utf8 = StandardCharsets.UTF_8;
    
    // files in resources
    private static final String geoDataJSON = "/data/geodata.json";
    //private static final String geoDataJSON = "/data/geodata_10.json";
    private static final String populationJSON = "/data/population.json";
    private static final String ageJSON = "/data/age.json";
    
    //Konstruktor
    public DataLoader() {
        // Create container for geodata
        DataLoader.geoData = new GeoData();
        DataLoader.population = new Population();
        DataLoader.calculation = new Calculation();
        
        DataLoader.loadOK = false;
    }
    
    @Override
    public void run() {
        // load datas from file
        loadNetwerk();
        MainFrame.setStatusText("Loading finished");
    }
    
    public void loadData() {
        boolean loadingOK = false;
        
        // load JSON from resouces
        // file 1
        Startup.dialog.addText( "loading data from file: '" + geoDataJSON + "'");
        loadingOK = this.loadJSON( );               // Grenzen -> geodata.json
        if( loadingOK ) {
            Startup.dialog.addText( "\tOK\n");
        } else {
            Startup.dialog.addText( "\tfailed\n");
        }
        
        // file 2
        if( loadingOK ) {
            Startup.dialog.addText( "loading data from file: '" + populationJSON + "'");
            loadingOK = this.loadJSON2( );          // Einwohner -> population.json
            if( loadingOK ) {
                Startup.dialog.addText( "\tOK\n");
            } else {
                Startup.dialog.addText( "\tfailed\n");
            }
        }
        // file 3
        if( loadingOK ) {
            Startup.dialog.addText( "loading data from file: '" + ageJSON + "'");
            loadingOK = this.loadJSON3( );          // Altersverteilung -> age.json
            if( loadingOK ) {
                Startup.dialog.addText( "\tOK\n");
            } else {
                Startup.dialog.addText( "\tfailed\n");
            }
        }
        
        // preparing data
        if( loadingOK ) {
            DataLoader.loadOK = true;
            
            // calculate bounds + distances
            DataLoader.geoData.calculateBounds();
            DataLoader.geoData.calculateDistances();
            // create population
            DataLoader.population.createPop();
        } else {
            DataLoader.loadOK = false;
        }
    }

    // load JSON: geodata (Grenzen)
    private boolean loadJSON() {

        // get File: geodata.json
        InputStream in = getClass().getResourceAsStream( geoDataJSON ); 
        
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        
        // Error message
        String idName = "";
        String errMsg = "All OK";
        boolean hasErr = false;

        try (BufferedReader reader = new BufferedReader( new InputStreamReader( in, utf8 ) ) ) {
            //Read JSON file
            Object obj = jsonParser.parse( reader );
            JSONArray objList = (JSONArray) obj;
             
            //Iterate over objects array
            Iterator<JSONObject> iterator = objList.iterator();
            while (iterator.hasNext()) {
                JSONObject tmpObj = iterator.next();
                JSONObject attriObj = (JSONObject) tmpObj.get("attributes");
                
                idName = tmpObj.get("id").toString();

                // INSERT DATA
                String input;
                geoData.addMunicip( attriObj.get("GMDNAME").toString() );       // Gemeindename
                // ID -> in map 
                input = attriObj.get("GMDNR").toString();
                if( MyUtilities.isInteger( input ) ) {
                    // setID --> map with [id, object]
                    geoData.setID( Integer.parseInt( input ), GeoData.getLastElement() );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'GMDNR'";
                    hasErr = true;
                    break;
                }
                // Zentrum: Koordinate LV 95 E
                input = attriObj.get("E_CNTR").toString();
                if( MyUtilities.isInteger( input ) ) {
                    GeoData.getLastElement().setCenterE( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'E_CNTR'";
                    hasErr = true;
                    break;
                }
                // Zentrum: Koordinate LV 95 N
                input = attriObj.get("N_CNTR").toString();
                if( MyUtilities.isInteger( input ) ) {
                    GeoData.getLastElement().setCenterN( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'N_CNTR'";
                    hasErr = true;
                    break;
                }
                // Umgrenzung: min Koordinate LV 95 N
                input = attriObj.get("N_MIN").toString();
                if( MyUtilities.isInteger( input ) ) {
                    GeoData.getLastElement().setMinN( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'N_MIN'";
                    hasErr = true;
                    break;
                }
                // Umgrenzung: max Koordinate LV 95 N
                input = attriObj.get("N_MAX").toString();
                if( MyUtilities.isInteger( input ) ) {
                    GeoData.getLastElement().setMaxN( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'N_MAX'";
                    hasErr = true;
                    break;
                }
                // Umgrenzung: min Koordinate LV 95 E
                input = attriObj.get("E_MIN").toString();
                if( MyUtilities.isInteger( input ) ) {
                    GeoData.getLastElement().setMinE( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'E_MIN'";
                    hasErr = true;
                    break;
                }
                // Umgrenzung: max Koordinate LV 95 E
                input = attriObj.get("E_MAX").toString();
                if( MyUtilities.isInteger(input) ) {
                    GeoData.getLastElement().setMaxE( Integer.parseInt( input ) );
                } else {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'E_MAX'";
                    hasErr = true;
                    break;
                }
                // Polygon Gemeindegrenze
                if( !GeoData.getLastElement().setPolygon( attriObj.get("POLYGON").toString() ) ) {
                    errMsg = geoDataJSON + "\nFehler im Objekt " + idName + ", element: 'POLYGON'";
                    hasErr = true;
                    break;
                }                      
                
            }    
 
        } catch (FileNotFoundException e) {
            hasErr = true;
            errMsg = "Datei nicht gefunden!";
        } catch (IOException e) {
            hasErr = true;
            errMsg = e.getMessage();
        } catch (org.json.simple.parser.ParseException e) {
            hasErr = true;
            errMsg = "Fehlerhafte Formatierung JSON in Pos: " + e.getPosition();
        } 
        
        // print error-message
        if( hasErr ) {
            MyUtilities.getErrorMsg("Error", errMsg);
            return false;
        } else {
            // no errors loading data
            return true;
        }
    }
    
    // load JSON: population (Einwohner)
    private boolean loadJSON2( ) {
        // get File: population.json
        InputStream in = getClass().getResourceAsStream( populationJSON ); 
        
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        
        // Error message
        String idName = "";
        String errMsg = "All OK";
        boolean hasErr = false;
        
        try (BufferedReader reader = new BufferedReader( new InputStreamReader( in, utf8 ) ) ) {
            //Read JSON file
            Object obj = jsonParser.parse( reader );
            JSONArray objList = (JSONArray) obj;
            
            //Iterate over objects array
            Iterator<JSONObject> iterator = objList.iterator();
            Municipality tmpMunicip;
            while (iterator.hasNext()) {
                JSONObject tmpObj = iterator.next();
                
                // INSERT DATA
                String input;
                idName  = tmpObj.get("name").toString();
                // id -> get Object in map
                input = tmpObj.get("id").toString();
                if( MyUtilities.isInteger( input ) ) {
                    // id --> get object from map with [id, object]
                    tmpMunicip = DataLoader.geoData.getMunicipByID( Integer.parseInt( input ) );
                    if( tmpMunicip == null ) {
                        errMsg = "ID nicht gefunden für 'name': " + idName;
                        hasErr = true;
                        break;
                    }
                } else {
                    errMsg = "Fehler im Objekt " + idName + ", element: 'id'";
                    hasErr = true;
                    break;
                }
                
                // Anzahl Einwohner
                input = tmpObj.get("population").toString();
                if( MyUtilities.isInteger( input ) ) {
                    tmpMunicip.setPopulation( Integer.parseInt( input ) );
                } else {
                    errMsg = "Fehler im Objekt " + idName + ", element: 'population'";
                    hasErr = true;
                    break;
                }
                // Anzahl Haushalte
                input = tmpObj.get("household").toString();
                if( MyUtilities.isInteger( input ) ) {
                    tmpMunicip.setHouseholds( Integer.parseInt( input ) );
                } else {
                    errMsg = "Fehler im Objekt " + idName + ", element: 'household'";
                    hasErr = true;
                    break;
                }
                // update Gemeindename
                tmpMunicip.setName(idName);
            }
            
        } catch (FileNotFoundException e) {
            hasErr = true;
            errMsg = "Datei nicht gefunden!";
        } catch (IOException e) {
            hasErr = true;
            errMsg = e.getMessage();
        } catch (org.json.simple.parser.ParseException e) {
            hasErr = true;
            errMsg = "Fehlerhafte Formatierung JSON in Pos: " + e.getPosition();
        } 
        
        // print error-message
        if( hasErr ) {
            MyUtilities.getErrorMsg("Error", errMsg);
            return false;
        } else {
            // no errors loading data
            return true;
        }
    }
    
    // load JSON: population (Einwohner)
    private boolean loadJSON3( ) {
        // get File: age.json
        InputStream in = getClass().getResourceAsStream( ageJSON ); 
        
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        
        // Error message
        String idName = "";
        String errMsg = "All OK";
        boolean hasErr = false;
        
        try (BufferedReader reader = new BufferedReader( new InputStreamReader( in, utf8 ) ) ) {
            //Read JSON file
            Object obj = jsonParser.parse( reader );
            JSONArray objList = (JSONArray) obj;
            
            //Iterate over objects array
            Iterator<JSONObject> iterator = objList.iterator();

            while (iterator.hasNext()) {
                JSONObject tmpObj = iterator.next();
                
                // INSERT DATA
                String input, input2;
                idName  = tmpObj.get("name").toString();
                // bound: lower bound from interval -> get Object in map
                input = tmpObj.get("bound").toString();
                if( MyUtilities.isInteger( input ) ) {
                    // save in -> fraction
                } else {
                    errMsg = "Fehler im Objekt " + idName + ", element: 'bound'";
                    hasErr = true;
                    break;
                }
                
                // fraction: Anteil Bevölkerung mit spez. Alter
                input2 = tmpObj.get("fraction").toString();
                if( MyUtilities.isNumeric( input2 ) ) {
                    DataLoader.population.setAgeDistr(Integer.parseInt( input ), Double.parseDouble( input2 ) );
                } else {
                    errMsg = "Fehler im Objekt " + idName + ", element: 'fraction'";
                    hasErr = true;
                    break;
                }

            }
            
        } catch (FileNotFoundException e) {
            hasErr = true;
            errMsg = "Datei nicht gefunden!";
        } catch (IOException e) {
            hasErr = true;
            errMsg = e.getMessage();
        } catch (org.json.simple.parser.ParseException e) {
            hasErr = true;
            errMsg = "Fehlerhafte Formatierung JSON in Pos: " + e.getPosition();
        } 
        
        // print error-message
        if( hasErr ) {
            MyUtilities.getErrorMsg("Error", errMsg);
            return false;
        } else {
            // no errors loading data
            return true;
        }
    }
    
    // getter
    // return boolean if loading is ok
    public static boolean isLoadingOK() {
        return DataLoader.loadOK;
    }
    
    
    // set status text in Dialog
    void setStatusText() {
        Startup.dialog.addText( "Loaded Municipalities: " + GeoData.getNbMunicip() + "\n");
        Startup.dialog.addText( "Bounds xMin " + geoData.getBoundX() + 
                                        ", yMin " + geoData.getBoundY() + "\n");
        Startup.dialog.addText( "\tWidth " + geoData.getWidth() +
                                ", Height " + geoData.getHeight() + "\n");
        Startup.dialog.addText( "Population: " + Population.getNbPersons() + " individuals created!\n");
    }

    // SAVE & LOAD
    public static void saveNetwork() {
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
