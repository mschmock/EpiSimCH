//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.graphics;

import ch.manuel.episimch.gui.MainFrame;
import ch.manuel.episimch.DataLoader;
import ch.manuel.geodata.GeoData;
import ch.manuel.geodata.Municipality;
import ch.manuel.population.Population;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;


public class PolygonPanel extends JPanel {

    // access to geodata
    private static GeoData geoData;
    // list polygons
    private static List<Polygon> listPoly; 
    // legend (in jPanel)
    private static Legend legend;
    // transformation
    private final AffineTransform tx;
    private static final int pxBORDER = 16;                     // border in pixel
    private static Map<Integer,Municipality> mapID;             // map with id of municipalities
    // network
    private static Municipality selectedMunicip;
    // draw per absolute or per 1'000?
    private static boolean absoluteRes;
        
        
    // Constructor
    public PolygonPanel() {
        super();
        
        // initialisation
        PolygonPanel.absoluteRes = true;
        listPoly = new ArrayList<>();
        mapID = new HashMap<Integer,Municipality>();
        
        // get polygons from geoData
        PolygonPanel.geoData = DataLoader.geoData; 
        initPolygons();
        
        // legend
        legend = new Legend( this );
        
        // init transformation
        this.tx = new AffineTransform();

    }
    
    // initalisation of polygons (border municipalites)
    private void initPolygons() {
        if( geoData != null ) {
            for (int i = 0; i < GeoData.getNbMunicip(); i++) {
                List<int[]> listPolyX = GeoData.getMunicip(i).getPolyX();
                List<int[]> listPolyY = GeoData.getMunicip(i).getPolyY();
                int nb = listPolyX.size();

                for (int j = 0; j < nb; j++) {
                    listPoly.add(new Polygon (  listPolyX.get(j), 
                                                listPolyY.get(j),
                                                listPolyX.get(j).length )
                    );
                    // create map with municipalities
                    mapID.put( listPoly.size()-1, GeoData.getMunicip(i) );
                }

            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if( geoData != null ) {
            // define transformation
            calcTransformation();
            // draw polygons based on selection
            switch( MainFrame.getPlotIndex() ) {
                case 0:
                    drawNetwork( g2 );
                    break;
                case 1:
                    drawInfections( g2);
                    break;
                case 2:
                    drawK0( g2);
                    break;
                default:
                    drawBorder( g2 );
            }
        }
        // draw legend
        legend.drawLegend( g2 );
    }
    
    // repaint on click
    public void repaintPanel() {
        this.validate();
        this.repaint();
    }
    
    // calculate transformation matrx (translation, scale, mirror...)
    private void calcTransformation() {
        double wd = geoData.getWidth();
        double hg = geoData.getHeight();
        
        double ratio1 = (this.getWidth() - (2 * pxBORDER)) / wd;
        double ratio2 = (this.getHeight()- (2 * pxBORDER)) / hg;
        double scaleFact = ( ratio1 < ratio2) ? ratio1 : ratio2;
        
        AffineTransform trans = AffineTransform.getTranslateInstance( -geoData.getBoundX(), -geoData.getBoundY() );
        AffineTransform scale = AffineTransform.getScaleInstance( scaleFact, scaleFact );
        AffineTransform mirr_y = new AffineTransform( 1, 0, 0, -1, 0, this.getHeight() );
        AffineTransform trans2 = AffineTransform.getTranslateInstance( pxBORDER, -pxBORDER );
        tx.setToIdentity();
        tx.concatenate(trans2);
        tx.concatenate(mirr_y);
        tx.concatenate(scale);
        tx.concatenate(trans);
    }
    
    
    private void drawBorder(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor( Color.black );
        
        for( int i = 0; i < listPoly.size(); i++ ) {
            Shape shape = this.tx.createTransformedShape( listPoly.get(i) );
            g2.draw(shape);

        }  
    }
    
    private void drawNetwork(Graphics2D g2) {
        
        Color col;
        // Filling for selected municip.
        if( PolygonPanel.selectedMunicip != null ) {
            
            int index = PolygonPanel.selectedMunicip.getIndex();
            col = Color.getHSBColor( 1.0f, 1.0f, 0.65f );
            // fill polygon
            fillMunicip( g2, index, col );
        }
        
        // draw connextions
        if( Population.getNetworkIsCreated() && (PolygonPanel.selectedMunicip != null) ) {
            int index = PolygonPanel.selectedMunicip.getIndex();
            
            // array with connection per municipality
            int[] connPerMunicip = GeoData.getConnPerMunicip( index );
            
            // get max value
            int max = 0;
            for ( int i = 0; i < connPerMunicip.length; i++ ) {
                if( max < connPerMunicip[i] ) { max = connPerMunicip[i]; }
            }
            // prepare legend
            legend.setMaxVal(max);      // max value
            legend.setLogScale(true);   // logarithmic scale
            
            // FILL: loop through municipalities
            for (int i = 0; i < GeoData.getNbMunicip(); i++) {
                // don't draw if no connextion
                if( connPerMunicip[i] > 1 ) {
                    // choose color from legend
                    col = legend.colorFactory( connPerMunicip[i] );
                    // fill polygon
                    fillMunicip( g2, i, col );
                }
            }
        }
        // draw borders on top
        this.drawBorder(g2);
        
    }
    
    // draw infections per municipality
    private void drawInfections(Graphics2D g2) {
        
        Color col;
        // get max value
        int maxInfect = Municipality.getMaxInfections();
        float maxInfectPerInhab = Municipality.getMaxInfectPerInhab();
        // loop through municipalities
        for (int i = 0; i < GeoData.getNbMunicip(); i++) {
            
            // choose color
            // absolute or relative
            if( PolygonPanel.absoluteRes ) {
                // draw absoute number
                int nbInfect = GeoData.getMunicip(i).getNbInfections();
                float fraction = (float) Math.log( nbInfect ) / (float) Math.log( maxInfect );
                col = Color.getHSBColor( 1.0f, fraction, 0.65f );
            } else {
                // draw per 1000 inhabitants
                float nbInfPerInhab = GeoData.getMunicip(i).getNbInfectPerInhab();
                float fraction = nbInfPerInhab / maxInfectPerInhab;
                col = Color.getHSBColor( 1.0f, fraction, 0.65f );
            }
            // fill polygon
            fillMunicip( g2, i, col );
        }
        // draw borders on top
        this.drawBorder(g2);
    }
        
    // draw infections per municipality
    private void drawK0(Graphics2D g2) {
        
        Color col;
        // get max value
        float maxK0 = Municipality.getMaxK0();
        // loop through municipalities
        for (int i = 0; i < GeoData.getNbMunicip(); i++) {
            
            float valK0 = GeoData.getMunicip(i).getK0();
            // choose color
            float fraction = valK0 / maxK0;
            col = Color.getHSBColor( 1.0f, fraction, 0.65f );
            // fill polygon
            fillMunicip( g2, i, col );
        }
        // draw borders on top
        this.drawBorder(g2);
    }
    
    // fill polygon of municipality i
    private void fillMunicip(Graphics2D g2, int i, Color col) {
        // color
        g2.setColor( col );
        
        // prepare polygons
        List<int[]> listPolyX = GeoData.getMunicip(i).getPolyX();
        List<int[]> listPolyY = GeoData.getMunicip(i).getPolyY();
        int nb = listPolyX.size();

        // draw polygons
        for (int j = 0; j < nb; j++) {
            Shape shape = this.tx.createTransformedShape(
                    new Polygon (   listPolyX.get(j), 
                                    listPolyY.get(j),
                                    listPolyX.get(j).length ) );
            g2.fill(shape);
        }
    }
    
    // set index / name for element with click
    public void setNameOnClick(Point p) {
        for( int i = 0; i < listPoly.size(); i++ ) {
            Shape shape = this.tx.createTransformedShape( listPoly.get(i) );
            if( shape.contains(p) ) {
                PolygonPanel.selectedMunicip = PolygonPanel.mapID.get(i);
                MainFrame.setStatusText("Selected: " + PolygonPanel.mapID.get(i).getName() );
                break;
            }
        }
    }
    
    // set draw absolute or per 1'000 inhabitants
    public static void setAbsoluteResultats(boolean bool) {
        PolygonPanel.absoluteRes = bool;
    }
    
}
