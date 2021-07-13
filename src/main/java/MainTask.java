import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainTask extends TimerTask implements Dao {
    @Override
    public void run() {
        System.out.println("_____________________");
        //сначала "опрашиваем" всех клиентов на предмет наличия новых закупок
        List<BitSellerClients> clientList = new ArrayList<>();
        clientList = getAllClients();
        for(BitSellerClients bufClient:clientList){
            //проходим по всем клиентам
            try {
                WebHeandless webParser = new WebHeandless(bufClient.getINN());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
