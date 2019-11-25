package acmd.terminal.common;

public class DBConf {
	private int version;
	private int code;
	private String out;
	public DBConf(int code) {
		super();
		this.version = 0;
		this.code = code;
	}
	
	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
