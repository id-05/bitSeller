import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot implements Dao {

    String Token;
    String UserName = "bitserver_bot";
    String bufName = "";
    String bufINN = "";
    String welcometext = "Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
            "я проверяю данные на наличие обновлений и отправляю вам их!";
    boolean registerStart = false;
    boolean password = false;
    boolean newKontrgagentName = false;
    boolean newKontrgagentINN = false;
    boolean bottalk =false;

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

    public synchronized void sendMsg(String chatId, String s, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error send "+e.getMessage());
        }
    }

    public synchronized void sendNews(String chatId, List<Purchase> purchaseList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Новости госзакупок:").append("\n");
        for(Purchase bufPurchase:purchaseList){
            if(!bufPurchase.getINN().equals(""))
            {
                stringBuilder.append("\n").append(getClientNameByINN(bufPurchase.getINN())).append(":").append("\n").append("\n");
            }
            stringBuilder.append("[").append(bufPurchase.getId()).append("](").
                    append("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=").
                    append(bufPurchase.getId()).append("&morphology=on&search-filter=Дате+размещения&pageNumber=1&sortDirection=false&recordsPerPage+").
                    append("=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&ca=on&pc=on&pa=on&currencyIdGeneral=-1)");

            stringBuilder.append("\n").append(bufPurchase.getDescription()).append("\n");
            stringBuilder.append("*").append(bufPurchase.getPrice()).append("*").append("\n");
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
                        if(!newKontrgagentName) {
                            BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                            sendMsg(user.getId(), "Hello, " + user.getName() + "!");
                            MainMenu(update);
                        }else{
                            if(!newKontrgagentINN) {
                                BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                                bufName = update.getMessage().getText();
                                sendMsg(user.getId(), "Проверьте название, вы указали: "+update.getMessage().getText()+"\n"+
                                        "Если всё верно, то теперь отправьте мне ИНН (не более 16 цифр)!",getBackKeybord());
                                newKontrgagentINN = true;
                            }else{
                                bufINN = update.getMessage().getText();
                                BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                ArrayList<MenuItem> menuItems = new ArrayList<>();
                                menuItems.add(new MenuItem("Подтвердить","settings","saveKontragent"));
                                menuItems.add(new MenuItem("Назад","settings","settings"));
                                List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
                                inlineKeyboardMarkup.setKeyboard(rowList);
                                sendMsg(user.getId(), "Проверьте ИНН, вы указали: "+update.getMessage().getText()+"\n"+
                                        "Если всё верно, то нажмите подтвердить!",inlineKeyboardMarkup);
                            }
                        }
                        if(bottalk){
                            List<BitSellerUsers> usersList;
                            usersList = getAllUsers();
                            for (BitSellerUsers bufUser : usersList) {
                                sendMsg(bufUser.getId(), update.getMessage().getText());
                            }
                            bottalk = false;
                        }
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
            }
            if (jsonObject.has("data")) {
                secondTeg = jsonObject.get("data").getAsString();
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

            if(firstTeg.equals("settings")) {
                if (secondTeg.equals("settings")) {
                    newKontrgagentName = false;
                    newKontrgagentINN = false;
                    SettingsMenu(update);
                }

                if (secondTeg.equals("addkontragent")) {
                    newKontrgagentName = true;
                    EditingMessage(update, "Отправьте мне название контрагента (не более 32 символов)!", getBackKeybord());
                }

                if (secondTeg.equals("saveKontragent")) {
                    if ((bufName.length() < 32) && (bufINN.length() < 16) ) {
                        saveNewClient(new BitSellerClients(bufName, bufINN));
                        EditingMessage(update, "Контрагент сохранен!", getBackKeybord());
                        newKontrgagentName = false;
                        newKontrgagentINN = false;
                    }else{
                        EditingMessage(update, "Введенные данные не корректны, не соответствуют длинне!", getBackKeybord());
                        newKontrgagentName = false;
                        newKontrgagentINN = false;
                    }
                }

                if (secondTeg.equals("deletekontragent")) {
                    SelectOneClient(update,"deletekontragent");
                }
            }

            if(firstTeg.equals("deletekontragent")) {
                List<BitSellerClients> clientList;
                clientList = getAllClients();
                for (BitSellerClients bufClient : clientList) {
                    if(bufClient.getINN().equals(secondTeg)){

                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        ArrayList<MenuItem> menuItems = new ArrayList<>();
                        menuItems.add(new MenuItem("Подтвердить","deleteKontr",bufClient.getINN()));
                        menuItems.add(new MenuItem("Назад","settings","settings"));
                        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
                        inlineKeyboardMarkup.setKeyboard(rowList);
                        EditingMessage(update, "Вы действительно хотите удалить контрагента: "+bufClient.getName()+"  ИНН: "+bufClient.getINN(), inlineKeyboardMarkup);
                    }
                }
            }

            if(firstTeg.equals("deleteKontr")){
                List<BitSellerClients> clientList;
                clientList = getAllClients();
                for (BitSellerClients bufClient : clientList) {
                    if (bufClient.getINN().equals(secondTeg)) {
                        deleteClient(bufClient);
                        EditingMessage(update, "Контрагент удалён!", getBackKeybord());
                    }
                }
            }

            if(firstTeg.equals("purchases")){
                if(secondTeg.equals("settings")) {
                    SettingsMenu(update);
                }

                if(secondTeg.equals("exit")) {
                    System.exit(0);
                }

                if(secondTeg.equals("getoneactive")) {
                    SelectOneClient(update,"getoneclient");
                }

                if(secondTeg.equals("backoneclient")) {
                    SelectOneClient(update,"getoneclient");
                }

                if(secondTeg.equals("getactive")) {
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
                        System.out.println("Возникла ошибка при парсинге!");
                    }
                }
            }

            if(firstTeg.equals("getoneclient")){
                List<BitSellerClients> clientList;
                clientList = getAllClients();
                List<Purchase> news = new ArrayList<>();
                try {
                    for (BitSellerClients bufClient : clientList) {
                        if(bufClient.getINN().equals(secondTeg)){
                            news.clear();
                            WebHeandless webParser = new WebHeandless(bufClient.getINN());
                            List<Purchase> listPurchase = webParser.getActualPurchase();
                            news.addAll(listPurchase);
                            if(news.size()>0) {
                                PurchacesOneClient(update, news);
                            }
                        }
                    }
                }catch (Exception e){
                    System.out.println("Возникла ошибка при парсинге!");
                }
            }

            if(firstTeg.equals("bottalk")){
                bottalk = true;
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                ArrayList<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem("Назад","back","main"));
                List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
                inlineKeyboardMarkup.setKeyboard(rowList);
                EditingMessage(update, "Отправьте мне текст и его увидят все пользователи!", inlineKeyboardMarkup);
            }

            if(firstTeg.equals("back")){
                if(secondTeg.equals("main")) {
                    MainMenu(update);
                }
            }
        }
    }

    public void EditingMessage(Update update, String text, InlineKeyboardMarkup keyboard){
        EditMessageText editMessage = new EditMessageText();
        editMessage.enableMarkdown(true);
        editMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessage.setText(text);
        editMessage.setReplyMarkup(keyboard);
        try {
            execute(editMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void SendingMessage(Update update, String text, InlineKeyboardMarkup keyboard){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void AdminMenu(Update update){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Выключить бота","settings","exit"));
        menuItems.add(new MenuItem("Отправить сообщение всем" ,"bottalk","bottalk"));
        menuItems.add(new MenuItem("Назад","back","main"));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendingMessage(update, "Меню администратора:", inlineKeyboardMarkup);
    }

    public InlineKeyboardMarkup getBackKeybord(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Назад","settings","settings"));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public void PurchacesOneClient(Update update,List<Purchase> news){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Назад");
        inlineKeyboardButton.setCallbackData(GetJsonForBotMenu("purchases","backoneclient"));
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        StringBuilder stringBuilder = new StringBuilder();
        Purchase bufP = news.get(0);
        if(!bufP.getINN().equals("")){
            stringBuilder.append("\n").append(getClientNameByINN(bufP.getINN())).append(":").append("\n");
        }
        for(Purchase bufPurchase:news){

            stringBuilder.append("\n").append("[").append(bufPurchase.getId()).append("](").
                    append("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=").
                    append(bufPurchase.getId()).append("&morphology=on&search-filter=Дате+размещения&pageNumber=1&sortDirection=false&recordsPerPage+").
                    append("=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&ca=on&pc=on&pa=on&currencyIdGeneral=-1)");
            stringBuilder.append("\n").append(bufPurchase.getDescription()).append("\n");
            stringBuilder.append("*").append(bufPurchase.getPrice()).append("*").append("\n");
        }

        if(update.hasCallbackQuery()){
            EditingMessage(update, stringBuilder.toString(), inlineKeyboardMarkup);
        }
    }

    public void SelectOneClient(Update update, String firsttag){
        List<BitSellerClients> listClient = getAllClients();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for(BitSellerClients bufClient:listClient){
            menuItems.add(new MenuItem(bufClient.getName(),firsttag,bufClient.getINN()));
        }
        menuItems.add(new MenuItem("Назад","back","main"));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(update.hasCallbackQuery()) {
            EditingMessage(update, "Выберите одного из клиентов:", inlineKeyboardMarkup);
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
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Получить активные закупки всех клиентов","purchases","getactive"));
        menuItems.add(new MenuItem("Получить закупки одного клиента","purchases","getoneactive"));
        menuItems.add(new MenuItem("Настройки","settings","settings"));
        if(user.isSubscription()) {
            menuItems.add(new MenuItem("Остановить подписку","subscription","stop"));
        }else{
            menuItems.add(new MenuItem("Подписаться","subscription","start"));
        }
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(update.hasCallbackQuery()){
            EditingMessage(update, welcometext, inlineKeyboardMarkup);
        }else{
            SendingMessage(update, welcometext, inlineKeyboardMarkup);
        }
    }

    public List<List<InlineKeyboardButton>> getMenuFromItemList(ArrayList<MenuItem> menuItem){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(MenuItem bufItem:menuItem) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(bufItem.getVisibleName());
            inlineKeyboardButton.setCallbackData(GetJsonForBotMenu(bufItem.getFirstTag(),bufItem.getSecondTag()));
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        }
        return rowList;
    }

    public void SettingsMenu(Update update){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Добавить Котрагента","settings","addkontragent"));
        menuItems.add(new MenuItem("Удалить Котрагента","settings","deletekontragent"));
        menuItems.add(new MenuItem("Назад","back","main"));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);//new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowList);

        if(update.hasCallbackQuery()){
            EditingMessage(update, welcometext, inlineKeyboardMarkup);
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
