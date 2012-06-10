/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.ajax;

import org.GCGA.client.algorithm.GeneticAlgorithm;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

/**
 *
 * @author Marcello
 * Singleton class
 */
public class GraphStorer {

    private static GraphStorer storer = null;

    private static String url;

    public static void setUrl(String urlParam){
            url = urlParam;
    }

    private GraphStorer() {
        setUrl("../saveGraph_json.php");
        //setUrl("http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&output=json");
    }

    public static GraphStorer get(){
        if (storer == null ){
            storer = new GraphStorer();
        }
        return storer;
    }

    public void store(GeneticAlgorithm ga){
//        tempVN = vNbr;
//        tempGT = graphType;


        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str = "graph="+ ga.bestElementToJSON();
            //WebLoader.showLoader();

            builder.sendRequest(params_str, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
//                    GeneticAlgorithm.alert("error");
                    return;
               // Couldn't connect to server (could be timeout, SOP violation, etc.)
                    //WebLoader.hideLoader();
    //                SC.warn("An error has occurred while trying to retrieve the data.<br/>We apologize.<br/>Please feel free to report us the problem.");
                //    alert("Impossible to store");
                }

                public void onResponseReceived(Request request, Response response) {
//                    GeneticAlgorithm.alert("ok");
                    return;
/*
                      if (200 == response.getStatusCode() ) {
                          // Process the response in response.getText()
//                            alert(response.getText());

                       }else {
                            // Handle the error.  Can get the status text from response.getStatusText()
                            //DO NOTHING

                      }
 * 
 */
                }
          });

        } catch (RequestException e) {
          // Couldn't connect to server
        }

        //return results;
    }
/*
    private native void alert(String message)/*-{
		alert(message);
    }-* /;
*/
}

