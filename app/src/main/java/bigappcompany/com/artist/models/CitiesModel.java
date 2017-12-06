package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artist.network.JsonParser;


public class CitiesModel {
	private final String id, city;


	public CitiesModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		city = object.getString(JsonParser.CITY);

	}
	//For DBAccess
	public CitiesModel(String id, String city)
	{
		this.id=id;

		this.city =city;


	}

	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}


}
