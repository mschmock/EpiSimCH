//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.episimch.gui;

import ch.manuel.episimch.Calculation;
import ch.manuel.episimch.DataLoader;
import ch.manuel.episimch.SaveAndLoad;
import ch.manuel.episimch.Startup;
import ch.manuel.episimch.graphics.ChartFrame;
import ch.manuel.episimch.graphics.PolygonPanel;
import ch.manuel.episimch.graphics.XY_Chart;
import ch.manuel.population.Population;
import ch.manuel.utilities.CSVwriter;
import ch.manuel.utilities.MyUtilities;
import java.awt.Point;


public class MainFrame extends javax.swing.JFrame {
    
    // membervariablen
    public static InputCalc dialog;
    public static InputNetwork dialog2;
    public static InputMortality dialog3;
    public static ChartFrame chartframe;
    private static int selectedIndex;
    // load and save
    private static SaveAndLoad saveAndLoad;
    // calculation
    private static Thread t1;                   // calculation thread
    private static Thread t2;                   // loading/saving from file thread
    
    // Constructor
    public MainFrame() {
        initComponents();
        selectedIndex = 0;
        
        // prepare Input Dialog
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog = new InputCalc(new javax.swing.JFrame(), true);
                dialog2 = new InputNetwork(new javax.swing.JFrame(), true);
                dialog3 = new InputMortality(new javax.swing.JFrame(), true);
                chartframe = new ChartFrame();
            }
        });
        
        // save and load object
        saveAndLoad = new SaveAndLoad();
    }
    
    // PUBLIC FUNCTIONS
    public static int getPlotIndex() {
        return MainFrame.selectedIndex;
    }
    public static void setStatusText(String str) {
        MainFrame.jTextField1.setText(str);
    }
    public static void repaintCustomPanel() {
        MainFrame.customPanel.repaintPanel();
    }
    public static void openInputPanel() {
        dialog2.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customPanel = new ch.manuel.episimch.graphics.PolygonPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 300));

        customPanel.setBackground(new java.awt.Color(204, 204, 204));
        customPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout customPanelLayout = new javax.swing.GroupLayout(customPanel);
        customPanel.setLayout(customPanelLayout);
        customPanelLayout.setHorizontalGroup(
            customPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        customPanelLayout.setVerticalGroup(
            customPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setText("status...");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Resultat");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Netzwerk", "Infektionen", "K0" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("Anhalten");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("pro 1'000 EW");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jMenuBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jMenu1.setText("File");
        jMenu1.setPreferredSize(new java.awt.Dimension(50, 21));

        jMenuItem7.setText("Save");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem8.setText("Load");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem2.setText("Close");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.setPreferredSize(new java.awt.Dimension(50, 21));

        jMenuItem3.setText("Soz. Netzwerk");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem1.setText("Parameter Start");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem4.setText("Sterblichkeit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Calculation");
        jMenu3.setPreferredSize(new java.awt.Dimension(80, 21));

        jMenuItem6.setText("Show Chart");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem5.setText("Reset Calculation");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Export CSV");

        jMenuItem9.setText("Save as CSV");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(customPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // show infections
        jComboBox1.setSelectedIndex( 1 );
        jComboBox1.setEnabled( false );
        // start simulation
        //if( (t1 != null) && !t1.isAlive() ) {
            t1 = new Thread( DataLoader.calculation );
            t1.start();
        //}
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void customPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customPanelMouseClicked
        // get name of polygon
        Point p = new Point(evt.getX(), evt.getY());
        MainFrame.customPanel.setNameOnClick(p);
        MainFrame.customPanel.repaintPanel();
    }//GEN-LAST:event_customPanelMouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // element selected in menu
        PolygonPanel.viewChanged();
        MainFrame.selectedIndex = jComboBox1.getSelectedIndex();
        MainFrame.customPanel.repaintPanel();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // end applicaiton
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        dialog2.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        dialog3.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // thread: set thread flag to false
        Calculation.stop();
        // enable combo box
        jComboBox1.setEnabled( true ); 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // thread: not active
        if( t1.isAlive() ) {
            MyUtilities.getErrorMsg("Berechnung", "Zuerst Berechnung stoppen!");
        } else {
            // reset calculation
            Calculation.resetCalc();
            Population.resetInfection();
            XY_Chart.resetChart();
            PolygonPanel.viewChanged();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        chartframe.setVisible( true );
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // set text in MainFrame
        MainFrame.setStatusText("Save file, please wait...");
        // save network
        SaveAndLoad.selectSaving();
        t2 = new Thread( saveAndLoad );
        t2.start();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // set text in MainFrame
        MainFrame.setStatusText("Loading file, please wait...");
        // load network
        SaveAndLoad.selectLoading();
        t2 = new Thread( saveAndLoad );
        t2.start();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // absolute results if NOT selected
        PolygonPanel.viewChanged();
        PolygonPanel.setAbsoluteResultats( !jCheckBox1.isSelected() );
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // save results to csv-file
        CSVwriter.saveResults();
    }//GEN-LAST:event_jMenuItem9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static ch.manuel.episimch.graphics.PolygonPanel customPanel;
    private static javax.swing.JButton jButton1;
    private static javax.swing.JButton jButton2;
    private static javax.swing.JCheckBox jCheckBox1;
    private static javax.swing.JComboBox<String> jComboBox1;
    private static javax.swing.JLabel jLabel1;
    private static javax.swing.JMenu jMenu1;
    private static javax.swing.JMenu jMenu2;
    private static javax.swing.JMenu jMenu3;
    private static javax.swing.JMenu jMenu4;
    private static javax.swing.JMenuBar jMenuBar1;
    private static javax.swing.JMenuItem jMenuItem1;
    private static javax.swing.JMenuItem jMenuItem2;
    private static javax.swing.JMenuItem jMenuItem3;
    private static javax.swing.JMenuItem jMenuItem4;
    private static javax.swing.JMenuItem jMenuItem5;
    private static javax.swing.JMenuItem jMenuItem6;
    private static javax.swing.JMenuItem jMenuItem7;
    private static javax.swing.JMenuItem jMenuItem8;
    private static javax.swing.JMenuItem jMenuItem9;
    private static javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
