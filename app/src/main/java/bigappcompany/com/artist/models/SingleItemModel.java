package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artist.network.JsonParser;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class SingleItemModel {


    private String name;
    private String url;
    private String description,id,date,cust_name,cust_mobile;
    private boolean isNew=false;


    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public SingleItemModel(JSONObject json) throws JSONException {
        this.id=json.getString(JsonParser.ID);
        this.date=json.getString(JsonParser.EVENT_START);
        this.cust_name=json.getString(JsonParser.CUST_NAME);
        this.cust_mobile=json.getString(JsonParser.CUST_MOBILE);
        this.name=json.getString(JsonParser.EVENT_NAME);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNew(boolean isNew)
    {
        this.isNew=isNew;
    }

    public boolean isNew(){
        return isNew;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
