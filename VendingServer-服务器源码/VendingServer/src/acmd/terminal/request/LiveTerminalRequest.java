package acmd.terminal.request;

import java.util.Date;

import com.google.gson.JsonObject;

import acmd.terminal.kits.Checker;
import acmd.terminal.kits.UserKit;

public class LiveTerminalRequest extends AbstractTerminalRequest {
	private static LiveTerminalRequest liveCommunicate = null;
	
	private LiveTerminalRequest() {
		super();
	}

	public static LiveTerminalRequest LiveCommunicate() {
		if (liveCommunicate == null) {
			liveCommunicate = new LiveTerminalRequest();
		}
		return liveCommunicate;
	}
	

	/**
	 * ǰ�����汾ֻ���һ̨�ն��Ƿ�����
	 * { "version": 1, "request-type": "live-request", "terminal-nbr": "TEST_1",
	 * "time": "2018-1-1 12:00:00" } { "version": 2, "request-type":
	 * "live-request", "terminal-nbr": "TEST_1", "time": 1533686888000 }
	 * ��ʱ���ɼ���ն�λ�õ�
	 * @param JSON
	 * @return true ��ʾ�ͻ������������ߵ�(���籣��ͨ����). false ��ʾ�ͻ��������ߵ�.
	 */
	public int liveAnalyse(String JSON) {
		JsonObject parameters = JSONparse(JSON);
		int version = parameters.get("version").getAsInt();
		String requestType = parameters.get("request-type").getAsString();
		System.out.println(JSON);
		if (version == 1) {
			long time = UserKit.timeMillisec(UserKit.timeParser(parameters.get("time").getAsString()));
			return Checker.delayChk(time);
		} else if (version == 2) {
			long time = parameters.get("time").getAsLong();
			return Checker.delayChk(time);
		}
		return Checker.BREAK_LIVE;
	}
	
	
}
