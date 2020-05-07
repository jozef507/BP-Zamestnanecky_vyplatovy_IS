package com.example.bpandroid3.httpcomunication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonArrayClass
{
    private JSONArray array;

    public JsonArrayClass(String j)
    {
        parseJson(j);
    }

    private void parseJson(String j)
    {
        if(j.length()>0)
        {
            if(j.charAt(0)=='[') {
                try {
                    this.array = new JSONArray(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject object = new JSONObject(j);
                    this.array = new JSONArray();
                    this.array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getElement(int index, String elementName)
    {
        try{
            return (((JSONObject)array.get(index)).get(elementName)).toString();
        } catch (NullPointerException e) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSize()
    {
        return this.array.length();
    }
}
