package acmd.terminal.common;

public class GoodImage {
	private int imgId;
	private String imgUrl;
	private String imgName;
	
	public GoodImage(int imgId, String imgUrl, String imgName) {
		super();
		this.imgId = imgId;
		this.imgUrl = imgUrl;
		this.imgName = imgName;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
}
