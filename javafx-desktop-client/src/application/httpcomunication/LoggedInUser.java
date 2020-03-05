package application.httpcomunication;

public class LoggedInUser
{
    private static String id;
    private static String token;

    public static void setId(String idf) {
        id = idf;
    }

    public static void setToken(String tokenf) {
        token = tokenf;
    }

    public static String getId() {
        return id;
    }

    public static String getToken() {
        return token;
    }

    public static void logout()
    {
        id = null;
        token = null;
    }
}
