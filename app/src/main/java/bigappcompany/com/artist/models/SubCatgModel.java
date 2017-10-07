package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artist.network.JsonParser;


public class SubCatgModel {
	private final String id, sub_catg;
	//For DBACCess
	private String path;

	public SubCatgModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		sub_catg = object.getString(JsonParser.CITY);
	}
	//For DBAccess
	public SubCatgModel(String id, String title)
	{
		this.id=id;

		this.sub_catg =title;


	}

	//For DBACCESS
	public void setPath(String path)
	{
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public String getSub_catg() {
		return sub_catg;
	}

	//DBACCESS
	public String getPath() {
		if(path ==null)
		{return "";}
		else
		return path;
	}
}
