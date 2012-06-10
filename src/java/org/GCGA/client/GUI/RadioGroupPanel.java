package org.GCGA.client.GUI;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import com.google.gwt.user.client.ui.RadioButton;
import com.smartgwt.client.widgets.AnimationCallback;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;

import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Eurialo
 */
public class RadioGroupPanel extends Canvas{

    RadioGroupPanel thisPanel;

    private String ID = "";

    private int index = Utility.INVALID_INDEX;

    private Label title = null;

    private VLayout buttonsPanel;

    private AnimationCallback onChange;

    private final ArrayList<RadioButton> buttons;

    private static final byte BUTTON_HEIGHT = 10;

    private static String HOVER_TITLE_PREAMBLE = "<div style='width:100%; position:absolute; top:0px; left:0px; background-color:"+Utility.HOVER_TITLE_BGCOLOR +"'><center><b style='font-family:"+Utility.HOVER_FONT+" font-size: 14px;'>";
    private static String HOVER_TITLE_CLOSURE = "</b></center></div>";

    private static String HOVER_BODY_PREAMBLE = "<br/><div style='padding:2px; font-family:" + Utility.HOVER_FONT + " font-size: 11px;'>";
    private static String HOVER_BODY_CLOSURE = "</div>";

    public RadioGroupPanel() {
        buttons  = new ArrayList<RadioButton>();
        buttonsPanel = new VLayout();
        init();
    }

    public RadioGroupPanel(String title) {
        buttons  = new ArrayList<RadioButton>();
        buttonsPanel = new VLayout();
        init();
        this.setRadioGroupTitle(title);
        this.setHeight(buttonsPanel.getBottom());
    }

    private void init(){
        thisPanel = this;
        this.addChild(buttonsPanel);
        //thisPanel.setHoverStyle("padding:0px;");
        this.setHoverMoveWithMouse(true);
        generateID();
        buttonsPanel.setHeight(BUTTON_HEIGHT);
        this.setHeight(2*BUTTON_HEIGHT);
        this.setHoverOpacity(50);

    }

    private void generateID(){
        if (title!=null)
            ID += title;
        ID += "_"+Math.random();
    }

    public void setRadioGroupTitle(String t){
        if (title != null){
            this.removeChild(title);
        }
        title = new Label("<b>"+t+"</b>");
        this.addChild(title);
        title.setTop(Utility.MARGIN);
        title.setLeft(Utility.MARGIN);
        title.setHeight(BUTTON_HEIGHT);
        title.setWidth(this.getWidth());

        buttonsPanel.setTop(title.getBottom() + Utility.MARGIN);

    }

    public void addButton(String text){
        final RadioButton rb = new RadioButton(ID);
        rb.setText(text);
        rb.setWidth(buttonsPanel.getWidthAsString());
        rb.setHeight(BUTTON_HEIGHT+"px");
        rb.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//                alert ("Click " + index + " : " + getSelectedIndex());
                if ( index != getSelectedIndex() ){
                    index = getSelectedIndex();
                    //fireEvent(new ValueChangeEvent<Integer>(index) );
                    //ValueChangeEvent.fire(thisPanel, index);
                    //DomEvent.fireNativeEvent(Document.get().createChangeEvent(), thisPanel);
                    //alert("Fired ok");
                    //MenuCanvas.onChangedGraphType(index);
                    onChange.execute(false);
                }
            }
        });

        buttons.add( rb );
        buttonsPanel.setHeight(buttonsPanel.getHeight() + BUTTON_HEIGHT);
        buttonsPanel.addMember(rb);
//        form.setFields(buttons);
    }

    @Override
    public void setWidth(int width){
        super.setWidth(width);
        buttonsPanel.setWidth(id);
        for ( RadioButton rb : buttons){
            rb.setWidth(width+"px");
        }
    }

    @Override
    public void setWidth(String width){
        super.setWidth(width);
        buttonsPanel.setWidth(id);
        for ( RadioButton rb : buttons){
            rb.setWidth(width);
        }
    }


    public RadioButton getField(String name) throws IllegalArgumentException{
         for ( RadioButton b : buttons ){
            if (b.getText().equals(name) ){
                return b;
            }
        }
         throw new IllegalArgumentException();
    }

    public void SetFieldPrompt(String fieldName, final String title, final String body){
        RadioButton b = getField(fieldName);
        b.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(MouseOverEvent event) {
                thisPanel.setHoverWidth(Utility.HOVER_IMG_WIDTH);
                thisPanel.setHoverHeight(Utility.HOVER_IMG_WIDTH);
                //thisPanel.setHoverStyle("margin:0px;");
                thisPanel.setPrompt(HOVER_TITLE_PREAMBLE+title+HOVER_TITLE_CLOSURE+HOVER_BODY_PREAMBLE+body+HOVER_BODY_CLOSURE);
//                thisPanel.setPrompt("<div style='width:100%; background-color: #FF5555;'><center><b>"+title+"</b></center></div><br/>"+body);
            }
        });/*
        b.addMouseOutHandler( new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                thisPanel.setPrompt("");
            }
        });*/
    }

    public void SetFieldPrompt(String fieldName, final String title, final String body, final int width, final int height){
        RadioButton b = getField(fieldName);
        b.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(MouseOverEvent event) {
                thisPanel.setHoverWidth(width);
                thisPanel.setHoverHeight(height);
                thisPanel.setPrompt(HOVER_TITLE_PREAMBLE+title+HOVER_TITLE_CLOSURE+HOVER_BODY_PREAMBLE+body+HOVER_BODY_CLOSURE);
            }
        });/*
        b.addMouseOutHandler( new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                thisPanel.setPrompt("");
            }
        });*/
    }

    public void setDefaultValue(String value){
         for (int i=0; i<buttons.size(); i++ ){

            if ( buttons.get(i).getText().equals(value) ){
                buttons.get(i).setValue(true);
                index = i;
            }
        }
    }

    public void setDefaultValue(int index){

        if (index>0 && index<buttons.size()){
            //selectItem(buttons.get(index));
            buttons.get(index).setValue(true);
        }
    }

    public int getSelectedIndex() throws ArrayIndexOutOfBoundsException{
        for (int i=0; i<buttons.size(); i++)
            if ( buttons.get(i).getValue() )
                return i;

        throw new ArrayIndexOutOfBoundsException();
    }

    public String getValue() throws ArrayIndexOutOfBoundsException{
        RadioButton tmp;
        for (int i=0; i<buttons.size(); i++){
            if ( (tmp = buttons.get(i) ).getValue() )
                return tmp.getText();
         }
        throw new ArrayIndexOutOfBoundsException();
    }

      /**
   * when firing a change event, this handler is used
   */
  public void addChangeHandler(AnimationCallback handler) {
    onChange = handler;
  }

}