import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileReader;
import java.io.IOException;

public class Main implements Dao{
    public static MyTelegramBot bot = null;
    public static String BotToken;
    public static JsonSettings  hibernateSettings;
    public static StringBuilder stringBuilder = new StringBuilder();

    public Main() throws IOException {
        init();
        botInit();
        System.out.println("Main()");
    }

    public static void botInit(){
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        bot = new MyTelegramBot(BotToken);

        try {
            assert botsApi != null;
            botsApi.registerBot(bot);
        } catch (TelegramApiException telegramApiRequestException) {
            telegramApiRequestException.printStackTrace();
        }

    }

    public void init(){
        try(FileReader reader = new FileReader("sellersettings.json"))
        {
            int c;
            while((c=reader.read())!=-1){
                stringBuilder.append((char)c);
            }

            if(stringBuilder != null){
                //System.out.println(stringBuilder);
                hibernateSettings = new JsonSettings(stringBuilder);
            }
            BotToken = getBitSellerResource("telegramtoken");
            System.out.println("this place + "+getBitSellerResource("telegramtoken"));

        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        while(true) {

        }
    }
}
