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
    // legend: geometry
    private final int LEN_SEGM = 20;
    private final int OFFS_BORDER = 20;     // distance to border
    // size of panel
    private int panelWidth;
    private int panelHeight;
    // max Value for legend
    private double maxValLegend;
    // logarithmic scale
    private boolean isLog;
    // number format
    private NumberFormat formatter;
    
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
        if( this.isLog ) {
            drawLegendLog( g2 );
        } else {
            drawLegendLin( g2 );
        }
    }
    
    private void drawLegendLog( Graphics2D g2 ) {
        // update panel size
        setSize();
        
        // nb of segments
        int nbSegm = (int) Math.log10( this.maxValLegend );
        
        // SEGMENTS
        // first segment 0
        g2.setStroke(new BasicStroke(6));
        g2.setColor( colorFactory( this.maxValLegend ) );
        drawSegment( 0, g2 );
        
        // segment 1 to N
        for( int i = nbSegm; i >= 0; i-- ) {
            int val = (int) Math.round( Math.pow(10, i) );
            g2.setColor( colorFactory( val ) );
            drawSegment( (nbSegm-i), g2 ); 
            
        }
        
        // TEXT LEGEND
        g2.setColor( Color.black );
        g2.setFont(new Font("Dialog", Font.PLAIN, 11) );
        String label = String.valueOf( formatter.format( this.maxValLegend ) );
        drawAnnotation( label, -1, g2);
        
        for( int i = nbSegm; i > 0; i-- ) {
            int val = (int) Math.round( Math.pow(10, i) );
            label = String.valueOf( formatter.format( val ) );
            drawAnnotation( label, (nbSegm-i), g2 );
        }
    }
    
    // legend: draw segment nb
    private void drawSegment( int nb, Graphics2D g2 ) {
 
        // positon: right upper corner
        int posX = getPosX0();
        int posY = getPosY0()  + nb * LEN_SEGM;
        
        g2.drawLine( posX, posY,
                     posX, posY+LEN_SEGM);
        
    }
    
    // legend: draw text for segment nb
    private void drawAnnotation( String txt, int nb, Graphics2D g2 ) {
        // rel. offset to segment
        int offsX = 5;
        int offsY = 12;
        // positon: right upper corner -> minus size of text length
        int strWidth = g2.getFontMetrics().stringWidth( txt );
        int posX = getPosX0() - strWidth - offsX;
        int posY = getPosY0() + offsY + (nb+1) * LEN_SEGM;
        
        g2.drawString(txt, posX, posY);
    }
    
    private void drawLegendLin( Graphics2D g2 ) {
        // update panel size
        setSize();
        
        // nb of segments (fixed)
        int nbSegm = 5;
        
        // SEGMENTS
        g2.setStroke(new BasicStroke(6));
        // segment 1 to N
        for( int i = nbSegm; i > 0; i-- ) {
            double val = this.maxValLegend / nbSegm * i;
            g2.setColor( colorFactory( val ) );
            drawSegment( (nbSegm-i), g2 ); 
        }
        
        // TEXT LEGEND
        g2.setColor( Color.black );
        g2.setFont(new Font("Dialog", Font.PLAIN, 11) );
        String label;
        for( int i = nbSegm; i > 0; i-- ) {
            double val = this.maxValLegend / nbSegm * i;
            label = String.valueOf( formatter.format( val ) );
            drawAnnotation( label, (nbSegm-i-1), g2 );
        }
    }

    public Color colorFactory( double val ) {
        float fraction;
        if( this.isLog ) {
            fraction = (float) (Math.log( val ) / Math.log( this.maxValLegend ) );
        } else {
            double tmp = val  / this.maxValLegend ; 
            fraction = (float) tmp;
        }
        return Color.getHSBColor( 1.0f, fraction, 0.65f );
    }
    
    // reset max 
    protected void resetMaxVal() {
        this.maxValLegend = 0;
    }
    
    // getter
    private int getPosX0() {
        // positon: right upper corner
        return panelWidth - OFFS_BORDER;
    }
    private int getPosY0() {
        // positon: right upper corner
        return OFFS_BORDER;
    }
    // setters
    public void setMaxVal( int max ) {
        if( max > this.maxValLegend ) {
            this.maxValLegend = roundFunc( max );
        } 
        formatter = new DecimalFormat("###,###");
    }
    public void setMaxVal( double max ) {
        if( max > this.maxValLegend ) {
            this.maxValLegend = roundFunc( max );
        } 
        formatter = new DecimalFormat("###,###.00");
    }
    public void setLogScale( boolean bool ) {
        this.isLog = bool;
    }
    
    // rounding function
    private double roundFunc( double val ) {
        int order = (int) Math.log10( val );
        order = (int) Math.pow(10, order);
        return Math.ceil( val/order ) * order;
    }
}
