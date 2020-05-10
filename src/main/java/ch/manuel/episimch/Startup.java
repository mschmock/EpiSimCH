//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.episimch;

import ch.manuel.episimch.gui.InfoDialog;
import ch.manuel.episimch.gui.MainFrame;
import ch.manuel.utilities.MyUtilities;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


// Main Class
public class Startup extends Thread {
    
    private static MainFrame mainFrame;
    public static InfoDialog dialog;
    private static DataLoader dataLoader;
    private static Thread t2;                   // loading from file thread
    
    public static void main(String[] args) {
        
        // Set Look and Feel
        MyUtilities.setLaF("Windows");
        
        // check available memory
        Startup.checkMemory();
        
        // open InfoPanel
        Startup.dialog = new InfoDialog(new javax.swing.JFrame(), true);
        Thread t1 = new Thread( Startup.dialog );
        t1.start();
                
        // Load data from resources
        dataLoader = new DataLoader();
        dataLoader.loadData();
        dataLoader.setStatusText();
        // Infopanel -> abschluss anzeigen
        Startup.dialog.setLoadingOK();
            
        // wait for InfoDialog to be closed
        try {
            t1.join();
        } catch (InterruptedException ex) {
            MyUtilities.getErrorMsg("Systemfehler", "Fehler im Thread 'dialog'. Programm wird beendet!");
            System.exit(0);
        }
        // open main-window if loading is ok
        if( DataLoader.isLoadingOK() && !dialog.isCanceled() ) {
            // close InfoPanel
            dialog.dispose();
            
            //Fenster erstellen und Anzeigen (Hauptfenster)
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                }
            });
        }
    }
    
    // start file loading thread
    public static void loadFile() {
        t2 = new Thread( Startup.dataLoader );
        t2.start();
    }
    
    // Check available memory of heap
    private static void checkMemory() {
        long maxMem = Runtime.getRuntime().maxMemory();
        double maxGB =  maxMem / 1000000000.0;
        boolean hasError = false;
        
        // needed memory: > 4gb
        if( maxGB < 5.0 ) {
            int ans = MyUtilities.getYesNoDialog("It seems that your memory size is limited to 4GB.\n "
                                               + "8GB are recommanded to run the simulation.\n"
                                               + "Try to restart application with 8GB heap size?", "Heap Size");
            if( ans == javax.swing.JOptionPane.YES_OPTION ) {
                
                System.out.println("Try to restart application");
                try {
                    // try restart with Xmx8g
                    Startup.restartApplication();
                } catch (URISyntaxException ex) {
                    System.out.println( "Error:\t" + ex.toString() );
                    hasError = true;
                } catch (IOException ex) {
                    System.out.println( "Error:\t" + ex.toString() );
                    hasError = true;
                }
            }
            // show error msg
            if( hasError ) {
                MyUtilities.getErrorMsg( "Error", "Error reloading application with heap size of 8GB");
            }
        }
    }
    
    // Try restart java application
    // with paramter "-Xmx8g" (8GB heap size)
    private static void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(Startup.class.getProtectionDomain().getCodeSource().getLocation().toURI() );

        // is it a jar file?
        if(!currentJar.getName().endsWith(".jar"))
            return;

        // Build command: java -jar application.jar
        final ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-Xmx8g");
        command.add("-jar");
        command.add(currentJar.getPath());
        
        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
        
}
