package acmd.terminal.request;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AbstractTerminalRequest implements ITerminalRequest {
	public static Gson gson = new Gson();
	public static JsonParser jsonp = new JsonParser();
	protected AbstractTerminalRequest(){}
	
	public static JsonObject JSONparse(String JSON){
		JsonObject parameters = jsonp.parse(JSON).getAsJsonObject();
		return parameters;
	}
	
	
}
