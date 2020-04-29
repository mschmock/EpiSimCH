//Autor: Manuel Schmocker
//Datum: 02.04.2020

package ch.manuel.graphics;

import ch.manuel.episimch.DataLoader;
import ch.manuel.geodata.GeoData;
import ch.manuel.geodata.Municipality;
import ch.manuel.population.Person;
import ch.manuel.population.Population;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;


public class DrawPolygon extends JPanel {

    // access to geodata
    private GeoData geoData;
    
    // list polygons
    private static List<Polygon> listPoly; 
    
    // transformation
    private final AffineTransform tx;
    private static final int pxBORDER = 16;    // border in pixel
    private static Map<Integer,Municipality> mapID;             // map with id 
    
    // network
    private static Municipality selectedMunicip;
    
    // draw per absolute or per 1'000?
    private static boolean absoluteRes;
        
        
    // Constructor
    public DrawPolygon() {
        super();
        
        // initialisation
        DrawPolygon.absoluteRes = true;
        listPoly = new ArrayList<>();
        mapID = new HashMap<Integer,Municipality>();
        
        // get polygons from geoData
        this.geoData = DataLoader.geoData; 
        initPolygons();
        
        // init transformation
        this.tx = new AffineTransform();

    }
    
    // initalisation of polygons
    private void initPolygons() {
        if( geoData != null ) {
            for (int i = 0; i < geoData.getNbMunicip(); i++) {
                List<int[]> listPolyX = geoData.getMunicip(i).getPolyX();
                List<int[]> listPolyY = geoData.getMunicip(i).getPolyY();
                int nb = listPolyX.size();

                for (int j = 0; j < nb; j++) {
                    listPoly.add(new Polygon (  listPolyX.get(j), 
                                                listPolyY.get(j),
                                                listPolyX.get(j).length )
                    );
                    // create map with municipalities
                    mapID.put( listPoly.size()-1, geoData.getMunicip(i) );
                }

            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if( geoData != null ) {
            // define transformation
            calcTransformation();
            // draw polygons based on selection
            switch( MainFrame.getPlotIndex() ) {
                case 0:
                    drawNetwork(g);
                    break;
                case 1:
                    drawInfections(g);
                    break;
                case 2:
                    drawK0(g);
                    break;
                default:
                    drawBorder((Graphics2D) g);
            }
        }
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
        //System.out.println("wd: " + wd + ", hg: " + hg + " X: " + geoData.getBoundX() + " Y: " + geoData.getBoundY());
    }
    
    
    private void drawBorder(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor( Color.black );
        
        for( int i = 0; i < listPoly.size(); i++ ) {
            Shape shape = this.tx.createTransformedShape( listPoly.get(i) );
            g2.draw(shape);

        }  
    }
    
    private void drawNetwork(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        Color col;
        // Filling for selected municip.
        if( DrawPolygon.selectedMunicip != null ) {
            
            int index = DrawPolygon.selectedMunicip.getIndex();
            col = Color.getHSBColor( 1.0f, 1.0f, 0.65f );
            // fill polygon
            fillMunicip( g2, index, col );
        }
        
        // draw connextions
        if( Population.getNetworkIsCreated() && (DrawPolygon.selectedMunicip != null) ) {
            int index = DrawPolygon.selectedMunicip.getIndex();
            int[] arrInhabit = DataLoader.geoData.getMunicip( index ).getArrListPers();
            
            // array with connection per municipality
            int[] connPerMunicip = new int[ DataLoader.geoData.getNbMunicip() ];
            // loop through inhabitants
            for (int i = 0; i < arrInhabit.length; i++ ) {
                Person pers = Population.getPerson( arrInhabit[i] );
                
                // loop through connections
                for ( int j = 0; j < pers.getListNetwork().size(); j++) {
                    int ind = Population.getPerson( pers.getListNetwork().get(j) ).getIndexMunicip();
                    connPerMunicip[ind]++;
                }
            }
            
            // get max value
            int max = 0;
            for ( int i = 0; i < connPerMunicip.length; i++ ) {
                if( max < connPerMunicip[i] ) { max = connPerMunicip[i]; }
            }
            
            // FILL: loop through municipalities
            for (int i = 0; i < geoData.getNbMunicip(); i++) {
                // don't draw if no connextion
                if( connPerMunicip[i] > 1 ) {
                    // choose color
                    float fraction = (float) Math.log( connPerMunicip[i] ) / (float) Math.log( max );
                    col = Color.getHSBColor( 1.0f, fraction, 0.65f );
                    // fill polygon
                    fillMunicip( g2, i, col );
                }
            }
        }
        
        // draw borders on top
        this.drawBorder(g2);
    }
    
    // draw infections per municipality
    private void drawInfections(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        Color col;
        // get max value
        int maxInfect = Municipality.getMaxInfections();
        float maxInfectPerInhab = Municipality.getMaxInfectPerInhab();
        // loop through municipalities
        for (int i = 0; i < geoData.getNbMunicip(); i++) {
            
            // choose color
            // absolute or relative
            if( DrawPolygon.absoluteRes ) {
                // draw absoute number
                int nbInfect = geoData.getMunicip(i).getNbInfections();
                float fraction = (float) Math.log( nbInfect ) / (float) Math.log( maxInfect );
                col = Color.getHSBColor( 1.0f, fraction, 0.65f );
            } else {
                // draw per 1000 inhabitants
                float nbInfPerInhab = geoData.getMunicip(i).getNbInfectPerInhab();
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
    private void drawK0(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        Color col;
        // get max value
        float maxK0 = Municipality.getMaxK0();
        // loop through municipalities
        for (int i = 0; i < geoData.getNbMunicip(); i++) {
            
            float valK0 = geoData.getMunicip(i).getK0();
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
        List<int[]> listPolyX = geoData.getMunicip(i).getPolyX();
        List<int[]> listPolyY = geoData.getMunicip(i).getPolyY();
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
                DrawPolygon.selectedMunicip = DrawPolygon.mapID.get(i);
                MainFrame.setStatusText("Selected: " + DrawPolygon.mapID.get(i).getName() );
                break;
            }
        }
    }
    // set draw absolute or per 1'000 inhabitants
    public static void setAbsoluteResultats(boolean bool) {
        DrawPolygon.absoluteRes = bool;
    }
}
