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
            stringBuilder.append(bufPurchase.getId());
            stringBuilder.append("\n");
            stringBuilder.append(bufPurchase.getDescription());
            stringBuilder.append("\n");
            stringBuilder.append(bufPurchase.getPrice());
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
                    //сообщение зарегистрированному ползователю
                    BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                    sendMsg(user.getId(), "Hello, "+user.getName()+"!");
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChat().getId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
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
                //System.out.println(update.getMessage().getChat().getId() +"   / "+ update.getMessage().getText());
//                if(update.getMessage().getText().equals("/start")){
//                    try {
//                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                }
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
                    System.out.println("stop subscription for "+update.getCallbackQuery().getId());
                    BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                    System.out.println(user.getId());
                    user.setSubscription(false);
                    saveNewUser(user);
                    //SettingsMenu(update);
                }
                if(secondTeg.equals("start")){
                    System.out.println("start subscription for "+update.getCallbackQuery().getId());
                    BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                    System.out.println(user.getId());
                    user.setSubscription(true);
                    saveNewUser(user);
                }

            }

            if(firstTeg.equals("functions")){
                if(secondTeg.equals("functions")) {
                    SettingsMenu(update);
                }

            }

//            if(firstTeg.equals("exit")){
//                if(secondTeg.equals("exit")) {
//                    System.exit(0);
//                }
//
//            }

            if(firstTeg.equals("back")){
                if(secondTeg.equals("main")){
                    MainMenu(update);
                }
                if(secondTeg.equals("list")){
                    ListMenu(update);
                }
                if(secondTeg.equals("settings")){
                    SettingsMenu(update);
                }
            }
        }
    }

    public void CustomEditMessage(Update update, String text){
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessage.setText(text);
        try {
            execute(editMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void ChangeParamMenu(Update update, String texttomes, String NameParam){
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
//        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
//        inlineKeyboardButton1.setText("-");
//        inlineKeyboardButton1.setCallbackData(JsonEdit.GetJsonForBotMenu("settings",NameParam+"-"));
//        inlineKeyboardButton2.setText("+");
//        inlineKeyboardButton2.setCallbackData(JsonEdit.GetJsonForBotMenu("settings",NameParam+"+"));
//        keyboardButtonsRow.add(inlineKeyboardButton1);
//        keyboardButtonsRow.add(inlineKeyboardButton2);
//        rowList.add(keyboardButtonsRow);
//        rowList.add(SetOneRowButton("Back",JsonEdit.GetJsonForBotMenu("back","settings")));
//        inlineKeyboardMarkup.setKeyboard(rowList);
//        EditMessageText editMessage = new EditMessageText();
//        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
//        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        editMessage.setText(texttomes);
//        editMessage.setReplyMarkup(inlineKeyboardMarkup);
//        try {
//            execute(editMessage);
//        } catch (TelegramApiException ex){
//            System.out.println(ex.toString());
//        }
    }

    public void DeviceMenu(Update update, String secondTeg, String info){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        rowList.add(SetOneRowButton("Short info",JsonEdit.GetJsonForBotMenu("short",secondTeg)));
//        rowList.add(SetOneRowButton("Full info",JsonEdit.GetJsonForBotMenu("full",secondTeg)));
//        rowList.add(SetOneRowButton("Error info",JsonEdit.GetJsonForBotMenu("error",secondTeg)));
//        rowList.add(SetOneRowButton("Back",JsonEdit.GetJsonForBotMenu("back","list")));
        inlineKeyboardMarkup.setKeyboard(rowList);

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        //  editMessage.setText(MainForm.MagMonList.get(Integer.parseInt(secondTeg)).getName()+" \n"+info);
        editMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(editMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public List<InlineKeyboardButton> SetOneRowButton(String NameButton, String Data){
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        inlineKeyboardButton.setText(NameButton);
        inlineKeyboardButton.setCallbackData(Data);
        keyboardButtonsRow.add(inlineKeyboardButton);
        return keyboardButtonsRow;
    }

    public void SettingsMenu(Update update){
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        rowList.add(SetOneRowButton("Time to update: "+MainForm.timeToMagMonUpdate,JsonEdit.GetJsonForBotMenu("settings","time")));
//        rowList.add(SetOneRowButton("Web Port: "+MainForm.WebPort,JsonEdit.GetJsonForBotMenu("settings","webport")));
//        String buf ="";
//        if(MainForm.BotMode){
//            buf = "Send Full Log";
//        }else{
//            buf = "Send Only Event";
//        }
//        rowList.add(SetOneRowButton("Bot mode: "+buf,JsonEdit.GetJsonForBotMenu("settings","botmode")));
//        rowList.add(SetOneRowButton("Reload Settings",JsonEdit.GetJsonForBotMenu("settings","reload")));
//        rowList.add(SetOneRowButton("Back",JsonEdit.GetJsonForBotMenu("back","main")));
//        inlineKeyboardMarkup.setKeyboard(rowList);
//
//        EditMessageText editMessage = new EditMessageText();
//        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
//        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        editMessage.setText("Settings MagMon:");
//        editMessage.setReplyMarkup(inlineKeyboardMarkup);
//        try {
//            execute(editMessage);
//        } catch (TelegramApiException ex){
//            System.out.println(ex.toString());
//        }
    }

    public void MainMenu(Update update){
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessage.setText("Bot functions:");
        editMessage.setReplyMarkup(StartMenuInlineKeyboardMarkup(update.getMessage().getChat().getId()));
        try {
            execute(editMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void ListMenu(Update update){
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//
//        for(int i=0; i<=MainForm.MagMonList.size()-1;i++) {
//            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//            inlineKeyboardButton.setText(MainForm.MagMonList.get(i).getName());
//            inlineKeyboardButton.setCallbackData(JsonEdit.GetJsonForBotMenu("device", String.valueOf(i)));
//            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//            keyboardButtonsRow.add(inlineKeyboardButton);
//            rowList.add(keyboardButtonsRow);
//        }
//
//        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//        inlineKeyboardButton.setText("Back");
//        inlineKeyboardButton.setCallbackData(JsonEdit.GetJsonForBotMenu("back","main"));
//        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
//        keyboardButtonsRow.add(inlineKeyboardButton);
//        rowList.add(keyboardButtonsRow);
//        inlineKeyboardMarkup.setKeyboard(rowList);
//
//        EditMessageText editMessage = new EditMessageText();
//        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
//        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        editMessage.setText("List of devices:");
//        editMessage.setReplyMarkup(inlineKeyboardMarkup);
//        try {
//            execute(editMessage);
//        } catch (TelegramApiException ex){
//            System.out.println(ex.toString());
//        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("hello"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("help"));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup StartMenuInlineKeyboardMarkup(Long chatId){
        BitSellerUsers user = getUserById(chatId.toString());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("News");
        inlineKeyboardButton1.setCallbackData(GetJsonForBotMenu("functions","functions"));
        inlineKeyboardButton2.setText("Settings");
        inlineKeyboardButton2.setCallbackData(GetJsonForBotMenu("settings","settings"));

        if(user.isSubscription()) {
            inlineKeyboardButton3.setText("Stop my subscription");
            inlineKeyboardButton3.setCallbackData(GetJsonForBotMenu("subscriptionp", "stop"));
        }else{
            inlineKeyboardButton3.setText("Start my subscription");
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
        return inlineKeyboardMarkup;
    }

    public SendMessage sendInlineKeyBoardMessage(Long chatId) {
        String hostname = "";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("bitSeller Bot, running on "+hostname);
        sendMessage.setReplyMarkup(StartMenuInlineKeyboardMarkup(chatId));
        return sendMessage;
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
        String buf = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", Name);
        jsonObject.addProperty("data", Data);
        return jsonObject.toString();
    }
}
