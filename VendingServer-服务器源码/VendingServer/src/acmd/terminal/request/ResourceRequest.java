package acmd.terminal.request;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import acmd.terminal.kits.Checker;
import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.Good;

public class ResourceRequest extends AbstractTerminalRequest {
	private static ResourceRequest resourceRequest = null;

	private ResourceRequest() {
	}

	public static ResourceRequest ResourceBuilder() {
		if (resourceRequest == null) {
			resourceRequest = new ResourceRequest();
		}
		return resourceRequest;
	}

	/**
	 * {"model":"Android SDK built for
	 * x86","version":1,"serial":"unknown","uuid":"39e26986fcc3a8d9","action":"getResAction"}
	 * version 1 ��ͬaction ΪgetResAction, ��ʾ��ø��ն˵����л�����Ϣ.
	 * 
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	public JsonObject requestAnalyse(String request) throws SQLException {
		JsonObject JSONRequest = JSONparse(request);
		JsonObject resp = null;
		switch (JSONRequest.get("version").getAsInt()) {
		case 1:
			if (JSONRequest.get("model").getAsString().contains("x86")) {
				resp = requestOperateV1x86(JSONRequest);
				break;
			} else if(JSONRequest.get("model").getAsString().contains("Android")){
				resp = requestOperateV1x86(JSONRequest);
				break;
			} else {
				resp = requestOperateV1x86(JSONRequest);
				break;
			}
		}
		return resp;
	}

	private final static String SELECT_RES = "SELECT * FROM goods_describe AS a, goods_terminal_mapping AS b, terminals_describe AS c, good_position_mapping AS d WHERE b.good_id = a.good_id AND b.terminal_id = c.terminal_id AND c.terminal_uuid = ? AND d.position_id = b.good_psi;";
	private final static String SELECT_IMG_RES = "SELECT * FROM good_image WHERE img_id = ?";
	private static final String PSI_XY = 
			"";
	private JsonObject requestOperateV1x86(JsonObject JSON) throws SQLException {
		JsonObject JSONresp = new JsonObject();
		JsonArray JSONGoods = new JsonArray();
		String uuid = JSON.get("uuid").getAsString();
		ResultSet r = DBKit.query(SELECT_RES, uuid);
		while(r.next()){
			int goodId = r.getInt("a.good_id");
			int urlId = r.getInt("b.url_id");
			float price = r.getFloat("b.good_price");
			String describe = r.getString("good_describe");
			String goodName = r.getString("good_name");
			int position = r.getInt("good_psi");
			int x = r.getInt("x");
			int y = r.getInt("y");
			int stock = r.getInt("good_stock");
			if(urlId == -1){
				urlId = r.getInt("a.url_id");
			}
			if(price == -1){
				price = r.getFloat("a.good_price");
			}
			ResultSet r1 = DBKit.query(SELECT_IMG_RES, urlId);
			Good good = null;
			while(r1.next()){
				good = new Good(goodId, r1.getString("img_url"), price, describe, goodName, stock, position, x, y);
			}
			r1.close();
			JsonObject JSONGood = gson.toJsonTree(good).getAsJsonObject();
			JSONGoods.add(JSONGood);
		}
		JSONresp.add("goods", JSONGoods);
		r.close();
		r.getStatement().close();
		return JSONresp;
	}
}
