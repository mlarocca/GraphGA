/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.ajax;

import org.GCGA.client.GUI.ListPanel;
import org.GCGA.client.GUI.WebLoader;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import org.GCGA.client.GraphEmbedding;
import org.GCGA.client.GraphType;


/**
 *
 * @author Marcello
 * Singleton class
 */
public class GraphRetriever {

    private static GraphRetriever retriever = null;

    private static String url;

    public static void setUrl(String urlParam){
            url = urlParam;
    }

    private GraphRetriever() {
        setUrl("../loadGraphs_json.php");
        //setUrl("http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&output=json");
    }

    public static GraphRetriever get(){
        if (retriever == null ){
            retriever = new GraphRetriever();
        }
        return retriever;

    }

    public void retrieve(int vNbr, short graphType, final ListPanel listPanel){
//        tempVN = vNbr;
//        tempGT = graphType;

        
//alert("retrieving");
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str;
            if ( graphType == GraphType.ALL ){
                params_str = null; 
            }else{
                params_str = "vNbr="+vNbr+"&gT="+graphType;
            }
            WebLoader.showLoader();
            
            builder.sendRequest(params_str, new RequestCallback() {

            public void onError(Request request, Throwable exception) {
           // Couldn't connect to server (could be timeout, SOP violation, etc.)
                WebLoader.hideLoader();
                SC.warn("An error has occurred while trying to retrieve the data.<br/>We apologize.<br/>Please feel free to report us the problem.");

            }

            public void onResponseReceived(Request request, Response response) {
                //alert(request.toString());
                //alert(response.getStatusCode()+" " + response.getStatusText());
               JSONObject jsonObj;
               JSONArray jsonArr;
               JSONValue valueObject;

               int errorCounter = 0;

               WebLoader.hideLoader();

              if (200 == response.getStatusCode() ) {
                  // Process the response in response.getText()
                    //alert(response.getText());
                    valueObject = JSONParser.parse(response.getText());
               }/* else if( 0 == response.getStatusCode()){
                   String json_txt;
                   switch (tempGT){
                       case GraphType.COMPLETE_GRAPH:
                           switch (tempVN){
                               case 5:
                                    json_txt = "[{\"graphType\":0, \"vNbr\":5, \"gridSideSize\":5, \"crossingNbr\":1, \"gridPositions\":\"[24,17,1,11,15]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.46242073797398286\", \"ip\":\"user_ip\"}]";
                                   break;
                               case 6:
                                    json_txt = "[{\"graphType\":0, \"vNbr\":6, \"gridSideSize\":12, \"crossingNbr\":3, \"gridPositions\":\"[12,90,56,137,34,39]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.24369076317572735\", \"ip\":\"user_ip\"},{\"graphType\":0, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":5, \"gridPositions\":\"[27,19,30,34,26,7]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.32266884975072974\", \"ip\":\"user_ip\"}]";

                                   break;
                               case 7:

                                    json_txt = "[{\"graphType\":0, \"vNbr\":7, \"gridSideSize\":14, \"crossingNbr\":9, \"gridPositions\":\"[170,149,179,78,158,36,105]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.05339494146514623\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":13, \"gridPositions\":\"[26,45,32,27,16,17,1]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.20980421845276787\", \"ip\":\"user_ip\"},{\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":17, \"gridPositions\":\"[37,34,38,29,43,0,24]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.4941210984920711\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":14, \"gridPositions\":\"[8,25,0,44,31,6,45]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.843934017018278\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":19, \"gridPositions\":\"[40,25,30,36,1,31,5]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.016010468657375876\", \"ip\":\"user_ip\"}]";
                                   break;
                               case 8:
                                    json_txt = "[{\"graphType\":0, \"vNbr\":8, \"gridSideSize\":18, \"crossingNbr\":19, \"gridPositions\":\"[158,135,241,139,51,2,68,194]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.29633729472610804\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":8, \"gridSideSize\":16, \"crossingNbr\":23, \"gridPositions\":\"[71,172,92,15,251,62,18,59]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.2999735739354723\", \"ip\":\"user_ip\"}]";
                                   break;
                               default:
                                   return;
                           }
                           break;
                       case GraphType.COMPLETE_BIPARTITE_GRAPH:
                           switch (tempVN){
                               case 6:
                                    json_txt = "[{\"graphType\":1, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":1, \"gridPositions\":\"[10,13,18,14,1,26]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.9794368384236602\", \"ip\":\"user_ip\"},{\"graphType\":1, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":1, \"gridPositions\":\"[15,28,7,25,21,17]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.4177318303277614\", \"ip\":\"user_ip\"}]";
                                   break;
                               case 8:
                                    json_txt = "[{\"graphType\":1, \"vNbr\":8, \"gridSideSize\":8, \"crossingNbr\":4, \"gridPositions\":\"[40,20,33,14,17,56,34,0]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.0944721155156566\", \"ip\":\"user_ip\"}, {\"graphType\":1, \"vNbr\":8, \"gridSideSize\":8, \"crossingNbr\":5, \"gridPositions\":\"[56,35,34,15,26,51,0,63]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.2710585970964664\", \"ip\":\"user_ip\"}]";
                                   break;
                               default:
                                   return;
                           }
                           break;
                       case GraphType.ALL:    //INDICA CH_E BISOGNA SCARICARE TUTTI I GRAFI
                           json_txt = "[{\"graphType\":0, \"vNbr\":5, \"gridSideSize\":5, \"crossingNbr\":1, \"gridPositions\":\"[24,17,1,11,15]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.46242073797398286\", \"ip\":\"user_ip\"},"+
                                      "{\"graphType\":0, \"vNbr\":6, \"gridSideSize\":12, \"crossingNbr\":3, \"gridPositions\":\"[12,90,56,137,34,39]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.24369076317572735\", \"ip\":\"user_ip\"},{\"graphType\":0, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":5, \"gridPositions\":\"[27,19,30,34,26,7]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.32266884975072974\", \"ip\":\"user_ip\"},"+
                                      "{\"graphType\":0, \"vNbr\":7, \"gridSideSize\":14, \"crossingNbr\":9, \"gridPositions\":\"[170,149,179,78,158,36,105]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.05339494146514623\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":13, \"gridPositions\":\"[26,45,32,27,16,17,1]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.20980421845276787\", \"ip\":\"user_ip\"},{\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":17, \"gridPositions\":\"[37,34,38,29,43,0,24]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.4941210984920711\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":14, \"gridPositions\":\"[8,25,0,44,31,6,45]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.843934017018278\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":7, \"gridSideSize\":7, \"crossingNbr\":19, \"gridPositions\":\"[40,25,30,36,1,31,5]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.016010468657375876\", \"ip\":\"user_ip\"},"+
                                      "{\"graphType\":0, \"vNbr\":8, \"gridSideSize\":18, \"crossingNbr\":19, \"gridPositions\":\"[158,135,241,139,51,2,68,194]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.29633729472610804\", \"ip\":\"user_ip\"}, {\"graphType\":0, \"vNbr\":8, \"gridSideSize\":16, \"crossingNbr\":23, \"gridPositions\":\"[71,172,92,15,251,62,18,59]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.2999735739354723\", \"ip\":\"user_ip\"},"+
                                      "{\"graphType\":1, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":1, \"gridPositions\":\"[10,13,18,14,1,26]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.9794368384236602\", \"ip\":\"user_ip\"},{\"graphType\":1, \"vNbr\":6, \"gridSideSize\":6, \"crossingNbr\":1, \"gridPositions\":\"[15,28,7,25,21,17]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.4177318303277614\", \"ip\":\"user_ip\"},"+
                                      "{\"graphType\":1, \"vNbr\":8, \"gridSideSize\":8, \"crossingNbr\":4, \"gridPositions\":\"[40,20,33,14,17,56,34,0]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.0944721155156566\", \"ip\":\"user_ip\"}, {\"graphType\":1, \"vNbr\":8, \"gridSideSize\":8, \"crossingNbr\":5, \"gridPositions\":\"[56,35,34,15,26,51,0,63]\", \"valid\":true, \"showEmail\":false, \"email\":\"user_0.2710585970964664\", \"ip\":\"user_ip\"}]";
                           break;
                       default:
                           return;

                   }

 //                   String json_txt = "[{\"graphType\":0,\"vNbr\":5,\"crossingNbr\":23,\"gridPositions\":\"[9,16,7,20,2]\",\"valid\":true,\"email\":\"ciccio@bello.com\",\"showEmail\":false,\"ip\":\"1.1.1.1\",\"id\":4},{\"graphType\":1,\"vNbr\":6,\"crossingNbr\":23,\"gridPositions\":\"[9,12,7,8,2,20]\",\"valid\":true,\"email\":\"ciccio@bello.com\",\"showEmail\":true,\"ip\":\"\",\"id\":5}]";
                    try{
                        valueObject = JSONParser.parse(json_txt);
                    }catch(Exception e){
                        SC.warn("An error has occurred while trying to retrieve the data.<br/>We apologize.<br/>Please feel free to report us the problem.");

                        return ;
                    }
               }*/
               else {
                // Handle the error.  Can get the status text from response.getStatusText()
                  try{
                    SC.warn("An error occurred while trying to retrieve data: " + response.getStatusText());
                  }catch(Exception e){
                            SC.warn("An error occurred while trying to retrieve data");
                  }finally{
                      throw new IllegalArgumentException("AJAX Request error: ");
                  }
              }
              try {
                  if ( (jsonArr=valueObject.isArray())==null ){
                          SC.warn("An error occurred while trying to retrieve data");
                          return; //Throw exception		    		  }
                  }

                  int s = jsonArr.size();
                  //alert("Dimensione array " + s);
                  if (s <= 0){
                          //alert("JSONArray empty");
//                                  GWT.log("JSONArray empty", null);
                          return; //Throw exception
                  }

                  ListGridRecord[] tmpList = new ListGridRecord[s];
                  GraphEmbedding[] tmpGraphs = new GraphEmbedding[s];

                  int vn;
                  short gt;
                  int gss;
                  //String tmpStr;
                  GraphEmbedding ge;

                  for (int i=0, j=0; i < s; i++){
                      try{
                          if ( (jsonObj = jsonArr.get(i).isObject()) == null ){
                              //alert("JSONObject["+i+"] null");
                                  //GWT.log("JSONObject["+i+"] null", null);
                              errorCounter++;
                              continue;
                          }
                            //JSONNumber tmp;
    //                      alert(jsonObj.toString());
    //                      alert( jsonObj.get("graphType").toString() );
                          vn = (int) jsonObj.get("vNbr").isNumber().doubleValue();
                          gt = (short) jsonObj.get("graphType").isNumber().doubleValue();
                          try{
                                gss = (int) jsonObj.get("gridSideSize").isNumber().doubleValue();
                          }catch(Exception e){                            
                              gss = 2*vn;
                          }
                          try{
                              valueObject = JSONParser.parse( jsonObj.get("gridPositions").isString().toString().replace("\"", "") );
                          }catch(JSONException e){
                              //tmpStr = e.getMessage();
                              errorCounter++;
                              continue;
                              //alert (e.getMessage());
                          }
                          JSONArray j_pos = valueObject.isArray();
                          if (j_pos==null){
                              //alert("Errore posizioni \n"+ tmpStr );
                              errorCounter++;
                              continue;
                          }
                          else{
                                try{
                                    ge = new GraphEmbedding(vn , gt, gss, j_pos );
                                }catch(IllegalArgumentException e){
                                    //alert(e.getMessage());
                                    errorCounter++;
                                    continue;
                                }
                                tmpGraphs[i] = ge;

           // ge.draw(null);
                          }
                           tmpList[j] = new ListGridRecord();
     //                     alert("creating");
                           tmpList[j].setAttribute("row", i );
                           tmpList[j].setAttribute("graphType", ge.getGraphTypeLabel() );
    //                       alert("setting 1 ");
                           tmpList[j].setAttribute("vNbr", vn );
     //                      alert("setting 2 ");
                           tmpList[j].setAttribute("crossingNbr", (int) jsonObj.get("crossingNbr").isNumber().doubleValue() );
     //                      alert("setting 3 ");
                           tmpList[j].setAttribute("valid", ListPanel.getIsValidLabel( jsonObj.get("valid").isBoolean().booleanValue() ) );
                           if (jsonObj.get("showEmail").isBoolean().booleanValue() == true )
                                tmpList[j].setAttribute("email", jsonObj.get("email").isString().stringValue() );
                           else
                               tmpList[j].setAttribute("email", "**** ****");
                           //tmpList[i].setAttribute("gridPositions",  );
     //                      alert("setting 4 ");
                           j++;
                      }
                      catch(Exception e){
                          //alert("Data read error");
                          errorCounter++;
                          continue;
                      }
                  }
                  //alert("ready to populate");
                  if (errorCounter!=0){
                      SC.warn("Error while retrieving data: at least one record was not properly retrieved.");
                  }
                  listPanel.populateList(tmpList, tmpGraphs);

              }
              catch (JSONException e) {
                  //alert("JSON parse exception");
                  errorCounter++;
                      //GWT.log("JSON parse exception", e);
              }
               catch (Exception e){
                   errorCounter++;
                   //alert(e.getMessage());
               }
//		    	  System.out.println( response.getText());
                  //String result[] = response.getText().parseJSON();

              //LoaderLabel.stopLoader();
            }
          });
        } catch (RequestException e) {
          // Couldn't connect to server
          SC.warn("An error has occurred while trying to connect to the server.<br/>We apologize.<br/>Please feel free to report us the problem.");
        }
    }
    
    /*

    private native void alert(String message)/*-{
		alert(message);
    }-* /;

     */

}

