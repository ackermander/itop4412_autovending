package acmd.terminal.kits;

import java.util.Date;

public class Checker {
	/**
	 * 不在线
	 */
	public static final int BREAK_LIVE = 0;
	/**
	 * 在线
	 */
	public static final int HEALTH_LIVE = 1;
	/**
	 * 出现了延迟
	 */
	public static final int DELAY_LIVE = 2;
	
	public static int deviceRegisterChk(String nbr){
		return -1;
	}
	
	public static int delayChk(long chktime){
		long now = new Date().getTime();
		if(Math.abs(chktime - now) < 1000 * 60 * 5){
			return HEALTH_LIVE;
		}
		if(Math.abs(chktime - now) < 1000 * 60 * 5){
			return DELAY_LIVE;
		}
		return BREAK_LIVE;
	}
}
