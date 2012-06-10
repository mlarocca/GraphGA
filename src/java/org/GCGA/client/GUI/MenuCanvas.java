/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import org.GCGA.client.algorithm.GeneticAlgorithm;
import org.GCGA.client.algorithm.AIS;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
/*
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
 * */
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.RestoreClickEvent;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;


import com.smartgwt.client.widgets.form.DynamicForm;

import com.smartgwt.client.widgets.form.fields.TextItem;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.BooleanCallback;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.events.RestoreClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.GCGA.client.Graphs.Complete_Bipartite_Graph;
import org.GCGA.client.ajax.GraphStorer;
import org.GCGA.client.GraphType;
import org.GCGA.client.Graphs.Square_Grid_Graph;
import org.GCGA.client.Graphs.Triangular_Mesh_Graph;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;
//import org.GCGA.client.GraphStorer;


/**
 *
 * @author Marcello
 */
public class MenuCanvas extends Canvas{

    private static MenuCanvas menu = null;

    private static Window gaWindow;
    private static Window gaParametersWindow;
    private static Window graphParametersWindow;

    private static Window elementsNavigationWindow;

    private static CheckboxItem AISCheckBoxItem;
    private static Slider verticesSlider;
    private static Slider populationSlider;
    private static Slider iterationsSlider;
    
    private static Slider gridsizeSlider;
    private static Slider crossoverSlider;
    private static Slider mutation_1_Slider;
    private static Slider mutation_2_Slider;
    private static Slider mutation_3_Slider;

    private static Slider elementSlider;

    private static Button graphCreatorButton;
    private static Button defaultButton;

    private static final String ImageComplete = "K5.png";
    private static final String ImageBipartiteComplete = "K3_3.png";
    private static final String ImageSquareMesh = "SquareMesh.png";
    private static final String ImageTriangleMesh = "TriangleMesh.png";
    //GraphPanel graphPanel = null;

    private static DrawingTabSet tabset = null;

    private static GeneticAlgorithm ga;

    private static int counter;

    //private static RadioGroupItem radioGroupItem;
    private static RadioGroupPanel  radioGroup;

    private static TextItem rankItem;
    private static TextItem crossingItem;
    private static TextItem fitnessItem;

    private static LoadingBar loading;

    private final static String FIRST_TIME_COOKIE_NAME = "FirstTimeCookie";

    private final static int GA_WINDOW_HEIGHT = 190;
    private final static int GA_PARAM_WINDOW_HEIGHT = 390;
    private final static int GRAPH_WINDOW_HEIGHT = 220;
    private final static int NAVIGATION_WINDOW_HEIGHT = 130;

    private final static short MENU_WIDTH  =  300;
    private final static short WINDOWS_WIDTH  =  MENU_WIDTH - 2*Utility.MARGIN;

    private final static byte LABEL_WIDTH = 60;
    private final static short SLIDER_WIDTH = 250;
    private final static byte PARAM_LABEL_WIDTH = 70;
    private final static short PARAM_SLIDER_WIDTH = 240;

    private static final short GRIDSIZE_SLIDER_PRECISION = 100;
    private static final byte CROSSOVER_SLIDER_PRECISION = 100;
    private static final short MUTATION_SLIDER_PRECISION = 1000;

    private static final byte MIN_VERTICES_NUMBER = 5;
    private static final byte MAX_VERTICES_NUMBER = 50;

    private static final String LOAD_DEFAULT_AIS_BUTTON_TITLE = "Load AIS defaults";
    private static final String LOAD_DEFAULT_GA_BUTTON_TITLE = "Load GA defaults";

     private static final String GRAPH_NAME_COMPLETE = "Complete";
     private static final String GRAPH_NAME_COMPLETE_BIPARTITE = "Complete Bipartite";
     private static final String GRAPH_NAME_SQUARE_MESH = "Square Mesh";
     private static final String GRAPH_NAME_TRIANGLE_MESH = "Triangle Mesh";
     private static final String GRAPH_NAME_CUSTOM = "Custom";


    public static MenuCanvas get(){
        if (menu==null){
//            alert("Menu object hasn't been properly set");
            throw new Error("Menu object hasn't been properly set");
        }
        return menu;
    }

    public static void init(DrawingTabSet dtab){
        if (menu!=null){
//            alert("Menu object already created");
            throw new Error("Menu object already created");
        }
        menu = new MenuCanvas(dtab);

    }
    private MenuCanvas( DrawingTabSet dtab ){
        super();
        tabset = dtab;
        this.setBackgroundColor("black");
        this.setWidth(MENU_WIDTH);
        this.setHeight(tabset.getHeight());

        //this.setTitle(id);
        //graphPanel = gp;
        this.createGAWindow();
        this.createGAParametersWindow();
        this.createGraphWindow();
        this.createNavigationWindow();

        /*
        final Button JsonTestButton = new Button("JSON");
        JsonTestButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                try{
                  alert( ga.bestElementToJSON() );
                }
                catch(Exception e){
                    alert(e.getMessage());
                    alert(e.getLocalizedMessage());

                }
            }
        });
        JsonTestButton.setTop(500);
        this.addChild(JsonTestButton);*/
    }
    private void createGAWindow(){
        gaWindow = new Window();
        this.addChild(gaWindow);
        gaWindow.setTitle("Evolutionary Algorithm");
        gaWindow.setShowCloseButton(false);
        gaWindow.setShowMinimizeButton(true);

        gaWindow.setAnimateMinimize(false);

        //gaWindow.setMinimizeTime(ANIMATE_TIME);

        gaWindow.addMinimizeClickHandler(new MinimizeClickHandler() {

            public void onMinimizeClick(MinimizeClickEvent event) {
                //alert("Minimizing " + gaWindow.getDefaultMinimizeHeight());
                event.cancel();

                gaWindow.minimize();
                gaParametersWindow.setTop(gaWindow.getBottom() + Utility.MARGIN);
                graphParametersWindow.setTop(gaParametersWindow.getBottom() + Utility.MARGIN );
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        });

        gaWindow.addRestoreClickHandler( new RestoreClickHandler() {

            public void onRestoreClick(RestoreClickEvent event) {

                event.cancel();
                gaWindow.restore();
                gaParametersWindow.setTop(gaWindow.getBottom() + Utility.MARGIN);
                graphParametersWindow.setTop(gaParametersWindow.getBottom() + Utility.MARGIN );
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        }  );

        gaWindow.setCanDragReposition(false);
        gaWindow.setCanDragResize(false);
        gaWindow.setWidth(WINDOWS_WIDTH);
        gaWindow.setHeight(GA_WINDOW_HEIGHT);
        gaWindow.setLeft(Utility.MARGIN);
        gaWindow.setTop( Utility.MARGIN );
//        gaWindow.setAutoSize(true);

        Canvas wrapper = new Canvas();
        wrapper.setLeft(0);
        wrapper.setTop(0);
        wrapper.setWidth(WINDOWS_WIDTH - Utility.W_MARGIN);
        wrapper.setHeight(GA_WINDOW_HEIGHT - Utility.H_MARGIN);
        gaWindow.addItem(wrapper);

        final Button runButton = new Button("Run");
        runButton.setWidth(50);
        runButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if ( Cookies.getCookie(FIRST_TIME_COOKIE_NAME)!=null ){
                    /*if (AISCheckBoxItem.getValueAsBoolean())
                        runAIS();
                    else*/
                        runGA();
                }else{

                    if ( Cookies.isCookieEnabled() )
                        Cookies.setCookie(FIRST_TIME_COOKIE_NAME, "0", null);

                    SC.confirm("This is the first time you run this Algorithm.<br/>" +
                            "You should know it may take a few minutes to complete execution.<br/>" +
                            "You'll probably be asked by your browser if you want toterminate the script: " +
                            "rest assured, it's no deadlock, the computation just takes some time.<br/>" +
                            "However, it's higly recommended to make your first trials with small graphs (i.e. less than 8 vertices) and small values for population and iterations, in order to check your hardware performance first.<br/><br/>" +
                            "Do you want to proceed anyway?", new BooleanCallback() {
                        public void execute(Boolean value) {
                            if (value != null && value) {
                                runGA();
                            }
                        }
                    });
                }


            }
        });
        runButton.setTop(Utility.MARGIN);
        runButton.setLeft(Utility.MARGIN);
        runButton.setLayoutAlign(Alignment.CENTER);

        UtilityFunctions.setWidgetPrompt(runButton, "Run Algorithm", "Click to launch the selected evolutive algorithm.<br/>Algorithms can be parameterized through the Parameters Tab below.");

        wrapper.addChild(runButton);

        AISCheckBoxItem = new CheckboxItem();
        AISCheckBoxItem.setTitle("AIS");
        AISCheckBoxItem.setValue(true);
        AISCheckBoxItem.setTitleOrientation(TitleOrientation.RIGHT);

//        AISCheckBoxItem.setTop();
//        AISCheckBoxItem.setLeft(0);

         UtilityFunctions.setWidgetPrompt(AISCheckBoxItem, "Artificial Immune System", "Activate or deactivate Artificial Immune Systems features (<i>Clonal Selection</i> and <i>Idiotypic Network Approach</i>) in addition to the classic Genetic Algorithm.");
         AISCheckBoxItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                if (AISCheckBoxItem.getValueAsBoolean())
                    defaultButton.setTitle( LOAD_DEFAULT_AIS_BUTTON_TITLE );
                else
                    defaultButton.setTitle( LOAD_DEFAULT_GA_BUTTON_TITLE );
            }
        });
        final DynamicForm AISForm = new DynamicForm();
        AISForm.setFields(AISCheckBoxItem);
        AISForm.setTop(runButton.getTop());
        AISForm.setLeft(runButton.getRight() + Utility.MARGIN);
        wrapper.addChild(AISForm);

        loading = new LoadingBar();
        wrapper.addChild(loading);
        loading.setTop(Utility.MARGIN);
        loading.setLeft(Utility.MARGIN + 100 + Utility.MARGIN);//(AISForm.getRight() + Utility.MARGIN);
        //loading.stopLoader();

        AISForm.sendToBack();

         populationSlider = new Slider("Population");
         populationSlider.setVertical(false);
         populationSlider.setLabelWidth(LABEL_WIDTH);
         populationSlider.setMinValue(10);
         populationSlider.setMaxValue(1000);
         populationSlider.setNumValues(991);
         
         populationSlider.setTop(AISForm.getBottom());
         populationSlider.setLeft(Utility.MARGIN);
         populationSlider.setWidth(SLIDER_WIDTH);
         populationSlider.setValue(20);

         wrapper.addChild(populationSlider);

         iterationsSlider = new Slider("Iterations");
         iterationsSlider.setVertical(false);
         iterationsSlider.setLabelWidth(LABEL_WIDTH);
         iterationsSlider.setMinValue(10);
         iterationsSlider.setMaxValue(1000);
         iterationsSlider.setNumValues(991);
         
         iterationsSlider.setTop(populationSlider.getBottom() + Utility.MARGIN);
//         iterationsSlider.setLeft(60);
         iterationsSlider.setWidth(SLIDER_WIDTH);
         iterationsSlider.setValue(20);

         wrapper.addChild(iterationsSlider);

         gaWindow.show();

    }
    
    private void createGAParametersWindow(){
        gaParametersWindow = new Window();
        this.addChild(gaParametersWindow);
        gaParametersWindow.setTitle("Parameters");
        gaParametersWindow.setShowCloseButton(false);
        gaParametersWindow.setShowMinimizeButton(true);

        gaParametersWindow.setAnimateMinimize(false);
        gaParametersWindow.addMinimizeClickHandler(new MinimizeClickHandler() {

            public void onMinimizeClick(MinimizeClickEvent event) {
                //alert("Minimizing " + gaParametersWindow.getDefaultMinimizeHeight());
                event.cancel();

                gaParametersWindow.minimize();
                graphParametersWindow.setTop(gaParametersWindow.getBottom() + Utility.MARGIN );
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        });

        gaParametersWindow.addRestoreClickHandler( new RestoreClickHandler() {

            public void onRestoreClick(RestoreClickEvent event) {

                event.cancel();
                gaParametersWindow.restore();
                graphParametersWindow.setTop(gaParametersWindow.getBottom() + Utility.MARGIN );
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        }  );

        gaParametersWindow.setCanDragReposition(false);
        gaParametersWindow.setCanDragResize(false);
        gaParametersWindow.setWidth(WINDOWS_WIDTH);
        gaParametersWindow.setHeight(GA_PARAM_WINDOW_HEIGHT);
        gaParametersWindow.setLeft(Utility.MARGIN);
        gaParametersWindow.setTop( gaWindow.getBottom() + Utility.MARGIN );

        Canvas wrapper = new Canvas();
        wrapper.setLeft(0);
        wrapper.setTop(0);
        wrapper.setWidth(WINDOWS_WIDTH - Utility.W_MARGIN);
        wrapper.setHeight(GA_WINDOW_HEIGHT - Utility.H_MARGIN);
        gaParametersWindow.addItem(wrapper);

         defaultButton = new Button(LOAD_DEFAULT_AIS_BUTTON_TITLE);

         defaultButton.setTop(Utility.MARGIN);
         defaultButton.setLeft(Utility.MARGIN);

         defaultButton.addClickHandler( new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (AISCheckBoxItem.getValueAsBoolean() ){
                    loadDefaultAISParameters();
                }else{
                    loadDefaultGAParameters();
                }
            }
         });

         UtilityFunctions.setWidgetPrompt(defaultButton, "Load Default Parameters", "Loads default parameters for the selected algorithm.<br/>Values can then be tuned again using the sliders below.");

         gridsizeSlider = new Slider("Gridsize");
         gridsizeSlider.setVertical(false);
         gridsizeSlider.setLabelWidth(PARAM_LABEL_WIDTH);

         gridsizeSlider.setMinValue(1);
         gridsizeSlider.setMaxValue(5);
         gridsizeSlider.setNumValues(GRIDSIZE_SLIDER_PRECISION+1);
         gridsizeSlider.setRoundValues(false);
         gridsizeSlider.setRoundPrecision((int)Math.ceil(Math.log10(GRIDSIZE_SLIDER_PRECISION)));

         gridsizeSlider.setTop(defaultButton.getBottom() + Utility.MARGIN);
         gridsizeSlider.setLeft(Utility.MARGIN);
         gridsizeSlider.setWidth(PARAM_SLIDER_WIDTH);

         UtilityFunctions.setWidgetPrompt(gridsizeSlider, "Gridsize multipling factor", "Tune the size of the grid where the graph's vertices will be placed.<br/>The graph's vertex number will be multiplied for this value.");
         wrapper.addChild(gridsizeSlider);

         crossoverSlider = new Slider("Crossover");
         crossoverSlider.setVertical(false);
         crossoverSlider.setLabelWidth(PARAM_LABEL_WIDTH);
         
         crossoverSlider.setMinValue((float)0.0);
         crossoverSlider.setMaxValue((float)1.0);
         crossoverSlider.setNumValues(CROSSOVER_SLIDER_PRECISION+1);
         crossoverSlider.setRoundValues(false);
         crossoverSlider.setRoundPrecision((int)Math.ceil(Math.log10(CROSSOVER_SLIDER_PRECISION)));

         crossoverSlider.setTop(gridsizeSlider.getBottom());
         crossoverSlider.setLeft(Utility.MARGIN);
         crossoverSlider.setWidth(PARAM_SLIDER_WIDTH);
         //crossoverSlider.setStepPercent((float)0.1);

         UtilityFunctions.setWidgetPrompt(crossoverSlider, "Crossover ratio", "Tunes the percent of crossover ratio c_r:<br/>c_r% of individuals in a new generation will be created by the crossover of 2 parents individulas, while the rest will be randomly choosen among the current generation individuals.");

         wrapper.addChild(crossoverSlider);

         wrapper.addChild(defaultButton);

         mutation_1_Slider = new Slider("Mutation 1");
         mutation_1_Slider.setVertical(false);
         mutation_1_Slider.setLabelWidth(PARAM_LABEL_WIDTH);
         mutation_1_Slider.setMinValue((float)0.0);
         mutation_1_Slider.setMaxValue(new Float(GeneticAlgorithm.MAX_MUTATION_RATIO));
         mutation_1_Slider.setNumValues(MUTATION_SLIDER_PRECISION+1);
         mutation_1_Slider.setRoundValues(false);
         mutation_1_Slider.setRoundPrecision((int)Math.ceil(Math.log10(MUTATION_SLIDER_PRECISION)));

         mutation_1_Slider.setTop(crossoverSlider.getBottom() );
         mutation_1_Slider.setLeft(Utility.MARGIN);
         mutation_1_Slider.setWidth(PARAM_SLIDER_WIDTH);

         UtilityFunctions.setWidgetPrompt(mutation_1_Slider, "Mutation 1 ratio", "Tunes the ratio of occurrence of the first kind of mutation among new generations.");

         wrapper.addChild(mutation_1_Slider);

         mutation_2_Slider = new Slider("Mutation 2");
         mutation_2_Slider.setVertical(false);
         mutation_2_Slider.setLabelWidth(PARAM_LABEL_WIDTH);
         mutation_2_Slider.setMinValue((float)0.0);
         mutation_2_Slider.setMaxValue(new Float(GeneticAlgorithm.MAX_MUTATION_RATIO));
         mutation_2_Slider.setNumValues(MUTATION_SLIDER_PRECISION+1);
         mutation_2_Slider.setRoundValues(false);
         mutation_2_Slider.setRoundPrecision((int)Math.ceil(Math.log10(MUTATION_SLIDER_PRECISION)));

         mutation_2_Slider.setTop(mutation_1_Slider.getBottom() );
         mutation_2_Slider.setLeft(Utility.MARGIN);
         mutation_2_Slider.setWidth(PARAM_SLIDER_WIDTH);

         UtilityFunctions.setWidgetPrompt(mutation_2_Slider, "Mutation 2 ratio", "Tunes the ratio of occurrence of the second kind of mutation among new generations.");

         wrapper.addChild(mutation_2_Slider);

         mutation_3_Slider = new Slider("Mutation 3");
         mutation_3_Slider.setVertical(false);
         mutation_3_Slider.setLabelWidth(PARAM_LABEL_WIDTH);
         mutation_3_Slider.setMinValue((float)0.0);
         mutation_3_Slider.setMaxValue(new Float(GeneticAlgorithm.MAX_MUTATION_RATIO));
         mutation_3_Slider.setNumValues(MUTATION_SLIDER_PRECISION+1);
         mutation_3_Slider.setRoundValues(false);
         mutation_3_Slider.setRoundPrecision((int)Math.ceil(Math.log10(MUTATION_SLIDER_PRECISION)));

         mutation_3_Slider.setTop(mutation_2_Slider.getBottom() );
         mutation_3_Slider.setLeft(Utility.MARGIN);
         mutation_3_Slider.setWidth(PARAM_SLIDER_WIDTH);

         UtilityFunctions.setWidgetPrompt(mutation_3_Slider, "Mutation 3 ratio", "Tunes the ratio of occurrence of the third kind of mutation among new generations.");

         wrapper.addChild(mutation_3_Slider);

         loadDefaultAISParameters();
         //gaParametersWindow.show();
         gaParametersWindow.minimize();
    }

    private void createGraphWindow(){
         graphParametersWindow = new Window();
         //graphParametersWindow.setEdgeSize(2);
         //graphParametersWindow.setShowEdges(true);
         this.addChild(graphParametersWindow);
         graphParametersWindow.setTitle("Graph parameters");
         graphParametersWindow.setShowCloseButton(false);
         graphParametersWindow.setShowMinimizeButton(true);
         graphParametersWindow.setCanDragReposition(false);
         graphParametersWindow.setCanDragResize(false);

        graphParametersWindow.setAnimateMinimize(false);
        graphParametersWindow.addMinimizeClickHandler(new MinimizeClickHandler() {

            public void onMinimizeClick(MinimizeClickEvent event) {
                //alert("Minimizing " + gaParametersWindow.getDefaultMinimizeHeight());
                event.cancel();

                graphParametersWindow.minimize();
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        });

        graphParametersWindow.addRestoreClickHandler( new RestoreClickHandler() {

            public void onRestoreClick(RestoreClickEvent event) {

                event.cancel();
                graphParametersWindow.restore();
                elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN );
            }
        }  );

         graphParametersWindow.setWidth(WINDOWS_WIDTH);
         graphParametersWindow.setHeight(GRAPH_WINDOW_HEIGHT);
         graphParametersWindow.setLeft(Utility.MARGIN);
//TODO:
         graphParametersWindow.setTop(gaParametersWindow.getBottom() + Utility.MARGIN  );

        Canvas wrapper = new Canvas();
        wrapper.setLeft(0);
        wrapper.setTop(0);
        wrapper.setWidth(WINDOWS_WIDTH - Utility.W_MARGIN);
        wrapper.setHeight(GRAPH_WINDOW_HEIGHT - Utility.H_MARGIN);
        graphParametersWindow.addItem(wrapper);
  
         verticesSlider = new Slider("Vertices");
         verticesSlider.setVertical(false);
         verticesSlider.setLabelWidth(LABEL_WIDTH);
         verticesSlider.setMinValue(MIN_VERTICES_NUMBER);
         verticesSlider.setMaxValue(MAX_VERTICES_NUMBER);
         verticesSlider.setNumValues(MAX_VERTICES_NUMBER-MIN_VERTICES_NUMBER+1);
         
         verticesSlider.setTop(Utility.MARGIN);
         verticesSlider.setLeft(Utility.MARGIN);
         verticesSlider.setWidth(SLIDER_WIDTH);

         verticesSlider.addValueChangedHandler( new ValueChangedHandler() {

            public void onValueChanged(ValueChangedEvent event) {
                if ( !verticesSlider.valueIsChanging() && tabset.getListPanelSelected() ){
                    tabset.retrieveList();
                }
            }
        } );

         verticesSlider.addDrawHandler(new DrawHandler() {
             public void onDraw(DrawEvent event) {
                 verticesSlider.addValueChangedHandler(new ValueChangedHandler() {
                     public void onValueChanged(ValueChangedEvent event) {
                         adjustVerticesValue();
                     }
                 });
             }
         });
        // verticesSlider.setLeft(120);

         wrapper.addChild(verticesSlider);

         radioGroup = new RadioGroupPanel("Graph Type");

         radioGroup.addButton(GRAPH_NAME_COMPLETE);
         radioGroup.addButton(GRAPH_NAME_COMPLETE_BIPARTITE);
         radioGroup.addButton(GRAPH_NAME_SQUARE_MESH);
         radioGroup.addButton(GRAPH_NAME_TRIANGLE_MESH);
         radioGroup.addButton(GRAPH_NAME_CUSTOM);

         radioGroup.setDefaultValue(GRAPH_NAME_COMPLETE);

         radioGroup.setTop(verticesSlider.getBottom() + Utility.MARGIN);
         radioGroup.setLeft(Utility.MARGIN);
         radioGroup.setWidth(150);
         radioGroup.setBorder(Utility.INNER_PANEL_BORDER);
         radioGroup.SetFieldPrompt(GRAPH_NAME_COMPLETE,"Complete Graph","<img src='"+Utility.IMAGE_DIR+ImageComplete+ "'/>");
         radioGroup.SetFieldPrompt(GRAPH_NAME_COMPLETE_BIPARTITE,"Complete Bipartite Graph","<img src='"+Utility.IMAGE_DIR+ImageBipartiteComplete+ "'/>");
         radioGroup.SetFieldPrompt(GRAPH_NAME_SQUARE_MESH,"Square Mesh Graph","<img src='"+Utility.IMAGE_DIR+ImageSquareMesh+ "'/>");
         radioGroup.SetFieldPrompt(GRAPH_NAME_TRIANGLE_MESH,"Triangle Mesh Graph","<img src='"+Utility.IMAGE_DIR+ImageTriangleMesh+ "'/>");
         radioGroup.SetFieldPrompt(GRAPH_NAME_CUSTOM,"Custom Graph","Create your own Graph with <i style='color:red;'>Graph creation</i> tool", Utility.HOVER_IMG_WIDTH, 60);

         radioGroup.addChangeHandler(new AnimationCallback() {
            public void execute(boolean earlyFinish) {
                onChangedGraphType();
            }
        });

         wrapper.addChild(radioGroup);

         graphCreatorButton = new Button("Graph Tool");
         graphCreatorButton.setWidth(80);
         graphCreatorButton.setDisabled(true);
         graphCreatorButton.setTop(150);
         graphCreatorButton.setLeft(180);

         graphCreatorButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                GraphCreationWindow.get();
            }
        });

        wrapper.addChild(graphCreatorButton);
        // graphParametersWindow.show();
    }

    private void createNavigationWindow(){
         elementsNavigationWindow = new Window();
         this.addChild(elementsNavigationWindow);
         //elementsNavigationWindow.setEdgeSize(2);
         //elementsNavigationWindow.setShowEdges(true);
         elementsNavigationWindow.setTitle("Elements navigation");
         elementsNavigationWindow.setShowCloseButton(false);
         elementsNavigationWindow.setShowMinimizeButton(true);
         elementsNavigationWindow.setCanDragReposition(false);
         elementsNavigationWindow.setCanDragResize(false);

         elementsNavigationWindow.setWidth(WINDOWS_WIDTH);
         elementsNavigationWindow.setHeight(NAVIGATION_WINDOW_HEIGHT);
         
         elementsNavigationWindow.setLeft(Utility.MARGIN);
//TODO:
         elementsNavigationWindow.setTop(graphParametersWindow.getBottom() + Utility.MARGIN  );

         elementsNavigationWindow.setAnimateTime(2000);
         
         Canvas wrapper = new Canvas();
         wrapper.setLeft(0);
         wrapper.setTop(0);
         wrapper.setWidth(WINDOWS_WIDTH - Utility.W_MARGIN);
         wrapper.setHeight(NAVIGATION_WINDOW_HEIGHT - Utility.H_MARGIN);
         elementsNavigationWindow.addItem(wrapper);         

        final DynamicForm resultsForm = new DynamicForm();

         rankItem = new TextItem();
         rankItem.setTitle("Element Rank");

         rankItem.setWidth(50);
         rankItem.setTitleAlign(Alignment.RIGHT);
         rankItem.setDisabled(true);

         crossingItem = new TextItem();
         crossingItem.setTitle("Crossing Number");
         crossingItem.setDisabled(true);

         //crossingItem.disable();
         //verticesItem.setRequired(true);

         crossingItem.setWidth(50);
         crossingItem.setTitleAlign(Alignment.RIGHT);

         fitnessItem = new TextItem();
         fitnessItem.setTitle("Fitness");
         //verticesItem.setRequired(true);
         fitnessItem.setDisabled(true);

         fitnessItem.setWidth(50);
         fitnessItem.setTitleAlign(Alignment.RIGHT);

         resultsForm.setFields(rankItem, crossingItem, fitnessItem);

         resultsForm.setTop( Utility.MARGIN );
         resultsForm.setLeft(170);
         resultsForm.setWidth(100);
         resultsForm.setHeight(100);

         wrapper.addChild(resultsForm);

         elementSlider = new Slider(""){
             private HandlerRegistration onChanged = null;

            @Override
             public void disable(){
                 if (onChanged!=null){
                     onChanged.removeHandler();
                 }
                 super.disable();
             }

            @Override
            public void enable(){
                super.enable();
                onChanged=addValueChangedHandler(new ValueChangedHandler() {
                    public void onValueChanged(ValueChangedEvent event) {
                        if (!elementSlider.valueIsChanging() ){
                            counter = event.getValue()-1;
                            rankItem.setValue(counter+1);
                            crossingItem.setValue(GeneticAlgorithm.getCrossingNumber(counter));
                            fitnessItem.setValue(GeneticAlgorithm.getFitness(counter));
                            tabset.selectGraphPanel();
                            GeneticAlgorithm.draw(tabset.getGraphPanel(), counter);
                            }
                    }
                });
            }

            @Override
            public HandlerRegistration addValueChangedHandler(ValueChangedHandler handler) {
                return onChanged = super.addValueChangedHandler(handler);
            }

         };
         elementSlider.setLabelWidth(0);
         elementSlider.setVertical(false);
//         elementSlider.setLabelWidth(PARAM_LABEL_WIDTH);

         elementSlider.setTop( Utility.MARGIN);
         elementSlider.setLeft(Utility.MARGIN);
         elementSlider.setWidth(resultsForm.getLeft() - 2 * Utility.MARGIN);

         UtilityFunctions.setWidgetPrompt(elementSlider, "Element position", "Select the element to be drawn.");
         wrapper.addChild(elementSlider);
 
        final Button prevButton = new Button("Previous");
        prevButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if ( counter > 0 ){
                     elementSlider.setValue((--counter)+1); //elementSlider.onChangValue will take care of everything
//                     rankItem.setValue((--counter)+1);
                     //crossingItem.setValue(GeneticAlgorithm.getCrossingNumber(counter));
                     //fitnessItem.setValue(GeneticAlgorithm.getFitness(counter));
//                     tabset.selectGraphPanel();
//                     GeneticAlgorithm.draw(tabset.getGraphPanel(), counter);
                }

            }
        });

        prevButton.setWidth(60);
        prevButton.setLeft( Utility.MARGIN + (elementSlider.getWidth() - Utility.MARGIN - 2 * prevButton.getWidth() )/2  );//Utility.MARGIN );
        prevButton.setTop( elementSlider.getBottom() );//resultsForm.getTop() + resultsForm.getHeight()/2 );

        UtilityFunctions.setWidgetPrompt(prevButton, "Previous element", "Shows the graph embedding carried by the previous element in the final rank, and displays its fitness and crossing number.");

        wrapper.addChild(prevButton);



        final Button nextButton = new Button("Next");
        nextButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (counter < GeneticAlgorithm.populationSize()-1 ){
                    elementSlider.setValue((++counter)+1); //elementSlider.onChangValue will take care of everything
//                    rankItem.setValue((++counter)+1);
//                    crossingItem.setValue(GeneticAlgorithm.getCrossingNumber(counter));
//                    fitnessItem.setValue(GeneticAlgorithm.getFitness(counter));
                    
//                    tabset.selectGraphPanel();
//                    GeneticAlgorithm.draw(tabset.getGraphPanel(), counter);
                }
            }
        });

        nextButton.setWidth(prevButton.getWidth());
        nextButton.setLeft(prevButton.getRight() + Utility.MARGIN);
        nextButton.setTop(prevButton.getTop());
        UtilityFunctions.setWidgetPrompt(nextButton, "Next element", "Shows the graph embedding carried by the next element in the final rank, and displays its fitness and crossing number.");
        wrapper.addChild(nextButton);
        
         elementsNavigationWindow.setVisible(false);

    }

    public static int getVerticesNbr(){
            return (int)verticesSlider.getValue();
    }

    public static short getGraphType(){

        if (radioGroup.getValue().equals(GRAPH_NAME_COMPLETE) )
            return GraphType.COMPLETE_GRAPH;
        if (radioGroup.getValue().equals(GRAPH_NAME_COMPLETE_BIPARTITE) )
            return GraphType.COMPLETE_BIPARTITE_GRAPH;
        if (radioGroup.getValue().equals(GRAPH_NAME_SQUARE_MESH) )
            return GraphType.SQUARE_GRID_GRAPH;
        if (radioGroup.getValue().equals(GRAPH_NAME_TRIANGLE_MESH) )
            return GraphType.TRIANGULAR_MESH_GRAPH;
        if (radioGroup.getValue().equals(GRAPH_NAME_CUSTOM) )
            if ( GraphCreationWindow.getGraph() == null || GraphCreationWindow.getGraph().verticesNumber()< Utility.MIN_CUSTOM_GRAPH_VERTICES_NUMBER )
                SC.warn("<b>The Custom Graph created is not valid</b><br/>Please use the graph creation tool or choose another graph type");
            else
                return GraphType.CUSTOM_GRAPH;
        return GraphType.ERROR;
    }

    private static void loadDefaultGAParameters(){
        gridsizeSlider.setValue(GeneticAlgorithm.DEFAULT_GRIDSIZE_FACTOR);
        crossoverSlider.setValue( new Float(GeneticAlgorithm.DEFAULT_CROSSOVER_RATIO) );
        mutation_1_Slider.setValue( new Float(GeneticAlgorithm.DEFAULT_MUTATION_1_RATIO) );
        mutation_2_Slider.setValue( new Float(GeneticAlgorithm.DEFAULT_MUTATION_2_RATIO) );
        mutation_3_Slider.setValue( new Float(GeneticAlgorithm.DEFAULT_MUTATION_3_RATIO) );
    }

    private static void loadDefaultAISParameters(){
        gridsizeSlider.setValue(GeneticAlgorithm.DEFAULT_GRIDSIZE_FACTOR);
        crossoverSlider.setValue( new Float(AIS.DEFAULT_CROSSOVER_RATIO) );
        mutation_1_Slider.setValue( new Float(AIS.DEFAULT_MUTATION_1_RATIO) );
        mutation_2_Slider.setValue( new Float(AIS.DEFAULT_MUTATION_2_RATIO) );
        mutation_3_Slider.setValue( new Float(AIS.DEFAULT_MUTATION_3_RATIO) );
    }

    private static void adjustVerticesValue(){
        int vn;
        int newVn;
        switch(getGraphType()){
            case GraphType.COMPLETE_GRAPH:
                break;
            case GraphType.COMPLETE_BIPARTITE_GRAPH:
                vn = (int)verticesSlider.getValue();
                newVn = Complete_Bipartite_Graph.correctNodeNumber( vn );
                if (vn!=newVn){
                   // alert( vn + " | " + newVn) ;
                    verticesSlider.setValue( newVn );
                }
                break;
            case GraphType.SQUARE_GRID_GRAPH:
                vn = (int)verticesSlider.getValue();
                newVn = Square_Grid_Graph.correctNodeNumber( vn );
                if (vn!=newVn){
                   // alert( vn + " | " + newVn) ;
                    verticesSlider.setValue( newVn );
                }                
                break;
            case GraphType.TRIANGULAR_MESH_GRAPH:
                vn = (int)verticesSlider.getValue();
                newVn = Triangular_Mesh_Graph.correctNodeNumber( vn );
                if (vn!=newVn){
                   // alert( vn + " | " + newVn) ;
                    verticesSlider.setValue( newVn );
                }                
                break;
        }
    }

    private void showNavigationWindow(){
        if ( elementsNavigationWindow.getVisibility().compareTo( Visibility.HIDDEN) == 0 ){
            elementsNavigationWindow.animateShow(AnimationEffect.FADE);
        }
        setupElementSlider();
    }

    private void runGA(){
        int vertices;
        if ( AISCheckBoxItem.getValueAsBoolean() ){
             switch (getGraphType()){
                case GraphType.CUSTOM_GRAPH:
                    AIS.init((int)iterationsSlider.getValue(), (int)populationSlider.getValue(), GraphCreationWindow.getGraph() , gridsizeSlider.getValue());
                    vertices = GraphCreationWindow.getGraph().verticesNumber();
                    break;
                default:
                    vertices = (int)verticesSlider.getValue();
                    AIS.init((int)iterationsSlider.getValue(), (int)populationSlider.getValue(), vertices, getGraphType(), gridsizeSlider.getValue());
            }
        }else{
        GeneticAlgorithm.resetInstance();
            switch (getGraphType()){
                case GraphType.CUSTOM_GRAPH:
                    GeneticAlgorithm.init((int)iterationsSlider.getValue(), (int)populationSlider.getValue(), GraphCreationWindow.getGraph() , gridsizeSlider.getValue());
                    vertices = GraphCreationWindow.getGraph().verticesNumber();
                    break;
                default:
                    vertices = (int)verticesSlider.getValue();
                    GeneticAlgorithm.init((int)iterationsSlider.getValue(), (int)populationSlider.getValue(), vertices, getGraphType(), gridsizeSlider.getValue());
            }
        }
        ga = GeneticAlgorithm.get();

        GeneticAlgorithm.setCrossoverRatio( crossoverSlider.getValue() );
        GeneticAlgorithm.setMutation_1_Ratio( mutation_1_Slider.getValue());
        GeneticAlgorithm.setMutation_2_Ratio( mutation_2_Slider.getValue() );
        GeneticAlgorithm.setMutation_3_Ratio( mutation_3_Slider.getValue() );

        tabset.getGraphPanel().setGrid(ga, vertices);

         GeneticAlgorithm.setProgressBar ( loading );
         ga.run();
         switch(GeneticAlgorithm.getGraph().getType()){
             case GraphType.COMPLETE_BIPARTITE_GRAPH:
             case GraphType.COMPLETE_GRAPH:
                 GraphStorer.get().store(ga);
                 break;
             default:
                 break;
         }

         showNavigationWindow();
         counter = 0;
         rankItem.setValue(counter+1);
         crossingItem.setValue(GeneticAlgorithm.getCrossingNumber(counter));
         fitnessItem.setValue(GeneticAlgorithm.getFitness(counter));
         tabset.selectGraphPanel();
         GeneticAlgorithm.draw(tabset.getGraphPanel());
         AnalysisPanel.get().updatePanelStatus();
    }
/*
    private static void setCounterValue(TextItem t, int c){
        t.setValue(c+1);
    }

    private static void setCounterValue(Slider s, int c){
        s.setValue(c+1);
    }
*/
    private static void onChangedGraphType(){
            String value = radioGroup.getValue() ;
            if (value.equals("Custom") ){
                GraphCreationWindow.get();
                graphCreatorButton.setDisabled(false);
                verticesSlider.setDisabled(true);
            }else{
                graphCreatorButton.setDisabled(true);
                verticesSlider.setDisabled(false);
                adjustVerticesValue();
                if ( !verticesSlider.valueIsChanging() && tabset.getListPanelSelected() ){
                    tabset.retrieveList();
                }
            }
    }

    private static void setupElementSlider(){
         elementSlider.disable();

         elementSlider.setMinValue(1);
         elementSlider.setMaxValue(GeneticAlgorithm.populationSize());
         elementSlider.setNumValues(GeneticAlgorithm.populationSize());

         elementSlider.enable();

    }

}
