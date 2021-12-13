import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot implements Dao, BotHelper {

    String Token;
    String UserName = "bitserver_bot";
    String bufName = "";
    String bufINN = "";
    String bufGroup = "";
    String welcometext = "Я бот - который парсит закупки ваших контрагентов с сайта zakupki.gov.ru. Один раз в час, " +
            "я проверяю данные на наличие обновлений и отправляю вам их!";
    boolean registerStart = false;
    boolean password = false;
    boolean newKontrgagentName = false;
    boolean newKontrgagentINN = false;
    boolean newKontrgagentGroup = false;
    boolean newGroup = false;
    boolean bottalk = false;
    int countItemInMes = 10;

    public MyTelegramBot(String Token){
        this.Token = Token;
    }

    public synchronized void sendMsg(String chatId, String s) {
        try {
            execute(prepareMsg(chatId,s));
        } catch (TelegramApiException e) {
            System.out.println("Error send 1"+e.getMessage());
        }
    }

    public synchronized void sendMsg(String chatId, String s, InlineKeyboardMarkup inlineKeyboardMarkup) {
        try {
            execute(prepareMsg(chatId,s,inlineKeyboardMarkup));
        } catch (TelegramApiException e) {
            System.out.println("Error send 2"+e.getMessage());
        }
    }

    public void editMsg(Update update, String text, InlineKeyboardMarkup keyboard){
        try {
            execute(prepareEditMsg(update,text,keyboard));
        } catch (TelegramApiException ex){
            System.out.println(ex.toString());
        }
    }

    public void sendNews(Update update, List<Purchase> purchaseList){
        String chatId = update.getCallbackQuery().getMessage().getChat().getId().toString();
        sendNews(chatId, purchaseList);
    }

    //основная процедура рассылки новостей
    public synchronized void sendNews(String chatId, List<Purchase> purchaseList) {
        if(purchaseList.size()>0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Новости госзакупок:").append("\n");
            String lastClientName = "";
            int i = 0;
            for (Purchase bufPurchase:purchaseList) {
                    if(!lastClientName.equals(getClientByINN(bufPurchase.getINN()).getName())) {
                        stringBuilder.append("\n").append(getClientByINN(bufPurchase.getINN()).getName()).append(":").append("\n");
                        lastClientName = getClientByINN(bufPurchase.getINN()).getName();
                    }else{
                        stringBuilder.append("\n");
                    }
                stringBuilder = getPurchaseToMessage(stringBuilder,bufPurchase);
                i++;
                if(i==countItemInMes){
                    sendMsg(chatId,stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    i=0;
                }
            }
            sendMsg(chatId,stringBuilder.toString());
        }
    }

    public StringBuilder getPurchaseToMessage(StringBuilder stringBuilder, Purchase bufPurchase){
        stringBuilder.append("\n").append(getClientByINN(bufPurchase.getINN()).getUGroup()).append(" - ").append(getClientByINN(bufPurchase.getINN()).getName()).append("\n");
        stringBuilder.append(bufPurchase.getDescription()).append("\n");
        stringBuilder.append("*").append(bufPurchase.getPrice()).append("*").append("\n");
        stringBuilder.append("Подача заявок до: ").append(bufPurchase.getDatebefore()).append("  ");
        stringBuilder.append("[").append("Открыть"/*bufPurchase.getId()*/).append("](").
                append("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=").
                append(bufPurchase.getId()).append("&morphology=on&search-filter=Дате+размещения&pageNumber=1&sortDirection=false&recordsPerPage+").
                append("=_100&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&ca=on&pc=on&pa=on&currencyIdGeneral=-1)").append("\n");
        return stringBuilder;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                if(validateUser(update.getMessage().getChat().getId().toString())){
                    if(update.getMessage().getText().equals("Админ")){
                        sendMsg(update.getMessage().getChatId().toString(), "Меню администратора:", getAdminMenu());
                    }else {
                        BitSellerUsers user = getUserById(update.getMessage().getChat().getId().toString());
                        if(!newKontrgagentName) {
                            if(!newGroup) {
                                if (bottalk) {
                                    for (BitSellerUsers bufUser:getAllUsers()) {
                                        if (bufUser.isSubscription()) {
                                            sendMsg(bufUser.getId(), update.getMessage().getText());
                                        }
                                    }
                                    bottalk = false;
                                } else {
                                    sendMsg(user.getId(), "Hello, " + user.getName() + "!");
                                    MainMenu(update);
                                }
                            }else{
                                //добавляем новую группу
                                if(update.getMessage().getText().length()<16) {
                                    boolean ifExist = false;
                                    for (BitSellerGroups bufgroup:getAllGroups()) {
                                        if (update.getMessage().getText().equals(bufgroup.getName())) {
                                            ifExist = true;
                                        }
                                    }
                                    if (!ifExist) {
                                        BitSellerGroups newGroup = new BitSellerGroups(update.getMessage().getText());
                                        saveNewGroup(newGroup);
                                        sendMsg(user.getId(), "Группа успешно добавлена!  " + update.getMessage().getText() + "\n", getSettingsMenu());
                                    }else{
                                        sendMsg(user.getId(), "Группа с таким именем уже существует!  " + update.getMessage().getText() + "\n", getSettingsMenu());
                                    }
                                }else{
                                    sendMsg(user.getId(), "Некорректное название группы!  " + update.getMessage().getText() + "\n", getSettingsMenu());
                                }
                                newGroup = false;
                            }
                        }else{
                            if(!newKontrgagentINN) {
                                bufName = update.getMessage().getText();
                                sendMsg(user.getId(), "Проверьте название, вы указали: "+update.getMessage().getText()+"\n"+
                                        "Если всё верно, то теперь отправьте мне ИНН (не более 16 цифр)!",getBackKeybord("settings","settings"));
                                newKontrgagentINN = true;
                            }else{
                                bufINN = update.getMessage().getText();
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                ArrayList<MenuItem> menuItems = new ArrayList<>();
                                for(BitSellerGroups bufGroup:getAllGroups()){
                                    menuItems.add(new MenuItem(bufGroup.getName(),"chooseGroup",bufGroup.getName()));
                                }
                                menuItems.add(new MenuItem("Назад","back","settings"));
                                List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
                                inlineKeyboardMarkup.setKeyboard(rowList);
                                sendMsg(user.getId(), "Проверьте ИНН, вы указали: "+update.getMessage().getText()+"\n"+
                                        "Если всё верно, то теперь выберите группу, в которую будет добавлен новый контрагент!",inlineKeyboardMarkup);
                                newKontrgagentGroup = true;
                            }
                        }
                    }
                }else{
                    if(registerStart){//start registration new user
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
            BitSellerUsers user = getUserById(update.getCallbackQuery().getMessage().getChat().getId().toString());
            String firstTeg = "";
            String secondTeg = "";
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(update.getCallbackQuery().getData()).getAsJsonObject();
            if (jsonObject.has("name")) { firstTeg = jsonObject.get("name").getAsString(); }
            if (jsonObject.has("data")) { secondTeg = jsonObject.get("data").getAsString(); }
    //bot menu start here
            switch (firstTeg){
                case "subscription":
                    switch (secondTeg) {
                        case "stop":
                            user.setSubscription(false);
                            break;
                        case "start":
                            user.setSubscription(true);
                            break;
                    }
                    saveNewUser(user);
                    MainMenu(update);
                    break;

                case "settings":
                    switch (secondTeg){
                        case "settings":
                            newKontrgagentName = false;
                            newKontrgagentINN = false;
                            SettingsMenu(update);
                            break;
                        case "addkontragent":
                            newKontrgagentName = true;
                            editMsg(update, "Отправьте мне название контрагента (не более 32 символов)!", getBackKeybord("settings","settings"));
                            break;
                        case "savekontragent":
                            newKontrgagentName = false;
                            newKontrgagentINN = false;
                            newKontrgagentGroup = false;
                            if ((bufName.length() < 32) && (bufINN.length() < 16) ) {
                                saveNewClient(new BitSellerClients(bufName, bufINN, bufGroup));
                                editMsg(update, "Контрагент сохранен!", getSettingsMenu());
                            }else{
                                editMsg(update, "Введенные данные не корректны, не соответствуют длинне!", getSettingsMenu());
                            }
                            break;
                        case "deletekontragent":
                            SelectOneGroup(update,"deleteclientfromg", "back","settings");
                            break;
                        case "addgroup":
                            newGroup = true;
                            editMsg(update, "Отправьте мне название новой группы (не более 16 символов)!", getBackKeybord("settings","settings"));
                            break;
                        case "deletegroup":
                            SelectOneGroup(update,"deletegroup","back","settings");
                            break;
                        case "filtrprice":
                            MenuForChangeFilter(update);
                            break;
                        case "exit":
                            System.exit(0);
                            break;
                        case "subscriptions":
                            SubscriptionMenu(update);
                            break;
                    }
                    break;

                case "chSub":
                    List<BitSellerGroups> allGroups = getAllGroups();
                    for(BitSellerGroups bufGroup:allGroups){
                        if(secondTeg.contains(bufGroup.getName())){
                            if(secondTeg.contains("off")){
                                BitSellerSubscriptions bufSub = new BitSellerSubscriptions(user.getId(),bufGroup.getName());
                                saveNewSubscription(bufSub);
                            }
                            if(secondTeg.contains("on")){
                                BitSellerSubscriptions bufSub = getExistSubcription(user,bufGroup.getName());
                                deleteSubscription(bufSub);
                            }
                            break;
                        }
                    }
                    SubscriptionMenu(update);
                    break;

                case "chooseGroup":
                    bufGroup = secondTeg;
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    ArrayList<MenuItem> menuItems = new ArrayList<>();
                    menuItems.add(new MenuItem("Подтвердить","settings","savekontragent"));
                    menuItems.add(new MenuItem("Назад","settings","settings"));
                    List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
                    inlineKeyboardMarkup.setKeyboard(rowList);
                    editMsg(update,"Проверьте данные? вы указали: Название: "+bufName+" ИНН: "+bufINN+" Группа: "+bufGroup+"\n Если всё верно," +
                            "то нажмите Подтвердить",inlineKeyboardMarkup);
                    break;

                case  "filtrprice":
                    int buf = user.getFilterfrice();
                    switch (secondTeg){
                        case "plus":
                            user.setFilterfrice(buf + 50000);
                            break;
                        case "minus":
                            user.setFilterfrice(buf - 50000);
                            break;
                    }
                    saveNewUser(user);
                    MenuForChangeFilter(update);
                    break;

                case "deletekontragent":
                    for (BitSellerClients bufClient : getAllClients()) {
                        if(bufClient.getINN().equals(secondTeg)){
                            inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            menuItems = new ArrayList<>();
                            menuItems.add(new MenuItem("Подтвердить","deletekontraccept",bufClient.getINN()));
                            menuItems.add(new MenuItem("Назад","settings","settings"));
                            rowList = getMenuFromItemList(menuItems);
                            inlineKeyboardMarkup.setKeyboard(rowList);
                            editMsg(update, "Вы действительно хотите удалить контрагента: "+bufClient.getName()+"  ИНН: "+bufClient.getINN(), inlineKeyboardMarkup);
                        }
                    }
                    break;

                case "deletekontraccept":
                    for (BitSellerClients bufClient : getAllClients()) {
                        if (bufClient.getINN().equals(secondTeg)) {
                            deleteClient(bufClient);
                            editMsg(update, "Контрагент удалён!", getSettingsMenu());
                        }
                    }
                    break;

                case "deletegroup":
                    boolean hasKontragent = false;
                    boolean hasSubscription = false;
                    for(BitSellerClients bufClient:getAllClients()){
                        if (bufClient.getUGroup().equals(secondTeg)) {
                            hasKontragent = true;
                            break;
                        }
                    }

                    for(BitSellerSubscriptions bufSubscription:getAllSubcriptions()){
                        if(bufSubscription.getTag().equals(secondTeg)){
                            hasSubscription = true;
                            break;
                        }
                    }

                    if(hasKontragent | hasSubscription){
                        editMsg(update, "Не возможно удалить группу! В неё уже добавлены контрагенты или есть подписанные клиенты! Сначала удалите всех контрагентов и подписки на группу!", getSettingsMenu());
                    }else {
                        deleteGroup(getGroupByName(secondTeg));
                        editMsg(update, "Группа удалена!", getSettingsMenu());
                    }
                    break;

                case "backoneclient":
                    SelectOneClient(update,"getoneclient",secondTeg, "purchases","getoneactive");
                    break;

                case "purchases":
                    switch (secondTeg){
                        case "settings":
                            SettingsMenu(update);
                            break;

                        case "getoneactive":
                            SelectOneGroup(update,"getoneclientfromg", "back", "main");
                            break;

                        case "getactive":
                            List<Purchase> news = new ArrayList<>();
                            try {
                                for (BitSellerClients bufClient:getAllClients()) {
                                    news.clear();
                                    WebHeandless webParser = new WebHeandless(bufClient.getINN(),user.getFilterfrice());
                                    List<Purchase> listPurchase = webParser.getActualPurchase();
                                    news.addAll(listPurchase);
                                    if(news.size()>0) {
                                        sendNews(update, news);
                                    }
                                }
                            }catch (Exception e){
                                System.out.println("Возникла ошибка при парсинге 1!");
                            }
                            break;
                    }
                    break;

                case "getoneclient":
                    List<Purchase> news = new ArrayList<>();
                    try {
                        for (BitSellerClients bufClient:getAllClients()) {
                            if(bufClient.getINN().equals(secondTeg)){
                                news.clear();
                                WebHeandless webParser = new WebHeandless(bufClient.getINN(),user.getFilterfrice());
                                List<Purchase> listPurchase = webParser.getActualPurchase();
                                news.addAll(listPurchase);
                                if(news.size()>0) {
                                    PurchacesOneClient(update, news);
                                }
                            }
                        }
                    }catch (Exception e){
                        System.out.println("Возникла ошибка при парсинге 2!"+e.getMessage());
                    }
                    break;

                case "getoneclientfromg":
                    SelectOneClient(update,"getoneclient",secondTeg, "purchases","getoneactive");
                    break;

                case "deleteclientfromg":
                    SelectOneClient(update,"deletekontragent",secondTeg, "back","settings");
                    break;

                case "bottalk":
                    bottalk = true;
                    inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    menuItems = new ArrayList<>();
                    menuItems.add(new MenuItem("Назад","back","main"));
                    rowList = getMenuFromItemList(menuItems);
                    inlineKeyboardMarkup.setKeyboard(rowList);
                    editMsg(update, "Отправьте мне текст и его увидят все пользователи!", inlineKeyboardMarkup);
                    break;

                case "back":
                    switch (secondTeg) {
                        case "main":
                            MainMenu(update);
                            break;
                        case "settings":
                            SettingsMenu(update);
                            break;
                    }
                    break;
            }
        }
    }

    public void SubscriptionMenu(Update update){
        BitSellerUsers user = getUserById(update.getCallbackQuery().getMessage().getChat().getId().toString());
        List<BitSellerGroups> allGroups = getAllGroups();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for(BitSellerGroups bufGroup:allGroups){
            String status = "";
            if(ifExistSubscription(user,bufGroup.getName())){
                status = "on";
            }else{
                status = "off";
            }
            menuItems.add(new MenuItem(bufGroup.getName() + " : "+status,"chSub",bufGroup.getName() + ":"+status));
        }
        menuItems.add(new MenuItem("Назад","back","settings"));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);
        if(update.hasCallbackQuery()) {
            editMsg(update, "Ваши текущие подписки, отмечены статусом 'on', для изменения статуса, кликните по соответствующей кнопке.", inlineKeyboardMarkup);
        }
    }

    public void MenuForChangeFilter(Update update){
        BitSellerUsers user = getUserById(update.getCallbackQuery().getMessage().getChat().getId().toString());
        editMsg(update, "Выберите ваш порог фильтрации, закупки ниже этой суммы, не будут попадать в ваши новости! " +
                "Ваш текущий порог фильтрации: "+user.getFilterfrice(), getChangeFilterMenu());
    }

    public void PurchacesOneClient(Update update,List<Purchase> news){
        StringBuilder stringBuilder = new StringBuilder();
        Purchase bufP = news.get(0);
        if(!bufP.getINN().equals("")){
            stringBuilder.append("\n").append(getClientByINN(bufP.getINN()).getName()).append(":").append("\n");
        }
        int i = 0;
        for(Purchase bufPurchase:news){
            stringBuilder = getPurchaseToMessage(stringBuilder,bufPurchase);
            i++;
            if(i==countItemInMes){
                sendMsg(update.getCallbackQuery().getMessage().getChatId().toString(),stringBuilder.toString());
                stringBuilder = new StringBuilder();
                i=0;
            }
        }

        if(update.hasCallbackQuery()){
            //editMsg(update, stringBuilder.toString(), getBackKeybord("purchases","backoneclient"));
            editMsg(update, stringBuilder.toString(), getBackKeybord("backoneclient",getClientByINN(bufP.getINN()).getUGroup()));
        }
    }

    public void SelectOneClient(Update update, String firsttag, String groupname, String backOneTag, String backTwoTag){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for(BitSellerClients bufClient:getAllClientsFromGroup(groupname)){
            menuItems.add(new MenuItem(bufClient.getName(),firsttag,bufClient.getINN()));
        }
        menuItems.add(new MenuItem("Назад",backOneTag,backTwoTag));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);
        if(update.hasCallbackQuery()) {
            editMsg(update, "Выберите одного из клиентов:", inlineKeyboardMarkup);
        }
    }

    public void SelectOneGroup(Update update, String firsttag, String backOneTag, String backTwoTag){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for(BitSellerGroups bufGroup:getAllGroups()){
            menuItems.add(new MenuItem(bufGroup.getName(),firsttag,bufGroup.getName()));
        }
        menuItems.add(new MenuItem("Назад",backOneTag,backTwoTag));
        List<List<InlineKeyboardButton>> rowList = getMenuFromItemList(menuItems);
        inlineKeyboardMarkup.setKeyboard(rowList);
        if(update.hasCallbackQuery()) {
            editMsg(update, "Выберите группу для удаления:", inlineKeyboardMarkup);
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
            editMsg(update, welcometext, inlineKeyboardMarkup);
        }else{
            sendMsg(update.getMessage().getChatId().toString(), welcometext, inlineKeyboardMarkup);
        }
    }

    public void SettingsMenu(Update update){
        if(update.hasCallbackQuery()){
            editMsg(update, welcometext, getSettingsMenu());
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

}
