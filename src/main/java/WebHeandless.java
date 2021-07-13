import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.gson.JsonArray;
import org.apache.commons.logging.LogFactory;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class WebHeandless {

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

        List<HtmlElement> elements = page.getTabbableElements();
        System.out.println("f.s.= "+elements.size());
//        for(HtmlElement buf:elements){
//                System.out.println(buf.toString());
//        }

        List<?> divs = page.getByXPath("//div");
        for(Object obj:divs){
            HtmlDivision buf = (HtmlDivision) obj;
            System.out.println((buf).asText());
        }

    }
}
