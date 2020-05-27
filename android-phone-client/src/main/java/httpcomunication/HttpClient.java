package com.example.bpandroid3.httpcomunication;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;


public class HttpClient
{
    Map<String,Object> postParams;
    byte[] postDataBytes;

    String response;
    int responseCode;

    public static String server_url;

    public HttpClient() {
        postParams = new LinkedHashMap<>();
    }

    public void addPostParam(String name, String value)
    {
        postParams.put(name, value);
    }

    public String getResponse()
    {
        return response;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    private void processParamsToPostdatabytes() throws Exception
    {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : postParams.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        postDataBytes = postData.toString().getBytes("UTF-8");
    }


    public void sendLoginPost() throws Exception {

        URL url = new URL("http://"+server_url+"auth/login");

        processParamsToPostdatabytes();

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Auth-Key", "simplerestapi");
        conn.setRequestProperty("Client-Service", "android-phone-frontend-client");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        /*Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        response = sb.toString();*/

        responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        }

    }


    public void sendPost(String uriSufix, String authorization, String userID) throws Exception
    {
        URL url = new URL("http://"+server_url+uriSufix);

        processParamsToPostdatabytes();

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Auth-Key", "simplerestapi");
        conn.setRequestProperty("Client-Service", "android-phone-frontend-client");
        conn.setRequestProperty("Authorization", authorization);
        conn.setRequestProperty("User-ID", userID);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        /*Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        response = sb.toString();*/

        responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        }
    }


    public void sendGet(String uriSufix, String authorization, String userID) throws Exception {

        URL url = new URL("http://"+server_url+uriSufix);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection.setRequestProperty("Client-Service", "android-phone-frontend-client");
        connection.setRequestProperty("Auth-Key", "simplerestapi");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("User-ID", userID);

        responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader( connection.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.response = response.toString();
        }
    }
}
