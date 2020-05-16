//Autor: Manuel Schmocker
//Datum: 12.04.2020

package ch.manuel.episimch.graphics;


import java.awt.Color;
import java.awt.Font;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


// show results in a Frame

public class XY_Chart {
    private static JFreeChart chart;
    private static ChartPanel chartPanel;
    private static XYSeriesCollection dataset1;
    private static XYSeriesCollection dataset2;
    private static XYSeriesCollection dataset3;
    private static XYSeries series11;
    private static XYSeries series12;
    private static XYSeries series13;
    private static XYSeries series21;
    private static XYSeries series22;
    private static XYSeries series31;
    private static XYSeries series32;
    private static XYSeries series33;
    // subplots
    private static XYPlot subplot1;
    private static XYPlot subplot2;
    
    public XY_Chart( java.awt.Dimension dmsn ) {
        
        // chart data
        // subplot 1
        series11 = new XYSeries("Infect.");
        series12 = new XYSeries("Immune");
        series13 = new XYSeries("Death");
        // subplot 2
        series21 = new XYSeries("1d-Infect");
        series22 = new XYSeries("1d-Death");
        // subplot 3
        series31 = new XYSeries("R0");
        series32 = new XYSeries("R0, 7Day");
        series33 = new XYSeries("Incr. rate");
        
        dataset1 = new XYSeriesCollection(series11);
        dataset1.addSeries(series12);
        dataset1.addSeries(series13);
        dataset2 = new XYSeriesCollection(series21);
        dataset2.addSeries(series22);
        dataset3 = new XYSeriesCollection(series31);
        dataset3.addSeries(series32);
        dataset3.addSeries(series33);
        
        chart = createChart(dataset1);
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(dmsn);
        
        // min /max dimensions
        chartPanel.setMaximumDrawWidth(2000);
        chartPanel.setMaximumDrawHeight(1400);

    }
    
    // get chart panel, for integration in jPanel
    public ChartPanel getChartPanel(){
        return chartPanel;
    }
    
    // Creates a chart.
    // @param dataset  the dataset; @return A chart.
    private JFreeChart createChart(final XYDataset dataset1) {
        // create subplot 1...
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis1 = new LogarithmicAxis("Infection / Immune / Death");
        subplot1 = new XYPlot(dataset1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        
        // create subplot 2...
        // final XYItemRenderer renderer2 = new XYBarRenderer();
        final XYItemRenderer renderer2 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis2 = new LogarithmicAxis("Daily Cases");
        rangeAxis2.setAutoRangeIncludesZero(true);
        subplot2 = new XYPlot(dataset2, null, rangeAxis2, renderer2);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        
        // create subplot 3...
        final XYItemRenderer renderer3 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis3 = new NumberAxis("R0 / Incr. Rate");
        rangeAxis3.setAutoRangeIncludesZero(true);
        final XYPlot subplot3 = new XYPlot(dataset3, null, rangeAxis3, renderer3);
        subplot3.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        // parent plot...
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Days"));
        plot.setGap(8.0);

        // add the subplots...
        plot.add(subplot1, 1);
        plot.add(subplot2, 1);
        plot.add(subplot3, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // Customise the subplot1
        subplot1.setBackgroundPaint(Color.lightGray);
        subplot1.setForegroundAlpha(0.65f);
        subplot1.setDomainGridlinePaint(Color.white);
        subplot1.setRangeGridlinePaint(Color.white);
        subplot1.getRangeAxis().setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Customise the subplot2
        subplot2.setBackgroundPaint(Color.lightGray);
        subplot2.setForegroundAlpha(0.65f);
        subplot2.setDomainGridlinePaint(Color.white);
        subplot2.setRangeGridlinePaint(Color.white);
        subplot2.getRangeAxis().setLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        // Customise the subplot3
        subplot3.setBackgroundPaint(Color.lightGray);
        subplot3.setForegroundAlpha(0.65f);
        subplot3.setDomainGridlinePaint(Color.white);
        subplot3.setRangeGridlinePaint(Color.white);
        subplot3.getRangeAxis().setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Linien bearbeiten subplot2
//        subplot2.getRenderer().setSeriesStroke(
//            0, new BasicStroke(
//                3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
//                1.0f)
//        );

        // x-Achse formatieren
        plot.getDomainAxis().setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        return new JFreeChart(null , titleFont, plot, true);
        
    }
    
    // change axes
    protected static void changeLogAxes( boolean bool ) {
        if( bool ) {
            subplot1.setRangeAxis( new LogarithmicAxis("Infection / Immune / Death") );
            subplot2.setRangeAxis( new LogarithmicAxis("Daily Cases") );
        } else {
            subplot1.setRangeAxis( new NumberAxis("Infection / Immune / Death") );
            subplot2.setRangeAxis( new NumberAxis("Daily Cases") );
        }
    }
    
    
    // add points to chart
    public static void addInfection(int day, int y) {
         // log scale: 0 not allowed 
        if( y > 1 ) {
            series11.add(day, y);
        }
    }
    public static void addImmune(int day, int y) {
         // log scale: 0 not allowed 
        if( y > 1 ) {
            series12.add(day, y);
        }
    }
    public static void addDeath(int day, int y) {
         // log scale: 0 not allowed 
        if( y > 1 ) {
            series13.add(day, y);
        }
    }
    public static void addDailyInf(int day, int y) {
        // log scale: 0 not allowed 
        if( y > 1 ) {
            series21.add(day, y);
        }
    }
    public static void addDailyDeath(int day, int y) {
        // log scale: 0 not allowed 
        if( y > 1) {
            series22.add(day, y);
        }
    }
    public static void addR0(int day, float y) {
        // log scale: 0 not allowed 
        if( y > 0.1 ) {
            series31.add(day, y);
        }
    }
    public static void add7DayR0(int day, float y) {
        // log scale: 0 not allowed 
        if( y > 0.1 ) {
           series32.add(day, y); 
        }
    }
    public static void addIncrRate(int day, float y) {
        // log scale: 0 not allowed 
        if( y > 0.1 ) {
            series33.add(day, y);
        }
    } 
    
    public static void resetChart() {
        series11.clear();
        series12.clear();
        series13.clear();
        series21.clear();
        series22.clear();
        series31.clear();
        series32.clear();
        series33.clear();
    }
}

