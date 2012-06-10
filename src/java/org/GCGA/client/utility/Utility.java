/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

/**
 *
 * @author Eurialo
 */
public interface Utility {

    public static final byte MIN_CUSTOM_GRAPH_VERTICES_NUMBER = 3;

    public static final byte INVALID_INDEX = -1;

    public static final int MARGIN = 5;         //Could be short, but Integer is needed by most methods
    public static final short DEFAULT_PANELS_WIDTH = 800;
    public static final short DEFAULT_TOP_PANEL_HEIGHT = 190;

    public static final short DEFAULT_WINDOW_WIDTH = 800;
    public static final short DEFAULT_WINDOW_HEIGHT = 800;

    public static final byte W_MARGIN = 18;
    public static final byte H_MARGIN = 36;
    public static final byte W_MARGIN_REDUCED = 10;
    public static final byte H_MARGIN_REDUCED = 26;
    public static final short NEWS_WINDOW_SIZE = 400;

    public static final byte LABEL_HEIGHT = 20;
    public static final byte INSTRUCTIONS_PANEL_HEIGHT = 15;


    public static final byte ORIGINAL_VERTEX_SIZE = 25;

    public static final int HOVER_WIDTH = 250;      //Could be short, but Integer is needed by most methods
    public static final int HOVER_IMG_WIDTH = 200;
    public static final int HOVER_OPACITY = 80;

    public static final String HOVER_TITLE_COLOR = "#f50000;";
    public static final String HOVER_TITLE_BGCOLOR = "#ffd937;";
    public static final String HOVER_BODY_BGCOLOR = "#fff69b;";
    public static final String HOVER_BORDER = "2px solid #ffaf03;";
    public static final String HOVER_FONT ="arial, verdana, serif;";



    public static final String HOVER_TITLE_PREAMBLE = "<div style='width:100%; background-color: "+HOVER_TITLE_BGCOLOR+" border: " + HOVER_BORDER + " border-bottom-width: 0px;'><center><b style='color:"+HOVER_TITLE_COLOR+" font-family: "+HOVER_FONT+"; font-size: 14px;'>";
    public static final String HOVER_TITLE_CLOSURE = "</b></center></div>";

    public static final String HOVER_BODY_PREAMBLE = "<div style='width:100%; background-color: "+HOVER_BODY_BGCOLOR+ " border: "+ HOVER_BORDER + "'><br><center><div style='padding:2px; font-family: "+HOVER_FONT+" font-size: 11px;'>";
    public static final String HOVER_BODY_CLOSURE = "</div></center></div>";

    public static final String LIGHT_BACKGROUND = "#BBBBBB";
    public static final String DARK_BACKGROUND = "#202020";
    
    public static final String LISTGRID_BACKGROUND = "#101010";

    public static final String NEWS_DATE_COLOR = "#FF0000";
    public static final String DEFAULT_LIST_ENTRIES_COLOR = "#FFFFFF";

    public static final String  INNER_PANEL_BORDER = "1px dashed gray";
    public static final String  INNER_PANEL_BORDER_BRIGHT = "1px dashed red";

    public static final String IMAGE_DIR = "images/";

    public static final String SHOW_TOP_PANEL_COOKIE_NAME = "showTopPanel";

    public static final String DATA_FOLDER_GA_FITNESS = "Fitness";

        public static final String DATA_LABEL_AVERAGE_FITNESS = "GA Fitness Average";
        public static final String DATA_LABEL_BEST_FITNESS = "GA Best Element Fitness";
        public static final String DATA_LABEL_VARIANCE_FITNESS = "GA Fitness Variance";
        public static final String DATA_LABEL_STDDEV_FITNESS = "GA Fitness Standard Deviation";

    public static final String DATA_FOLDER_GA_ELEMENTS_FITNESS = "ElementsFitness";

        public static final String DATA_LABEL_ELEMENT_FITNESS = "Element n° ";

    public static final String INVALID_EMAIL_CHARACTERS = "/\\^\"'\n\t\r@";

}
