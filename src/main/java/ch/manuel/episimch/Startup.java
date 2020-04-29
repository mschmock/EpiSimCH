//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.episimch;

import ch.manuel.graphics.InfoDialog;
import ch.manuel.graphics.MainFrame;
import ch.manuel.utilities.MyUtilities;


// Main Class
public class Startup extends Thread {
    
    private static MainFrame mainFrame;
    public static InfoDialog dialog;
    private static DataLoader dataLoader;
    private static Thread t2;                   // loading from file thread
    
    public static void main(String[] args) {
        
        // Set Look and Feel
        MyUtilities.setLaF("Windows");
        
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
    
        
}
