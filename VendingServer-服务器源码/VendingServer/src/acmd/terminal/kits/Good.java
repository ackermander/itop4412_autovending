package acmd.terminal.kits;

public class Good {
	private int goodId;
	private String imgUrl;
	private float price;
	private String describe;
	private String name;
	private int stock;
	private int position;
	private int x;
	private int y;

	public Good(int goodId, String imgUrl, float price, String describe, String name, int stock, int position, int x, int y) {
		super();
		this.goodId = goodId;
		this.imgUrl = imgUrl;
		this.price = price;
		this.describe = describe;
		this.name = name;
		this.stock = stock;
		this.position = position;
		this.x = x;
		this.y = y;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getGoodId() {
		return goodId;
	}

	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
