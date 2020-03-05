package application.exceptions;

public class CommunicationException extends Exception
{
    private String request;
    private int statusCode;
    private String response;

    public CommunicationException(int code)
    {
        this.statusCode = code;
    }

    public CommunicationException(int code, String req, String res)
    {
        this.statusCode = code;
        this.request = req;
        this.response =res;
    }

    public String toString()
    {
        return "Chyba pri komunikácii so serverom.\n" +
                "Požiadavka: "+ this.request+"\n"+
                "Návratený stavový kód: "+ this.statusCode+"\n"+
                "Odpoveď: "+ this.response+"\n";
    }
}
