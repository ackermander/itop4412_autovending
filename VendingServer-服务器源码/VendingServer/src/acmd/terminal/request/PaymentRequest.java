package acmd.terminal.request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;

public class PaymentRequest extends AbstractTerminalRequest {
	public static final int VERSION = 4;
	public static final String ACTION_READY = "ready";
	public static final String ACTION_CONFIRM = "confirm";

	public void readyToPay(String data) {
		System.out.println("�û�׼��֧��" + data);
	}

	public static final String SELECT_CHECK = "SELECT * FROM checks_detail WHERE check_name = ? AND terminal_id = ?";
	public static final String EXIST_TERMINAL = "SELECT * FROM terminals_describe WHERE terminal_uuid = ?";

	/**
	 * {"check-uuid":123123123123123,"tmnl-uuid":123123123,"time":12312312L,"version":4,"action":"confirm"}
	 * 
	 * @param data
	 * @throws SQLException
	 */
	public JsonObject confirmedCheck(JsonObject JSONData) throws SQLException {
		String action;
		int version;
		String checkName;
		String uuid;
		int position;
		JsonObject result = new JsonObject();
		result.addProperty("r", 0);
		try {
			checkName = JSONData.get("check-name").getAsString();
			version = JSONData.get("version").getAsInt();
			action = JSONData.get("action").getAsString();
			uuid = JSONData.get("uuid").getAsString();
			position = JSONData.get("position").getAsInt();
			System.out.println("PaymentRequest.confirmedCheck: psi = " + position);
		} catch (NullPointerException e) {
			System.out.println("PaymentRequest.confirmedCheck: 1");
			// �������ݸ��ͻ�����ʾ
			return result;
		}
		if (version != 4 || !action.equals("confirm")) {
			System.out.println("PaymentRequest.confirmedCheck: 2");
			// �������ݸ��ͻ�����ʾ
			return result;
		}
		System.out.println(uuid);
		ResultSet r = DBKit.query(SELECT_CHECK, checkName, uuid);
		if (!r.next()) {
			System.out.println("PaymentRequest.confirmedCheck: 3");
			r.getStatement().close();
			r.close();
			return result;
		}
		int checkId = r.getInt("check_id");
		String terminalUUID = r.getString("terminal_id");//
		int goodId = r.getInt("good");
		long createTime = r.getLong("create_time");
		int statu = r.getInt("pay_statu");
		int checkAmount = r.getInt("check_amount");
		r.getStatement().close();
		r.close();
		if (statu == 1) {
			System.out.println("PaymentRequest.confirmedCheck: 4");
			return result;
		}
		// �жϸ÷������Ƿ���ע��
		r = DBKit.query(EXIST_TERMINAL, terminalUUID);
		if (!r.next()) {
			System.out.println("PaymentRequest.confirmedCheck: 5");
			return result;
		}
		int terminalId = r.getInt("terminal_id");
		long now = new Date().getTime();
		if (Math.abs(now - createTime) > 60 * 1000 * 5) {
			System.out.println("PaymentRequest.confirmedCheck: 6");
			return result;
		}
		JsonObject paySuccess = paySuccess(terminalId, goodId, checkId, position);
		if (paySuccess.get("r").getAsBoolean()) {
			result.addProperty("r", 1);
			result.addProperty("x", paySuccess.get("x").getAsInt());
			result.addProperty("y", paySuccess.get("y").getAsInt());
			return result;
		} else {
			return result;
		}
	}

	public static final String UPDATE_CHECK = "UPDATE checks_detail SET pay_statu = 1 WHERE check_id = ?";
	public static final String UPDATE_GOODS = "UPDATE goods_terminal_mapping SET good_stock = good_stock - 1 WHERE terminal_id = ? AND good_id = ?";
	private static final String SELECT_POSITION = "SELECT * FROM good_position_mapping WHERE position_id = ?";
	private static final String UPDATE_POSITION = "UPDATE good_position_mapping SET stock = stock - 1, position_statu = ? WHERE mapping_id = ?";

	public JsonObject paySuccess(int terminalId, int goodId, int checkId, int positionId) throws SQLException {
		JsonObject result = new JsonObject();
		ResultSet r = DBKit.query(SELECT_POSITION, positionId);
		while (r.next()) {
			int stock = r.getInt("stock");
			if (stock > 0) {
				int mappingId = r.getInt("mapping_id");
				int statu = 0;
				if (stock == 1) {
					statu = 1;
				}
				DBKit.update(UPDATE_POSITION, statu, mappingId);
				result.addProperty("x", r.getInt("x"));
				result.addProperty("y", r.getInt("y"));
				break;
			} else {
				if (r.isLast()) {
					result.addProperty("r", false);
					return result;
				}
			}
		}
		r.getStatement().close();
		r.close();
		DBKit.update(UPDATE_CHECK, checkId);
		DBKit.update(UPDATE_GOODS, terminalId, goodId);
		result.addProperty("r", true);
		return result;
	}

}
