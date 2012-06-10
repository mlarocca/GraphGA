/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

//import com.google.gwt.user.client.ui.Label;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import org.GCGA.client.News;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Eurialo
 */
public class NewsWindow extends Window{

    private static final short DEFAULT_WINDOW_WIDTH = 400;
    //private static final int DEFAULT_WINDOW_HEIGHT = 400;
    public NewsWindow(News news) {
        super();

        this.setTitle("News: " + news.getTitle());
        String text = "<br/><p style='color:"+Utility.NEWS_DATE_COLOR+";'><b>"+news.getDay()+"</b>" + " ("+news.getTime()+")</p>"+
                "<br/><b>"+news.getBody()+"</b>";

        Label bodyLabel = new Label(text);
        bodyLabel.setWidth100();
        bodyLabel.setHeight100();
        bodyLabel.setPadding(Utility.MARGIN);
        //bodyLabel.setValign(VerticalAlignment.TOP);
        //bodyLabel.setAutoFit(true);
        this.setCanDragResize(false);
        this.setWidth(DEFAULT_WINDOW_WIDTH);
        //this.setHeight(DEFAULT_WINDOW_HEIGHT);
        this.setAutoSize(true );


        bodyLabel.setLeft(Utility.MARGIN);
        bodyLabel.setTop(Utility.MARGIN);
        this.addItem(bodyLabel);

        this.addCloseClickHandler( new CloseClickHandler() {

            public void onCloseClick(CloseClientEvent event) {
                destroy();
            }
        });

        this.show();
        this.centerInPage();
    }
}
