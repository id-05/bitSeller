import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class MainTask extends TimerTask implements Dao {
    @Override
    public void run() {
        System.out.println("timer start "+new Date());

        List<BitSellerClients> clientList ;
        List<Purchase> news = new ArrayList<>();
        clientList = getAllClients();
        for(BitSellerClients bufClient:clientList){

            try {
                WebHeandless webParser = new WebHeandless(bufClient.getINN());
                List<Purchase> listPurchase = webParser.getActualPurchase();

                for(Purchase bufPurchase:listPurchase){
                    if(!ifExistPurchase(bufPurchase.id)){
                        BitSellerPurchase buf = new BitSellerPurchase(bufPurchase.getId(),bufClient.getINN());
                        saveNewPurchase(buf);
                        news.add(bufPurchase);
                    }
                }

            } catch (IOException e) {

               System.out.println("Ошибка во время опроса по ИНН");
            }
        }


        if(news.size()>0) {
            ////////////////////////////////////////////////////////////
            List<BitSellerUsers> usersList;// = new ArrayList<>();
            usersList = getAllUsers();
            List<Purchase> bufNews = new ArrayList<>();
            for (BitSellerUsers bufUser : usersList) {
                if(bufUser.isSubscription()) {
                    for(Purchase bufPurchase:news){
                        int indZ = bufPurchase.getPrice().indexOf(",");
                        String bufStr = "0";
                        if(indZ>0){
                            bufStr = bufPurchase.getPrice().substring(0,indZ);
                        }
                        if( Integer.parseInt(bufStr.replaceAll("[^0-9]","")) > bufUser.getFilterfrice() ){
                            bufNews.add(bufPurchase);
                        }
                    }
                    Main.bot.sendNews(bufUser.getId(), bufNews);
                    bufNews.clear();
                }
            }
            ////////////////////////////////////////////////////////////
        }

    }
}
