package application.httpcomunication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonArrayClass
{
    private JSONArray array;

    public JsonArrayClass(String j)
    {
        parseJson(j);
    }

    private void parseJson(String j)
    {

        JSONParser parser = new JSONParser();
        if(j.charAt(0)=='[') {
            try {
                this.array = (JSONArray) parser.parse(j);
            } catch (ParseException e) {
                System.out.println("its not arrary");
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject object = (JSONObject) parser.parse(j);
                this.array = new JSONArray();
                this.array.add(object);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getElement(int index, String elementName)
    {
        try{
            return (((JSONObject)array.get(index)).get(elementName)).toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public int getSize()
    {
        return this.array.size();
    }
}
