package bigappcompany.com.artist.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.JsonParser;


public class ImageObj implements Serializable{
	private final String id,sl_image,title;
	//For DBACCess
	private String local_path;

	public ImageObj(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		sl_image = ApiUrl.BASE_URL+object.getString(JsonParser.IMG_LINK);
		title= object.getString(JsonParser.TITLE);
	}
	//For DBAccess
	public ImageObj(String id, String musicUrl, String title)
	{
		this.id=id;
		this.sl_image=musicUrl;
		this.title=title;
	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
	}

	public String getId() {
		return id;
	}


	public String getPhoto() {
		return sl_image;
	}

	public String getTitle(){return title;}

	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}
}
