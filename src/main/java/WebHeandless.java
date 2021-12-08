import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class WebHeandless {
    int coutnActivePurchase = 0;
    int currentDivNumber = 0;
    List<Purchase> listPurchase = new ArrayList<>();
    String INN;
    int filterPrice = 0;

    public WebHeandless(String INN) throws IOException {
        this.INN = INN;
    }

    public WebHeandless(String INN, int filterPrice) throws IOException {
        this.INN = INN;
        this.filterPrice = filterPrice;
    }

    public List<Purchase> getActualPurchase() throws IOException {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        final WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        final HtmlPage page = webClient.getPage("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=" +
                INN+"&morphology=on&search-filter=+Дате+размещения&pageNumber=1&sortDirection=false&" +
                "recordsPerPage=_100&showLotsInfoHidden=false&sortBy=UPDATE_DATE&" +
                "fz44=on&fz223=on&fz94=on&af=on&currencyIdGeneral=-1");

        List<?> divs = page.getByXPath("//div");

        String[] bufStrings = null;
        for(int i =0; i<divs.size();i++){
            if( ((HtmlDivision)divs.get(i)).asText().contains("Результаты поиска")){
                bufStrings = ((HtmlDivision)divs.get(i+1)).asText().split("\n");
            break;
            }
        }

        for(int i = 0; i< Objects.requireNonNull(bufStrings).length; i++){
            String buf = bufStrings[i];
            if(buf.contains("Результаты поиска")){
                String bufStr = bufStrings[i+1];
                int j = bufStr.indexOf(" ");
                coutnActivePurchase = Integer.parseInt(bufStr.substring(0,j));
                //currentDivNumber = i+1;
                break;
            }
        }

        currentDivNumber = 0;
        for(int i =0; i<coutnActivePurchase; i++){
            for(int k = currentDivNumber; k < bufStrings.length; k++){
                String bufStr = bufStrings[k];
                if(bufStr.charAt(0) == '№'){
                    String buf = bufStrings[k+7];
                    int indZ = buf.indexOf(",");
                    String bufStrPrice = "0";
                    if(indZ>0){
                        bufStrPrice = buf.substring(0,indZ);
                    }
                    if( Integer.parseInt(bufStrPrice.replaceAll("[^0-9]","")) > filterPrice ){
                        Purchase bufPurchase = new Purchase(INN,bufStr.substring(2,bufStr.length()-1),
                                bufStrings[k+3], bufStrings[k+7]);
                        listPurchase.add(bufPurchase);
                    }

//                    Purchase bufPurchase = new Purchase(INN,bufStr.substring(2,bufStr.length()-1),
//                            bufStrings[k+3], bufStrings[k+7]);
//                    listPurchase.add(bufPurchase);
                    currentDivNumber = k+7;
                    break;
                }
            }
        }

        return listPurchase;
    }
}
