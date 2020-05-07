package com.example.bpandroid3.data;

public class LoggedInUser
{
    private static String id;
    private static String token;
    private static String email;
    private static String role;

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

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        LoggedInUser.email = email;
    }

    /*public static void logout()
    {
        HttpClientClass ht = new HttpClientClass();
        try {
            ht.sendPost("auth/logout", LoggedInUser.getToken(), LoggedInUser.getId());
        }catch (IOException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());

        } catch (InterruptedException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba",
                    "Problem s pripojením na aplikačný server!\nKontaktujte administrátora systému", e.getMessage());

        } catch (CommunicationException e) {
            e.printStackTrace();
            CustomAlert a = new CustomAlert("error", "Komunikačná chyba", "Komunikačná chyba na strane servera." +
                    "\nKontaktujte administrátora systému!", e.toString());
        }

        id = null;
        token = null;
        email = null;
    }*/

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        LoggedInUser.role = role;
    }
}
