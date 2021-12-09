import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

public class Main implements Dao {
    public static MyTelegramBot bot = null;
    public String BotToken;
    public static JsonSettings hibernateSettings;
    public static StringBuilder stringBuilder = new StringBuilder();
    private static final Logger log = Logger.getLogger(Main.class);
    public static TimerTask timerTask;
    public static Timer timer;

    public Main() throws IOException {
        init();
        botInit();
        timerStart();
        log.info("Программа запущена!");
    }

    public void botInit(){
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);

            bot = new MyTelegramBot(BotToken);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
                hibernateSettings = new JsonSettings(stringBuilder);
            }
            BotToken = getBitSellerResource("telegramtoken");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void timerStart(){
        timerTask = null;
        timer = null;
        timerTask = new MainTask();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 5*60*1000);
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        while(true) {

        }
    }
}
