//Autor: Manuel Schmocker
//Datum: 02.05.2020


package ch.manuel.graphics;

// Legend in main plot (Class PolygonPanel.java)

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Legend {
    // membervariables
    private PolygonPanel polyPanel;
    // size of panel
    private int panelWidth;
    private int panelHeight;
    // max Value for legend
    private int maxValLegend;
    // logarithmic scale
    private boolean isLog;

    
    // Constructor
    public Legend( PolygonPanel polyPanel) {
        this.polyPanel = polyPanel;
    }
    
    // recalculate size
    private void setSize() {
        this.panelWidth = polyPanel.getWidth();
        this.panelHeight = polyPanel.getHeight();
    }
    

    
    protected void drawLegend( Graphics2D g2 ) {
        // positon: right upper corner
        int posX = panelWidth - 20;
        int posY = 20;
        
        // length of segment
        int lenSegm = 20;
        
        g2.setStroke(new BasicStroke(4));
        g2.setColor( colorFactory( this.maxValLegend ) );
        
        g2.drawLine( posX, posY,
                     posX, posY+lenSegm);
        
        // TEXT LEGEND
        // format
        NumberFormat formatter = new DecimalFormat("###,###.##");
        
        String label = String.valueOf( formatter.format( this.maxValLegend ) );
        g2.setFont(new Font("Dialog", Font.PLAIN, 12) );
        int strWidth = g2.getFontMetrics().stringWidth( label );
        posX = posX - strWidth - 5;
        posY = posY - 6;
        
        g2.setColor( Color.black );
        g2.drawString(label, posX, posY);
        
    }
    

    public Color colorFactory( int val ) {
        float fraction = (float) Math.log( val ) / (float) Math.log( this.maxValLegend );
        return Color.getHSBColor( 1.0f, fraction, 0.65f );
    }
    
    // setters
    public void setMaxVal( int max ) {
        this.maxValLegend = max;
    }
    public void setLogScale( boolean bool ) {
        this.isLog = bool;
    }
}
