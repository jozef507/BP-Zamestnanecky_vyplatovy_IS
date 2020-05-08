package application.httpcomunication;

import application.alerts.CustomAlert;
import application.exceptions.CommunicationException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpClientClass
{
     // one instance, reuse
    private HttpClient httpClient;
    private HttpRequest request;
    private HttpResponse<String> response;
    private ArrayList<String> paramNames;
    private ArrayList<String> paramValues;

    public HttpClientClass()
    {
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        this.request = null;
        this.response = null;
        this.paramNames = new ArrayList<>();
        this.paramValues = new ArrayList<>();
    }

    private void clearClient()
    {
        this.paramNames.clear();
        this.paramValues.clear();
    }

    public int getRespnseStatus()
    {
        return this.response.statusCode();
    }

    public String getRespnseBody()
    {
        return this.response.body();
    }

    public void addParam(String name, String value)
    {
        this.paramNames.add(name);
        this.paramValues.add(value);
    }

    public void sendGet(String uriSufix, String authorization, String userID) throws IOException, InterruptedException, CommunicationException {

        this.request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost/bp_rest_api/"+uriSufix))
                .setHeader("Client-Service", "jfx-desktop-frontend-client")
                .setHeader("Auth-Key", "simplerestapi")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Authorization", authorization)
                .setHeader("User-ID", userID)
                .build();
        this.response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        this.checkResponse();
    }

    public void sendDelete(String uriSufix, String authorization, String userID) throws IOException, InterruptedException, CommunicationException
    {
        this.request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost/bp_rest_api/"+uriSufix))
                .setHeader("Client-Service", "jfx-desktop-frontend-client")
                .setHeader("Auth-Key", "simplerestapi")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Authorization", authorization)
                .setHeader("User-ID", userID)
                .build();
        this.response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        this.checkResponse();
    }

    public void sendLoginPost(String email, String password) throws IOException, InterruptedException, CommunicationException {

        Map<Object, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        this.request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost/bp_rest_api/auth/login"))
                .setHeader("Client-Service", "jfx-desktop-frontend-client")
                .setHeader("Auth-Key", "simplerestapi")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        this.response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        this.checkResponse();
    }

    public boolean sendPost(String uriSufix, String authorization, String userID) throws IOException, InterruptedException, CommunicationException {

        if(this.paramNames.size() != this.paramValues.size())
            return false;

        Map<Object, Object> data = new HashMap<>();
        for (int i = 0; i<this.paramNames.size();++i)
        {
            data.put(paramNames.get(i), paramValues.get(i));
        }

        this.request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost/bp_rest_api/"+uriSufix))
                .setHeader("Client-Service", "jfx-desktop-frontend-client")
                .setHeader("Auth-Key", "simplerestapi")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Authorization", authorization)
                .setHeader("User-ID", userID)
                .build();
        this.response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        this.checkResponse();
        clearClient();
        return true;
    }

    public static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    private void checkResponse () throws CommunicationException{
        if (this.response.statusCode() >= 500&& this.response.statusCode() <= 599) {
            throw new CommunicationException(this.response.statusCode(), this.request.toString(), this.response.body());
        } else if (this.response.statusCode() >= 400 && this.response.statusCode() <= 499){
            if(this.response.statusCode() == 403) {
                JsonArrayClass json = new JsonArrayClass(getRespnseBody());
                String message = json.getElement(0, "message");
                if(message.equals("Zadany nespravny prihlasovaci e-mail!")) {
                    return;
                }else if(message.equals("Nemate pristup do tejto aplikacie!"))  {
                    return;
                }else if(message.equals("Zadane nespravne heslo!")){
                    return;
                } /*else if(message.equals("Zadany nespravny datum!")){
                    return;
                }*/
            }
            throw new CommunicationException(this.response.statusCode(), this.request.toString(), this.response.body());
        } else if (this.response.statusCode() >= 300 && this.response.statusCode() <= 399){
            throw new CommunicationException(this.response.statusCode(), this.request.toString(), this.response.body());
        }
    }

    public String errorMessage()
    {
        String s = "Požiadavka: " + this.request.toString() +"\n"+
                    "Kód odpovede: " + this.response.statusCode() + "\n"+
                    "Odpoveď: " + this.response.body();
        return s;
    }

}
