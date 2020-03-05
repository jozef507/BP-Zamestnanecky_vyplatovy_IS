package application.httpcomunication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonObjectClass
{
    private JSONObject object;

    public JsonObjectClass(String j)
    {
        parseJson(j);
    }

    private void parseJson(String j)
    {
        JSONParser parser = new JSONParser();
        try {
            this.object = (JSONObject) parser.parse(j);
        } catch (ParseException e) {
            System.out.println("its not arrary");
            e.printStackTrace();
        }
    }

    public String getElement(String elementName)
    {
        return (object.get(elementName).toString());
    }


}
