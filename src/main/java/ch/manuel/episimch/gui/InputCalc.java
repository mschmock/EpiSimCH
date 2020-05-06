//Autor: Manuel Schmocker
//Datum: 12.04.2020

package ch.manuel.episimch.gui;

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
        // disable change of numbers of contacts on day N
        InputCalc.setContactModifActive( false );
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
        spinnRandCont1 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        spinnInfDay0 = new javax.swing.JSpinner();
        inputProbab1 = new javax.swing.JTextField();
        labelDay0 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        spinnPermCont1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spinnDaysToRecov = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        spinnRecovVari = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        spinnDaysToInf = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        spinnMaxCalcDays = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        spinnImmDay0 = new javax.swing.JSpinner();
        labelContacts = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        spinnRandCont2 = new javax.swing.JSpinner();
        spinnPermCont2 = new javax.swing.JSpinner();
        inputProbab2 = new javax.swing.JTextField();
        spinnChangeDay = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();

        setTitle("Input Parameter");
        setResizable(false);

        jLabel1.setText("Anzahl Zufallskontakte pro Tag:");

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Anzahl Infizierte am Tag 0");

        spinnRandCont1.setModel(new javax.swing.SpinnerNumberModel(2, 0, 30, 1));

        jLabel4.setText("Wahrscheinlichkeit für Übertragung:");
        jLabel4.setToolTipText("Wahrscheinlichkeit für Übertragung pro Kontakt");

        spinnInfDay0.setModel( getModel3());

        inputProbab1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputProbab1.setText("0.015");

        labelDay0.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        labelDay0.setText("Parameter Tag 0 (Start Berechnung):");

        jLabel2.setText("Anzahl permanente Kontakte pro Tag:");

        spinnPermCont1.setModel(new javax.swing.SpinnerNumberModel(8, 0, 30, 1));

        jLabel5.setText("Tage bis Genesung");
        jLabel5.setToolTipText("");

        spinnDaysToRecov.setModel(new javax.swing.SpinnerNumberModel(14, 2, 30, 1));

        jLabel6.setText("Abweichung Genesung: +/- Tage");
        jLabel6.setToolTipText("");

        spinnRecovVari.setModel(new javax.swing.SpinnerNumberModel(5, 0, 10, 1));

        jLabel8.setText("Tage bis eine Person infektiös wird");
        jLabel8.setToolTipText("Anzahl Tage von der Infektion bis eine Übertragung möglich wird");

        spinnDaysToInf.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));

        jLabel9.setText("Max. Anzahl Tage für Berechnung");
        jLabel9.setToolTipText("");

        spinnMaxCalcDays.setModel(new javax.swing.SpinnerNumberModel(2000, 1000, 10000, 100));

        jLabel10.setText("Anzahl Immune am Tag 0");
        jLabel10.setToolTipText("");

        spinnImmDay0.setModel( getModel8());

        labelContacts.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        labelContacts.setText("Kontakte pro Tag");

        jLabel12.setText("Anpassung nach N Tagen:");
        jLabel12.setToolTipText("");

        jCheckBox1.setText("Aktivieren");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel13.setText("Tag:");
        jLabel13.setToolTipText("");

        spinnRandCont2.setModel(new javax.swing.SpinnerNumberModel(2, 0, 30, 1));

        spinnPermCont2.setModel(new javax.swing.SpinnerNumberModel(8, 0, 30, 1));

        inputProbab2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputProbab2.setText("0.015");

        spinnChangeDay.setModel(new javax.swing.SpinnerNumberModel(100, 1, 10000, 1));

        jLabel14.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel14.setText("Krankheitsverlauf");

        jLabel15.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel15.setText("Berechnung");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addComponent(labelDay0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addGap(25, 25, 25))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spinnPermCont1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inputProbab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(spinnRandCont1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spinnChangeDay, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(spinnPermCont2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(inputProbab2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spinnRandCont2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelContacts)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spinnMaxCalcDays, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnDaysToInf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinnDaysToRecov, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinnRecovVari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnInfDay0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinnImmDay0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelDay0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spinnInfDay0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(spinnImmDay0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelContacts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spinnRandCont1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnRandCont2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spinnPermCont1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnPermCont2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(inputProbab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputProbab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jCheckBox1)
                    .addComponent(spinnChangeDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spinnDaysToRecov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(spinnRecovVari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spinnDaysToInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(spinnMaxCalcDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        labelDay0.getAccessibleContext().setAccessibleName("jLabel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        // values from input fields
        InputCalc.validInput = initValues();
        
        if( InputCalc.validInput ) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        setContactModifActive( jCheckBox1.isSelected() );
    }//GEN-LAST:event_jCheckBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTextField inputProbab1;
    private static javax.swing.JTextField inputProbab2;
    private static javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private static javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel jLabel10;
    private static javax.swing.JLabel jLabel12;
    private static javax.swing.JLabel jLabel13;
    private static javax.swing.JLabel jLabel14;
    private static javax.swing.JLabel jLabel15;
    private static javax.swing.JLabel jLabel2;
    private static javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private static javax.swing.JLabel jLabel5;
    private static javax.swing.JLabel jLabel6;
    private static javax.swing.JLabel jLabel8;
    private static javax.swing.JLabel jLabel9;
    private static javax.swing.JSeparator jSeparator1;
    private static javax.swing.JSeparator jSeparator2;
    private static javax.swing.JSeparator jSeparator3;
    private static javax.swing.JLabel labelContacts;
    private static javax.swing.JLabel labelDay0;
    private static javax.swing.JSpinner spinnChangeDay;
    private static javax.swing.JSpinner spinnDaysToInf;
    private static javax.swing.JSpinner spinnDaysToRecov;
    private static javax.swing.JSpinner spinnImmDay0;
    private static javax.swing.JSpinner spinnInfDay0;
    private static javax.swing.JSpinner spinnMaxCalcDays;
    private static javax.swing.JSpinner spinnPermCont1;
    private static javax.swing.JSpinner spinnPermCont2;
    private static javax.swing.JSpinner spinnRandCont1;
    private static javax.swing.JSpinner spinnRandCont2;
    private static javax.swing.JSpinner spinnRecovVari;
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
        // Textfeld1 auslesen und numerischer Wert prüfen
        String input1 = inputProbab1.getText();
        if( !MyUtilities.isNumeric(input1) ) {
            cond = false;
            inputProbab1.setText( "0.015" );
            MyUtilities.getErrorMsg("Eingabefehler", "Eingabe überprüfen: " + input1);
        }
        // Textfeld2 auslesen und numerischer Wert prüfen
        String input2 = inputProbab2.getText();
        if( !MyUtilities.isNumeric(input2)) {
            cond = false;
            inputProbab2.setText( "0.015" );
            MyUtilities.getErrorMsg("Eingabefehler", "Eingabe überprüfen: " + input2);
        }
        // immunes + infected can't exceed population
        int nbInhab = Population.getNbPersons();
        int nbInf = (int) spinnInfDay0.getValue();
        int nbImm = (int) spinnImmDay0.getValue();
        if( (nbInf+nbImm) >= nbInhab ) {
            cond = false;
            spinnImmDay0.getModel().setValue(0);
            MyUtilities.getErrorMsg("Eingabefehler", "Anz. Infizierte + Immune darf Bevölkerungszahl nicht überschreiten");
        }
        
        if( cond ) {
            // Tag mit Wechsel von Zustand 1 -> 2
            if( jCheckBox1.isSelected() ) {
                Calculation.setDayChange( (int) spinnChangeDay.getValue() );
            } else {
                // never change (day -1 will never be reached)
                Calculation.setDayChange( -1 );
            }
            // Anzahl Zufallskontakte
            Calculation.setNbRandomContacts( 0, (int) spinnRandCont1.getValue() );
            Calculation.setNbRandomContacts( 1, (int) spinnRandCont2.getValue() );
            // Anzahl perm. Konzatke
            Calculation.setNbPermContacts( 0, (int) spinnPermCont1.getValue() );
            Calculation.setNbPermContacts( 1, (int) spinnPermCont2.getValue() );
            // Wahrscheinlichkeit Übertragung
            Calculation.setProbaTransmition( 0, Float.parseFloat(input1) );
            Calculation.setProbaTransmition( 1, Float.parseFloat(input2) );
            // Anzahl Infizierte am Tag 0
            Calculation.setNbInfectionStart((int) spinnInfDay0.getValue() );
            // Anzahl Infizierte am Tag 0
            Calculation.setNbImmunesStart((int) spinnImmDay0.getValue() );
            // Anzahl Tage bis Genesung
            Calculation.setDaysToRecov((int) spinnDaysToRecov.getValue() );
            // Anzahl Infizierte am Tag 0
            Calculation.setVarRecov((int) spinnRecovVari.getValue() );
            // Anzahl Tages bis Übertragung möglich wird
            Infection.setDaysToTransmition((int) spinnDaysToInf.getValue() );
            // Anzahl Tages bis Übertragung möglich wird
            Calculation.setMaxCalcDays((int) spinnMaxCalcDays.getValue() );
        }
        
        return cond;
    }
    
    public static boolean isInputOK() {
        return InputCalc.validInput;
    }
    
    // enable/disable change on day N
    private static void setContactModifActive( boolean bool ) {
        spinnRandCont2.setEnabled( bool );
        spinnPermCont2.setEnabled( bool );
        inputProbab2.setEnabled( bool );
        spinnChangeDay.setEnabled( bool );
    }
}
