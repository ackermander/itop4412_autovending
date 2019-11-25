package acmd.terminal.kits;

import java.util.Date;

import com.google.gson.JsonObject;

public class ResponseKit {
	/**
	 * 客户端发送在线检测请求后, 服务器应答. {"version": 1, "check-result": "health_live",
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
			// 待定. 通过查询数据表设置结果.
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
