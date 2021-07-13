import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.gson.JsonArray;
import org.apache.commons.logging.LogFactory;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WebHeandless {
    int coutnActivePurchase = 0;
    int currentDivNumber = 0;

    public WebHeandless(String INN) throws IOException {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        final WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        final HtmlPage page = webClient.getPage("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=" +
                INN+"&morphology=on&search-filter=Дате+размещения&pageNumber=1&sortDirection=false&" +
                "recordsPerPage=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&" +
                "fz44=on&fz223=on&fz94=on&af=on&currencyIdGeneral=-1");

        List<?> divs = page.getByXPath("//div");
//        for(Object obj:divs){
//            HtmlDivision buf = (HtmlDivision) obj;
//            System.out.println("__________________________________");
//            System.out.println((buf).asText());
//            System.out.println("__________________________________");
//        }

        String[] bufStrings = null;
        for(int i =0; i<divs.size();i++){
            if( ((HtmlDivision)divs.get(i)).asText().contains("Результаты поиска")){
                bufStrings = ((HtmlDivision)divs.get(i+1)).asText().split("\n");
            break;
            }
        }

//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        assert bufStrings != null;
//        for(String buf:bufStrings){
//            System.out.println(buf);
//        }
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for(int i =0; i<bufStrings.length; i++){
            //System.out.println(bufStrings[i]);
            String buf = bufStrings[i];
            if(buf.contains("Результаты поиска")){
                //System.out.println("yes");
                String bufStr = bufStrings[i+1];
                int j = bufStr.indexOf(" ");
                coutnActivePurchase = Integer.parseInt(bufStr.substring(0,j));
                currentDivNumber = i+1;
                break;
            }
        }

        System.out.println("count "+coutnActivePurchase);

        currentDivNumber = 0;
        List<Purchase> listPurchase = new ArrayList<>();
        for(int i =0; i<coutnActivePurchase; i++){
            for(int k = currentDivNumber; k < bufStrings.length; k++){
                String bufStr = bufStrings[k];
//                System.out.println("bufstr = "+bufStr);
//                System.out.println("bufStr.charAt(0) = "+bufStr.charAt(0));
                if(bufStr.charAt(0) == '№'){
                    Purchase bufPurchase = new Purchase(bufStr.substring(2,bufStr.length()-1),
                            bufStrings[k+3], bufStrings[k+7]);
                    listPurchase.add(bufPurchase);
                    currentDivNumber = k+7;
                    break;
                }
            }
        }

        for(Purchase bufP:listPurchase){
            System.out.println("____________________________");
            System.out.println("id = "+bufP.getId());
            System.out.println("description = "+bufP.getDescription());
            System.out.println("price = "+bufP.getPrice());
        }

    }
}
