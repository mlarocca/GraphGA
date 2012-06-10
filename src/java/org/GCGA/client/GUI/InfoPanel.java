package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import org.GCGA.client.utility.Utility;


public class InfoPanel extends Canvas{

    private static InfoPanel thisPanel = null;

    private static final String text = "<font size='3'><span style='font-weight: bold;'><span style='color: "+Utility.DEFAULT_LIST_ENTRIES_COLOR+";'><span style='color: rgb(255, 0, 0);'>WARNING: This website properly works only with Google Chrome and Firefox v. 3.x</span></span></span></font><font size='3'><span style='font-weight: bold;'><span style='color: "+Utility.DEFAULT_LIST_ENTRIES_COLOR+";'><span style='color: rgb(255, 0, 0);'> at the moment. IE 8 also works fine, except for some minor graphic bug</span></span></span></font><font size='3'><span style='font-weight: bold;'><span style='color: "+Utility.DEFAULT_LIST_ENTRIES_COLOR+";'><span style='color: rgb(255, 0, 0);'>. Cross browser compatibility will hopefully be improved asap, please be patient. We are very sorry for the incovenience.</span></span></span></font><br><font size='4'><span style='color: "+Utility.DEFAULT_LIST_ENTRIES_COLOR+";'><span style='font-weight: bold;'><span style='font-family: arial,helvetica,sans-serif;'><br>Minimum crossing number Genetic Algorithm + Artificial Immune System<br>Author: Marcello La Rocca</span></span></span></font><br><br><div style='text-align: justify;'><font size='3'><span style='font-weight: bold;'><span style='color: "+Utility.DEFAULT_LIST_ENTRIES_COLOR+";'>When a graph is embedded in a tridimensional (or polidimensional) space, it is <span style='color: rgb(255, 0, 0);'>always </span>possible to draw its edges without any intersection.<br>Unfortunately, this is not true in a bidimensional space: there exists (an infinite number of) graphs that aren't <span style='font-style: italic; color: rgb(255, 0, 0);'>planar</span>, i.e. they can't be embedded&nbsp; in a plane without intersection among their edges.<br>Since Kuratowski's theorems it is well known that K<font size='1'>3,3</font> and K<font size='1'>5</font> are the simplest non-planar graphs and many polinomial algorithms have been found for planarity testing.<br>The issue of minimum crossing number for non-planar graphs, however, hasn't been as thoroughly investigated, also because its far higher complexity.<br><br>It is at this point that evolutionary algorithms step in.<br>This algorithm explore the specific case of minimum rectilinear crossing number, i.e. the minimum crossing number for graphs whose edges are drawn using segments (instead of generic Jordan curves).<br><br>On the left, users will find a list of menues that allow to choose among all the options provided as well as to tune the algorithm parameters.<br>Graph panel should be considered first, in order to choose on which graph to work on: it is possible to create instances of tree kind of graphs just choosing&nbsp; how many vertex they have, plus it will be possible to create every graph with a visual interface.<br>Once the algorithm has been ran at least once, the best embedding found will be automatically shown, plus it will be possible to navigate through all the results.<br><br>Using this Tab Panel, instead, users are allowed to take a look at the best embeddings ever found for the selected graph (or to check the list of all the embeddings found): a database of the best results is kept, and every time the algorithm is ran if any good result is found it is automatically stored in the database. <br><br>For details about the algorithm used, check out the 'Algorithm description' tab.<br><br>For everything else, just explore the website and feel free to try all the different options you want.<br>And if you feel you can help to improve the site, your feedback is really appreciated.<span style='color: rgb(255, 0, 0);'></span><br></span></span></font><br></div>";

    private InfoPanel(int panelWidth, int panelHeight) {
        super();
        this.setWidth(panelWidth);
        this.setHeight(panelHeight);

        Label textLabel = new Label(text);
        textLabel.setBackgroundColor(Utility.LISTGRID_BACKGROUND);
        textLabel.setWidth(panelWidth);
        textLabel.setHeight(panelHeight);
        textLabel.setPadding(Utility.MARGIN);
        this.addChild(textLabel);
    }

    public static void init(int panelWidth, int panelHeight){
        if ( thisPanel == null)
            thisPanel = new InfoPanel(panelWidth, panelHeight);
    }

    public static InfoPanel get(){
        if ( thisPanel == null)
            thisPanel = new InfoPanel(Utility.DEFAULT_PANELS_WIDTH, Utility.DEFAULT_PANELS_WIDTH);

        return thisPanel;
    }

}
