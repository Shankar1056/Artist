package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artist.network.JsonParser;


public class CatgModel {
	private final String id, city;
	//For DBACCess
	private String local_path;

	public CatgModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		city = object.getString(JsonParser.CITY);

	}
	//For DBAccess
	public CatgModel(String id, String title)
	{
		this.id=id;

		this.city =title;


	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
	}

	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}
}
