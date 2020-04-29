//Autor: Manuel Schmocker
//Datum: 12.04.2020

package ch.manuel.graphics;

import ch.manuel.episimch.Calculation;
import ch.manuel.population.Infection;
import ch.manuel.population.Population;
import ch.manuel.utilities.MyUtilities;
import javax.swing.SpinnerNumberModel;

// Input Dialog: Startparameter

public class InputCalc extends javax.swing.JDialog {
    
    // membervariable
    private static boolean validInput;
    
    // Constructor
    public InputCalc(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        // set standard-values for calculation
        InputCalc.validInput = this.initValues();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();

        setTitle("Input Parameter");
        setResizable(false);

        jLabel1.setText("Anzahl Zufallskontakte pro Tag:");

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Anzahl Infizierte am Tag 0:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(2, 0, 30, 1));

        jLabel4.setText("Wahrscheinlichkeit für Übertragung:");
        jLabel4.setToolTipText("Wahrscheinlichkeit für Übertragung pro Kontakt");

        jSpinner3.setModel( getModel3());

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("0.015");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel7.setText("Parameter Start");

        jLabel2.setText("Anzahl permanente Kontakte pro Tag:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(8, 0, 30, 1));

        jLabel5.setText("Tage bis Genesung");
        jLabel5.setToolTipText("");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(14, 2, 30, 1));

        jLabel6.setText("Abweichung Genesung: +/- Tage");
        jLabel6.setToolTipText("");

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(5, 0, 10, 1));

        jLabel8.setText("Tage bis eine Person infektiös wird");
        jLabel8.setToolTipText("Anzahl Tage von der Infektion bis eine Übertragung möglich wird");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));

        jLabel9.setText("Max. Anzahl Tage für Berechnung");
        jLabel9.setToolTipText("");

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(1000, 1000, 10000, 100));

        jLabel10.setText("Anzahl Immune am Tag 0:");
        jLabel10.setToolTipText("");

        jSpinner8.setModel( getModel8());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(62, 62, 62)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jLabel7.getAccessibleContext().setAccessibleName("jLabel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        // values from input fields
        InputCalc.validInput = initValues();
        
        if( InputCalc.validInput ) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jButton2;
    private static javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel jLabel10;
    private static javax.swing.JLabel jLabel2;
    private static javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private static javax.swing.JLabel jLabel5;
    private static javax.swing.JLabel jLabel6;
    private static javax.swing.JLabel jLabel7;
    private static javax.swing.JLabel jLabel8;
    private static javax.swing.JLabel jLabel9;
    private static javax.swing.JSpinner jSpinner1;
    private static javax.swing.JSpinner jSpinner2;
    private static javax.swing.JSpinner jSpinner3;
    private static javax.swing.JSpinner jSpinner4;
    private static javax.swing.JSpinner jSpinner5;
    private static javax.swing.JSpinner jSpinner6;
    private static javax.swing.JSpinner jSpinner7;
    private static javax.swing.JSpinner jSpinner8;
    private static javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    // set custom model for spinners:
    private SpinnerNumberModel getModel3() {
        int nbInhab = Population.getNbPersons();
        return new SpinnerNumberModel(50, 1, nbInhab, 1);
    }
    // set custom model for spinners:
    private SpinnerNumberModel getModel8() {
        int nbInhab = Population.getNbPersons();
        return new SpinnerNumberModel(0, 0, nbInhab, 1);
    }
    // get values from input-fields
    private boolean initValues() {
        boolean cond = true;
        // Textfeld auslesen und numerischer Wert prüfen
        String str1 = jTextField1.getText();
        if (MyUtilities.isNumeric(str1)) {
            Calculation.setProbaTransmition( Float.parseFloat(str1) );
        } else {
            cond = false;
            jTextField1.setText( "0.015" );
            MyUtilities.getErrorMsg("Eingabefehler", "Eingabe überprüfen: " + str1);
        }
        // immunes + infected can't exceed population
        int nbInhab = Population.getNbPersons();
        int nbInf = (int) jSpinner3.getValue();
        int nbImm = (int) jSpinner8.getValue();
        if( (nbInf+nbImm) >= nbInhab ) {
            cond = false;
            jSpinner8.getModel().setValue(0);
            MyUtilities.getErrorMsg("Eingabefehler", "Anz. Infizierte + Immune darf Bevölkerungszahl nicht überschreiten");
        }
        
        if( cond ) {
            // Anzahl Zufallskontakte
            Calculation.setNbRandomContacts( (int) jSpinner1.getValue() );
            // Anzahl perm. Konzatke
            Calculation.setNbPermContacts( (int) jSpinner2.getValue() );
            // Anzahl Infizierte am Tag 0
            Calculation.setNbInfectionStart( (int) jSpinner3.getValue() );
            // Anzahl Infizierte am Tag 0
            Calculation.setNbImmunesStart( (int) jSpinner8.getValue() );
            // Anzahl Tage bis Genesung
            Calculation.setDaysToRecov( (int) jSpinner4.getValue() );
            // Anzahl Infizierte am Tag 0
            Calculation.setVarRecov( (int) jSpinner5.getValue() );
            // Anzahl Tages bis Übertragung möglich wird
            Infection.setDaysToTransmition( (int) jSpinner6.getValue() );
            // Anzahl Tages bis Übertragung möglich wird
            Calculation.setMaxCalcDays( (int) jSpinner7.getValue() );
        }
        
        return cond;
    }
    
    public static boolean isInputOK() {
        return InputCalc.validInput;
    }
    
}
