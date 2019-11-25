package acmd.terminal.request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.google.gson.JsonObject;

import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;

public class PayQRCodeRequest extends AbstractTerminalRequest {
	public static final int VERSION = 3;
	public static final String ACTION = "get-payment-code";
	public static final String ACTION_RESP_QR = "qr-resp";
	private final static String SELECT_RES = "SELECT * FROM goods_describe AS a, goods_terminal_mapping AS b, terminals_describe AS c WHERE b.good_id = a.good_id AND b.terminal_id = c.terminal_id AND c.terminal_uuid = ? AND a.good_id = ?;";
	private final static String INSERT_CHECK = "INSERT INTO checks_detail (check_name, check_amount, terminal_id, pay_statu, create_time, good) VALUES (?,?,?,0,?,?)";

	/**
	 * �����������ݺ󴴽�֧������, ���浽���ݿ���.
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	public static JsonObject createQRCode(String data) throws SQLException {
		JsonObject JSONData = JSONparse(data);
		String model = JSONData.get("model").getAsString();
		String uuid = "";
		uuid = JSONData.get("uuid").getAsString();
		JsonObject good = JSONData.get("good").getAsJsonObject();
		int position = good.get("position").getAsInt();
		int goodId = good.get("goodId").getAsInt();
		ResultSet r = DBKit.query(SELECT_RES, uuid, goodId);
		boolean next = r.next();
		System.out.println("stock: " + next);
		JsonObject jo = new JsonObject();
		jo.addProperty("uuid", uuid);
		if (next) {
			int stock = r.getInt("b.good_stock");
			if (stock > 0) {
				UUID checkName = UUID.randomUUID();
				float price = UserKit.getPrice(r);
				long createTime = new Date().getTime();
				DBKit.update(INSERT_CHECK, checkName.toString(), price, uuid, createTime, goodId);
				jo.addProperty("qr", checkName.toString());
				jo.addProperty("create_time", createTime);
				jo.addProperty("position", position);
				jo.addProperty("version", 3);
				jo.addProperty("action", ACTION_RESP_QR);
			}
		}
		r.close();
		r.getStatement().close();
		return jo;
	}
}
