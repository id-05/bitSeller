import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonSettings {
    private String hDriver;
    private String hIP;
    private String hPort;
    private String hBaseName;
    private String hLogin;
    private String hPassword;
    private String hDialect;

    public String gethDriver() {
        return hDriver;
    }

    public void sethDriver(String hDriver) {
        this.hDriver = hDriver;
    }

    public String gethIP() {
        return hIP;
    }

    public void sethIP(String hIP) {
        this.hIP = hIP;
    }

    public String gethPort() {
        return hPort;
    }

    public void sethPort(String hPort) {
        this.hPort = hPort;
    }

    public String gethBaseName() {
        return hBaseName;
    }

    public void sethBaseName(String hBaseName) {
        this.hBaseName = hBaseName;
    }

    public String gethLogin() {
        return hLogin;
    }

    public void sethLogin(String hLogin) {
        this.hLogin = hLogin;
    }

    public String gethPassword() {
        return hPassword;
    }

    public void sethPassword(String hPassword) {
        this.hPassword = hPassword;
    }

    public String gethDialect() {
        return hDialect;
    }

    public void sethDialect(String hDialect) {
        this.hDialect = hDialect;
    }

    JsonSettings(StringBuilder data) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = parser.parse(String.valueOf(data)).getAsJsonObject();
        } catch (
                Exception e) {
            System.out.println("Ошибка парсинга = " + e.getMessage());
        }
        if (jsonObject.has("hDriver")) hDriver = jsonObject.get("hDriver").getAsString();
        if (jsonObject.has("hIP")) hIP = jsonObject.get("hIP").getAsString();
        if (jsonObject.has("hPort")) hPort = jsonObject.get("hPort").getAsString();
        if (jsonObject.has("hBaseName")) hBaseName = jsonObject.get("hBaseName").getAsString();
        if (jsonObject.has("hLogin")) hLogin = jsonObject.get("hLogin").getAsString();
        if (jsonObject.has("hPassword")) hPassword = jsonObject.get("hPassword").getAsString();
        if (jsonObject.has("hDialect")) hDialect = jsonObject.get("hDialect").getAsString();
    }
}
