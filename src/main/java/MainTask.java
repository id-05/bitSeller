import java.io.IOException;
import java.util.*;
import DAO.*;

public class MainTask extends TimerTask implements Dao {
    @Override
    public void run() {
        System.out.println("timer start "+new Date());
        List<BitSellerClients> clientList = new ArrayList<>();
        //узнаем все группы, на которые есть подписки, что бы не опрашивать лишние
        Set<BitSellerSubscriptions> uniqueSubscriptions = new HashSet<>(getAllSubcriptions());
        for(BitSellerSubscriptions bufSubscription:uniqueSubscriptions){
            clientList.addAll(getAllClientsFromGroup(bufSubscription.getTag()));
        }
        List<Purchase> news = new ArrayList<>();
        for(BitSellerClients bufClient:clientList){
            try {
                WebHeandless webParser = new WebHeandless(bufClient.getINN());
                List<Purchase> listPurchase = webParser.getActualPurchase();
                for(Purchase bufPurchase:listPurchase){
                    if(!ifExistPurchase(bufPurchase.getId())){
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
            List<Purchase> bufNews = new ArrayList<>();
            for (BitSellerUsers bufUser : getAllUsers()) {
                if(bufUser.isSubscription()) {
                    for(Purchase bufPurchase:news){
                        if(ifExistSubscription(bufUser,getClientByINN(bufPurchase.getINN()).getUGroup())) {
                            boolean filterDetect = false;
                            for(BitSellerFilterWord bufFilter:getAllUserFilterWords(bufUser)){
                                if(bufPurchase.getDescription().contains(bufFilter.getWord())){
                                    filterDetect = true;
                                    break;
                                }
                            }
                            if(!filterDetect) {
                                int indZ = bufPurchase.getPrice().indexOf(",");
                                String bufStr = "0";
                                if (indZ > 0) {
                                    bufStr = bufPurchase.getPrice().substring(0, indZ);
                                }
                                if (Integer.parseInt(bufStr.replaceAll("[^0-9]", "")) > bufUser.getFilterfrice()) {
                                    bufNews.add(bufPurchase);
                                }
                            }
                        }
                    }
                    Main.bot.sendNews(bufUser.getId(), bufNews);
                    bufNews.clear();
                }
            }
        }

    }
}
