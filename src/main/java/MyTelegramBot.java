import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot implements Dao {

    String Token;
    String UserName = "bitserver_bot";
    boolean registerStart = false;
    boolean password = false;

    public MyTelegramBot(String Token){
        this.Token = Token;
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error send "+e.getMessage());
        }
    }

    public synchronized void sendNews(String chatId, List<Purchase> purchaseList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Новости госзакупок:");
        stringBuilder.append("\n");
        String bufINN ="";
        for(Purchase bufPurchase:purchaseList){
            if(bufINN.equals(bufPurchase.getINN())){

            }else{
                bufINN = bufPurchase.getINN();
                stringBuilder.append("\n");
                stringBuilder.append(getClientNameByINN(bufPurchase.getINN())).append(":");
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");

            stringBuilder.append("[").append(bufPurchase.getId()).append("](").
                    append("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=").
                    append(bufPurchase.getId()).append("&morphology=on&search-filter=Дате+размещения&pageNumber=1&sortDirection=false&recordsPerPage+").
                    append("=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&ca=on&pc=on&pa=on&currencyIdGeneral=-1)");

            stringBuilder.append("\n");
            stringBuilder.append(bufPurchase.getDescription());
            stringBuilder.append("\n");
            stringBuilder.append("*").append(bufPurchase.getPrice()).append("*");
            stringBuilder.append("\n");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(stringBuilder.toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error send "+e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                if(validateUser(update.getMessage().getChat().getId().toString())){
                    if(update.getMessage().getText().equals("Админ")){
                        AdminMenu(update);
                    }else {
                        BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                        sendMsg(user.getId(), "Hello, " + user.getName() + "!");
                        MainMenu(update);
                    }
                }else{
                    if(registerStart){
                        if(password){
                            BitSellerUsers user = new BitSellerUsers(update.getMessage().getChat().getId().toString(),update.getMessage().getText());
                            try{
                                saveNewUser(user);
                                password = false;
                                registerStart = false;
                                sendMsg(user.getId(), "Hello, "+user.getName()+"!");
                            }catch (Exception e){
                                sendMsg(user.getId(), "No valid name! Try again!");
                            }
                        }else{
                            String bufStr = update.getMessage().getText().toUpperCase().replaceAll("[\\s|\\u00A0]+", "");
                            if(bufStr.equals("NO!WEAREBITSERVICE!")) {
                                sendMsg(update.getMessage().getChat().getId().toString(), "Ok! Enter you name:");
                                password = true;
                            }else{
                                sendMsg(update.getMessage().getChat().getId().toString(), "Are you gangsters?");
                            }
                        }
                    }else {
                        registerStart = true;
                        sendMsg(update.getMessage().getChat().getId().toString(), "Are you gangsters?");
                    }
                }
            }
        }else if(update.hasCallbackQuery()){
            String firstTeg = "";
            String secondTeg = "";
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(update.getCallbackQuery().getData()).getAsJsonObject();
            if (jsonObject.has("name")) {
                firstTeg = jsonObject.get("name").getAsString();
                System.out.println("firstTeg :"+firstTeg);
            }
            if (jsonObject.has("data")) {
                secondTeg = jsonObject.get("data").getAsString();
                System.out.println("secondTeg :"+secondTeg);
            }

            if(firstTeg.equals("subscription")){
                if(secondTeg.equals("stop")) {
                    BitSellerUsers user = getUserById(update.getCallbackQuery().getMessage().getChat().getId().toString());
                    user.setSubscription(false);
                    saveNewUser(user);
                    MainMenu(update);
                }
                if(secondTeg.equals("start")){
                    BitSellerUsers user = getUserById(update.getCallbackQuery().getMessage().getChat().getId().toString());
                    user.setSubscription(true);
                    saveNewUser(user);
                    MainMenu(update);
                }
            }

            if(firstTeg.equals("settings")){
                if(secondTeg.equals("settings")) {
                   System.out.println("настройки");

                }

                if(secondTeg.equals("exit")) {
                    System.exit(0);
                }

                if(secondTeg.equals("getactive")) {
                    System.out.println("активные закупки");
                    List<BitSellerClients> clientList;
                    clientList = getAllClients();
                    List<Purchase> news = new ArrayList<>();
                    try {
                        for (BitSellerClients bufClient : clientList) {
                            news.clear();
                            WebHeandless webParser = new WebHeandless(bufClient.getINN());
                            List<Purchase> listPurchase = webParser.getActualPurchase();
                            news.addAll(listPurchase);
                            if(news.size()>0) {
                                sendNews(update.getCallbackQuery().getMessage().getChat().getId().toString(), news);
                            }
                        }


                    }catch (Exception e){
                        System.out.println("Возникла ошибка при парсинге");
                    }
                }

            }

            if(firstTeg.equals("back")){
                if(secondTeg.equals("main")) {
                    MainMenu(update);
                }
            }

        }
    }

    public void AdminMenu(Update update){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Выключить бота");
        inlineKeyboardButton1.setCallbackData(GetJsonForBotMenu("settings","exit"));
        inlineKeyboardButton2.setText("Пусто");
        inlineKeyboardButton2.setCallbackData(GetJsonForBotMenu("empty","empty"));
        inlineKeyboardButton3.setText("В главное меню");
        inlineKeyboardButton3.setCallbackData(GetJsonForBotMenu("back", "main"));
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow3.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
        sendMessage.setText("Меню администратора:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void MainMenu(Update update){
        BitSellerUsers user;
        if(update.hasCallbackQuery()){
            user = getUserById(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }else{
            user = getUserById(String.valueOf(update.getMessage().getChat().getId()));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Получить активные закупки всех клиентов");
        inlineKeyboardButton1.setCallbackData(GetJsonForBotMenu("settings","getactive"));
        inlineKeyboardButton2.setText("Настройки");
        inlineKeyboardButton2.setCallbackData(GetJsonForBotMenu("settings","settings"));
        if(user.isSubscription()) {
            inlineKeyboardButton3.setText("Остановить подписку");
            inlineKeyboardButton3.setCallbackData(GetJsonForBotMenu("subscription", "stop"));
        }else{
            inlineKeyboardButton3.setText("Подписаться");
            inlineKeyboardButton3.setCallbackData(GetJsonForBotMenu("subscription", "start"));
        }
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow3.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(update.hasCallbackQuery()){
            EditMessageText editMessage = new EditMessageText();
            editMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            editMessage.setText("Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
                    "я проверяю данные на наличие обновлений и отправляю вам их");
            editMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(editMessage);
            } catch (TelegramApiException ex){
                System.out.println(ex.toString());
            }
        }else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
            sendMessage.setText("Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
                    "я проверяю данные на наличие обновлений и отправляю вам их");
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex){
                System.out.println(ex.toString());
            }
        }
    }

    public void SettingsMenu(Update update){
        BitSellerUsers user;
        if(update.hasCallbackQuery()){
            user = getUserById(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }else{
            user = getUserById(String.valueOf(update.getMessage().getChat().getId()));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Добавить Котрагента");
        inlineKeyboardButton1.setCallbackData(GetJsonForBotMenu("settings","addkontragent"));
        inlineKeyboardButton2.setText("Список контрагентов");
        inlineKeyboardButton2.setCallbackData(GetJsonForBotMenu("settings","listkontragent"));
        inlineKeyboardButton3.setText("Назад");
        inlineKeyboardButton3.setCallbackData(GetJsonForBotMenu("settings", "back"));
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow3.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(update.hasCallbackQuery()){
            EditMessageText editMessage = new EditMessageText();
            editMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            editMessage.setText("Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
                    "я проверяю данные на наличие обновлений и отправляю вам их");
            editMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(editMessage);
            } catch (TelegramApiException ex){
                System.out.println(ex.toString());
            }
        }else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
            sendMessage.setText("Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
                    "я проверяю данные на наличие обновлений и отправляю вам их");
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException ex){
                System.out.println(ex.toString());
            }
        }
    }


    @Override
    public String getBotUsername() {
        return UserName;
    }

    @Override
    public String getBotToken() {
        return Token;
    }

    public static String GetJsonForBotMenu(String Name, String Data) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", Name);
        jsonObject.addProperty("data", Data);
        return jsonObject.toString();
    }
}
