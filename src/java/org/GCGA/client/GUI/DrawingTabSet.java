/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.tab.Tab;  
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Eurialo
 */
public class DrawingTabSet extends TabSet{

    private Tab[] tabs;
    private static final byte TABS = 6;

    private static final byte TAB_INTRO = 0;
    private static final byte TAB_CONTACTS = 1;
    private static final byte TAB_EXPLAIN = 2;
    private static final byte TAB_LIST = 3;
    private static final byte TAB_ANALYSIS = 4;
    private static final byte TAB_DRAWING = 5;
    
    private boolean tabDrawingEnabled = false;

    private static GraphPanel gp;
    private static AnalysisPanel anP;

    private static ListPanel lp;

    private AlgorithmPanel ap = null;

    private static DrawingTabSet thisTabset;

        private DrawingTabSet(){
            super();
            setTabBarPosition(Side.TOP);
            tabs = new Tab[TABS];

            addIntroTab();
            addContactsTab();
            addExplainTab();

            //this.setScrollbarSize(1);
            
            addDrawingTab();
            addListTab();

            addAnalysisTab();

            this.setWidth( gp.getSize() + Utility.W_MARGIN  );//+ 25
            this.setHeight( gp.getSize() + Utility.H_MARGIN  );//+ 50

            this.addTabSelectedHandler( new TabSelectedHandler() {

            public void onTabSelected(TabSelectedEvent event) {
                //alert(event.getTabNum()+" ");
                    switch(event.getTabNum() ){
                        case TAB_LIST:
                            lp.retrieveList(MenuCanvas.getVerticesNbr(), MenuCanvas.getGraphType() );
                            if (ap!=null){
                                ap.hidePanel();
                            }
                            break;
                        case TAB_CONTACTS:
                            ContactsPanel.get().init();
                            if (ap!=null){
                                ap.hidePanel();
                            }
                            break;
                        case TAB_EXPLAIN:
                            if (ap==null){
                                tabs[TAB_EXPLAIN].setPane(ap = AlgorithmPanel.get());
                            }else{
                                ap.showPanel();
                            }
                            break;
                        case TAB_ANALYSIS:
                            if ( AnalysisPanel.get().isReady() )
                                AnalysisPanel.get().repaint(true);
                            else
                                SC.warn("The analysis panel will be available <b style='color:red'>after you run the Genetic Algorithm</b> (or the AIS).");
                            if (ap!=null){
                                ap.hidePanel();
                            }
                            break;
                        default:
                            if (ap!=null){
                                ap.hidePanel();
                            }
                            break;
                    }
                }
            });
        }

        public static DrawingTabSet get(){
            if (thisTabset == null)
                thisTabset = new DrawingTabSet();
            return thisTabset;
        }
        private void addIntroTab(){
            tabs[TAB_INTRO] = new Tab("Info");
            tabs[TAB_INTRO].setPane(InfoPanel.get());

            this.addTab(tabs[TAB_INTRO]);
        }

        private void addContactsTab(){
            tabs[TAB_CONTACTS] = new Tab("Contacts");
            tabs[TAB_CONTACTS].setPane(ContactsPanel.get());

            this.addTab(tabs[TAB_CONTACTS]);
        }

        private void addExplainTab(){
            tabs[TAB_EXPLAIN] = new Tab("Algorithm description");
            this.addTab(tabs[TAB_EXPLAIN]);
        }
        
        private void addDrawingTab(){

            tabs[TAB_DRAWING] = new Tab("Graph Embeddings");
            Canvas wrapper = new Canvas();

            gp=new GraphPanel();
            wrapper.setWidth(gp.getSize());
            wrapper.setHeight(gp.getSize());
            wrapper.setMargin(0);
            wrapper.addChild(gp);
            wrapper.setTop(0);
            wrapper.setLeft(0);
            tabs[TAB_DRAWING].setPane( wrapper );
            
            //tabs[TAB_DRAWING].setDisabled(true);
        }

        private void addAnalysisTab(){

            tabs[TAB_ANALYSIS] = new Tab("Algorithm analysis");
            Canvas wrapper = new Canvas();

            anP = AnalysisPanel.get();
            wrapper.setWidth(anP.getWidth());
            wrapper.setHeight(anP.getHeight());
            wrapper.setMargin(0);
            wrapper.addChild(anP);
            wrapper.setTop(0);
            wrapper.setLeft(0);
            tabs[TAB_ANALYSIS].setPane( wrapper );
            this.addTab(tabs[TAB_ANALYSIS]);

            //tabs[TAB_DRAWING].setDisabled(true);
        }

        private void addListTab(){
             tabs[TAB_LIST] = new Tab("Best Results");
            this.addTab(tabs[TAB_LIST]);

            tabs[TAB_LIST].setPane(lp = new ListPanel(gp.getSize()) );
        }
   /*
    private native void alert(String message)/*-{
		alert(message);
    }-* /;
   */

        public void enableGraphPanel(){
            if (!tabDrawingEnabled){
                this.addTab(tabs[TAB_DRAWING]);
                tabDrawingEnabled = true;
            }
        }

        public GraphPanel getGraphPanel(){
            return gp;
        }

        public void selectGraphPanel(){
            TopPanel.collapse();
            enableGraphPanel();
            this.selectTab(TAB_DRAWING);
        }

        public boolean getListPanelSelected(){
            return this.getSelectedTabNumber() == TAB_LIST;
        }

        public void retrieveList(){
            lp.retrieveList(MenuCanvas.getVerticesNbr(), MenuCanvas.getGraphType() );
        }

}
