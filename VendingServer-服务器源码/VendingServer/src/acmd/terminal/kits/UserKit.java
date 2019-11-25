package acmd.terminal.kits;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UserKit {
	public static Date timeParser(String datestr) {
		Date result = null;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			result = dateformat.parse(datestr);
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ʱ���޹�
	 * 
	 * @param time
	 * @return
	 */
	public static long timeMillisec(Date time) {
		return time.getTime();
	}
	
	public static float getPrice(ResultSet r) throws SQLException{
		
		float price = r.getFloat("b.good_price");
		if(price == -1){
			price = r.getFloat("a.good_price");
		}
		return price;
	}

	public class JSONResponse {
		private JsonObject jObject = null;

		public JSONResponse() {
			jObject = new JsonObject();
		}
		public JSONResponse addAttribute(String key, Object value) {
			if (value instanceof Number) {
				jObject.addProperty(key, (Number) value);
			} else if (value instanceof Character) {
				jObject.addProperty(key, (Character) value);
			} else if (value instanceof String) {
				jObject.addProperty(key, (String) value);
			} else if (value instanceof Boolean) {
				jObject.addProperty(key, (Boolean) value);
			} else if (value instanceof JsonElement){
				jObject.add(key,(JsonElement) value);
			} else {
			}
			return this;
		}
	}
	
	public static final String md5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	
	public static String toJdbcUrl(String url, String db){
		return "jdbc:mysql://" + url + "/"+ db +"?useSSL=false";
	}
	
	public static String randomname(){
		char[] cs = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		int cs的长度 = cs.length;
		String name = "";
		for(int i = 0; i < 11; i++){
			name += cs[(int) (Math.random() * 62)];
		}
		return name;
	}
}
