package acmd.terminal.common;

public class GoodV1 {
	private int id;
	private String name;
	private float price;
	private int urlId;
	private String desc;
	private int stock;
	private int x;
	private int y;
	
	public GoodV1(int id, String name, float price, int urlId, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.urlId = urlId;
		this.desc = desc;
	}
	
	public GoodV1(int id, String name, float price, int urlId, String desc, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.urlId = urlId;
		this.desc = desc;
		this.stock = stock;
	}
	
	public GoodV1(int id, String name, float price, int urlId, String desc, int stock, int x, int y) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.urlId = urlId;
		this.desc = desc;
		this.stock = stock;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
