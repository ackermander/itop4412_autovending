package acmd.terminal.kits;

import java.util.Date;

import com.google.gson.JsonObject;

public class ResponseKit {
	/**
	 * �ͻ��˷������߼�������, ������Ӧ��. {"version": 1, "check-result": "health_live",
	 * "result": "pass", "time": 123123112}
	 * 
	 * @param liveCode
	 * @return
	 */
	public static JsonObject liveResponseV1(int liveCode) {
		JsonObject response = new JsonObject();
		response.addProperty("version", 1);
		response.addProperty("time", new Date().getTime());
		response.addProperty("type", "live-check");
		switch (liveCode) {
		case Checker.HEALTH_LIVE:
			response.addProperty("check-result", "health-live");
			response.addProperty("result", "pass");
			break;
		case Checker.DELAY_LIVE:
			response.addProperty("check-result", "delay-live");
			// ����. ͨ����ѯ���ݱ����ý��.
			response.addProperty("result", "");
			break;
		case Checker.BREAK_LIVE:
			response.addProperty("check-result", "break-live");
			break;
		default:
			response.addProperty("check-result", "mistake-protocol");
			response.addProperty("result", "unpass");
			break;
		}
		return response;
	}
}
