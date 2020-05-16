//Autor: Manuel Schmocker
//Datum: 13.04.2020

package ch.manuel.episimch;

import ch.manuel.utilities.MyUtilities;
import ch.manuel.geodata.GeoData;
import ch.manuel.geodata.Municipality;
import ch.manuel.population.Population;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

// Class: Load geodatas from resources
public class DataLoader {
    
    // Membervariablen
    public static GeoData geoData;
    public static Population population;
    public static Calculation calculation;
    private static boolean loadOK;
    private static final Charset utf8 = StandardCharsets.UTF_8;
    
    // files in resources
    private static final String dataXML = "/data/appData.xml";
    private static final String geoDataJSON = "/data/geodata.json";
    private static final String populationJSON = "/data/population.json";
    private static final String ageJSON = "/data/age.json";
    
    // xml
    private static Document appDataXML;
    
    //Konstruktor
    public DataLoader() {
        // Create container for geodata
        DataLoader.geoData = new GeoData();
        DataLoader.population = new Population();
        DataLoader.calculation = new Calculation();
        
        DataLoader.loadOK = false;
    }

    
    public void loadData() {
        boolean loadingOK = false;
        
        // load xml
        loadXML();
        
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
    
    // load xml
    private boolean loadXML() {
        boolean hasErr = false;
        String errMsg = "All OK";
        
        try {
            // get File: appData.xml
            InputStream in = getClass().getResourceAsStream( dataXML );

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
                                
            DataLoader.appDataXML = db.parse( in );
            DataLoader.appDataXML.normalizeDocument();

        } catch (ParserConfigurationException ex) {
            hasErr = true;
            errMsg = "Error loading xml: " + ex.getMessage();
        } catch (SAXException ex) {
            hasErr = true;
            errMsg = "Error loading xml: " + ex.getMessage();
        } catch (IOException ex) {
           hasErr = true;
            errMsg = "Error loading xml: " + ex.getMessage();
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
    public static String getXMLdata(String tagName) {
        return DataLoader.appDataXML.getElementsByTagName(tagName).item(0).getTextContent();
    } 
    
    // set status text in Dialog
    void setStatusText() {
        Startup.dialog.addText( "Application Version: " + DataLoader.getXMLdata( "version" ) + "\n");
        Startup.dialog.addText( "Loaded Municipalities: " + GeoData.getNbMunicip() + "\n");
        Startup.dialog.addText( "Bounds xMin " + geoData.getBoundX() + 
                                        ", yMin " + geoData.getBoundY() + "\n");
        Startup.dialog.addText( "\tWidth " + geoData.getWidth() +
                                ", Height " + geoData.getHeight() + "\n");
        Startup.dialog.addText( "Population: " + Population.getNbPersons() + " individuals created!\n");
    }

    
}
