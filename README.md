![](https://github.com/id-05/bitSeller/blob/master/images/logo.png)

# bitSeller - телеграмм бот гос-закупок

### Описание

- Телеграм бот, который парсит закупки с сайта гос закупок и отправляет вам информацию;
- На компьютере должен быть установлен Java-интерпретатор, можно скачать отсюда: https://www.java.com/ru/download/ 
- Телеграмм-бота, вам придется самостоятельно, с помощью BotFather, 
например вот по этой инструкции:[инструкция по созданию бота](https://1spla.ru/blog/telegram_bot_for_mikrotik/)
- В базе данных должно быть четыре таблицы, описание их структуры находястя в файлах в папке /db;
- В той же директории, где будет находится jar-файл бота, должен быть размещен файл 'sellersettings.json'
содержаший настройки подключения к базе данных MSQL, пример структуры файла
{
  "hDriver":"com.mysql.cj.jdbc.Driver",
  "hIP":"ip вашего сервера", 
  "hPort":"порт вашего сервера",
  "hBaseName":"имя вашей бд",
  "hLogin":"логин бд",
  "hPassword":"пароль бд",
  "hDialect":"org.hibernate.dialect.MySQLDialect",
  "timeUpdate":5
}

# [Скачать бота](https://github.com/id-05/bitSeller/blob/master/out/artifacts/bitSeller_jar/bitSeller.jar?raw=true)

