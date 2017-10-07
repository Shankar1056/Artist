package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artist.network.JsonParser;


public class RequestsModel {
	private final String id, event_name, name,date,address,phone;

	String customer_id;

	public RequestsModel(JSONObject json) throws JSONException {
		this.id=json.getString(JsonParser.ID);
		this.date=json.getString(JsonParser.EVENT_START);
		this.name=json.getString(JsonParser.CUST_NAME);
		this.phone=json.getString(JsonParser.CUST_MOBILE);
		this.event_name=json.getString(JsonParser.EVENT_NAME);
		this.address=json.getString(JsonParser.EVENT_VENUE);
	}
	//For DBAccess
	public RequestsModel(String id, String name,  String event_name, String date,String address,String phone)
	{
		this.id=id;
		this.name =name;
		this.event_name = event_name;
		this.date=date;
		this.address=address;
		this.phone=phone;
	}



	public String getId() {
		return id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public String getName(){return name;}

	public String getDate(){return date;}

	public String getAddress(){return address;}

	public String getPhone(){return phone;}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}
}
