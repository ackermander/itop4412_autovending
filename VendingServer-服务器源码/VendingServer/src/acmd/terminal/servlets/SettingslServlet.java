package acmd.terminal.servlets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import acmd.terminal.common.GoodImage;
import acmd.terminal.common.GoodV1;
import acmd.terminal.common.Terminal;
import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.thread.ControllerWeb;

/**
 * Servlet implementation class TerminalServlet
 */
@WebServlet("/settings")
public class SettingslServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ALL_TERMINALS = "SELECT terminal_id, terminal_nbr, terminal_uuid FROM terminals_describe";
	private static final String ADD_TERMINAL = "INSERT INTO terminals_describe (terminal_nbr, terminal_uuid) VALUES (?,?)";
	private static final String A_TERMINAL = "SELECT terminal_id FROM terminals_describe WHERE terminal_nbr = ? AND terminal_uuid = ?";
	private static final String A_TERMINAL_UUID = "SELECT terminal_uuid FROM terminals_describe WHERE terminal_id = ?";
	private static final String ADD_IMG = "INSERT INTO good_image (img_url, img_name) VALUE (?,?)";
	private static final String AN_IMG = "SELECT img_id FROM good_image WHERE img_name = ?";
	private static final String ADD_GOOD = "INSERT INTO goods_describe (good_name, good_price, url_id, good_describe) VALUES (?,?,?,?)";
	private static final String A_GOOD = "SELECT good_id FROM good_describe WHERE good_name = ?";
	private static final String ADD_GOOD_TML_MAP = "INSERT INTO goods_terminal_mapping (terminal_id, good_id, good_price, good_stock, good_psi, url_id) VALUES(?,?,?,?,?,?)";
	private static final String ADD_GOOD_TML_PSI = "INSERT INTO good_position_mapping (position_statu, x, y, terminal_id, good_id, stock) VALUES (?,?,?,?,?,?)";
	private static final String ALL_GOOD = "SELECT * FROM goods_describe";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SettingslServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("GET");
	}

	/**
	 * 0: 获得所有终端
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json");
		String result = "result";
		System.out.println("post setting servlet");
		String actionCode = request.getParameter("action");
		int actioncode = Integer.parseInt(actionCode);
		Gson g = new Gson();
		JsonObject resp = new JsonObject();
		resp.addProperty("actioncode", actioncode);
		switch (actioncode) {
		// 获得所有的终端信息
		case 0: {
			getAllTmlInfo(g, resp);
			break;
		}
		// 添加的终端
		case 1: {
			add_terminal(request, resp);
			break;
		}
		// 将货物添加到终端中
		// {'action':2, 'tml': tml, 'gdid':gdid, 'x': x, 'y': y, 'price': price,
		// 'stock': stock},
		case 2: {
			addGood2Tml(request, resp);
			break;
		}
		// 添加一个货物信息
		case 3: {
			addGood(request, resp);
			break;
		}
		// 获得所有的货物信息
		case 4: {
			getAllGood(result, g, resp);
			break;
		}
		//获得终端下的货物信息
		case 5: {
			terminalGoods(request, g, resp);
			break;
		}
		//获得图片地址
		case 6:{
			getPic(request, resp);
			break;
		}
		//获得所有图片信息
		case 7:{
			allPics(g, resp);
			break;
		}
		//更新终端货物
		case 8:{
			updateTmlGood(request, resp);
			break;
		}
		//获得所有连接的websocket
		case 9:{
			getWebSockets(resp);
			break;
		}
		// 删除指定的终端的一个货物
		case 10:{
			String SELECT_POSITION = 
					"SELECT good_psi FROM goods_terminal_mapping WHERE terminal_id = ? AND good_id = ?";
			String DROP_GOOD_MAPPING = 
					"DELETE FROM goods_terminal_mapping WHERE terminal_id = ? AND good_id = ?";
			String DROP_POSITION_MAPPING = 
					"DELETE FROM good_position_mapping WHERE position_id = ?";
			//还是不要做删除操作
			break;
		}
		case 103: {
			alterKind(request, resp);
			break;
		}
		}
		response.getWriter().write(resp.toString());
	}

	private void getWebSockets(JsonObject resp) {
		Map<String, ControllerWeb> cs = ControllerWeb.allClients();
		Map<String, ControllerWeb> unreg = ControllerWeb.allUnregs();
		JsonArray ja = new JsonArray();
		for(Map.Entry<String, ControllerWeb> en: cs.entrySet()){
			JsonObject jo = new JsonObject();
			jo.addProperty("nbr", en.getValue().getNbr());
			jo.addProperty("uuid", en.getValue().getUuid());
			ja.add(jo);
		}
		JsonArray jb = new JsonArray();
		for(Map.Entry<String, ControllerWeb> en: unreg.entrySet()){
			jb.add(en.getKey());
		}
		resp.add("uuids", ja);
		resp.add("unregs", jb);
		System.out.println(resp.toString());
	}

	private void updateTmlGood(HttpServletRequest request, JsonObject resp) throws IOException {
		String UPDATE_TML_GOOD = 
				"UPDATE good_position_mapping AS a, goods_terminal_mapping AS b SET a.stock = ?, b.good_stock = ?, b.url_id = ?, a.x = ?, a.y = ?, b.good_price = ? WHERE b.terminal_id = ? AND b.good_psi = a.position_id AND a.good_id = ?";
		String tmlid = request.getParameter("tmlid");
		String gdid = request.getParameter("gdid");
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String price = request.getParameter("price");
		String stock = request.getParameter("stock");
		String urlid = request.getParameter("urlid");
		String uuid = request.getParameter("uuid");
		try {
			DBKit.update(UPDATE_TML_GOOD, stock,stock,urlid,x,y,price,tmlid,gdid);
			ControllerWeb.rec1(uuid);
			resp.addProperty("result", true);
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void allPics(Gson g, JsonObject resp) {
		String ALL_PIC = 
				"SELECT img_id, img_url, img_name FROM good_image";
		try {
			ResultSet r = DBKit.query(ALL_PIC);
			JsonObject pics = new JsonObject();
			while(r.next()){
				int imgId = r.getInt("img_id");
				String imgUrl = r.getString("img_url");
				String imgName = r.getString("img_name");
				GoodImage img = new GoodImage(imgId, imgUrl, imgName);
				pics.add(imgId + "", g.toJsonTree(img));
			}
			resp.add("pics", pics);
			resp.addProperty("result", true);
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void getPic(HttpServletRequest request, JsonObject resp) {
		String urlid = request.getParameter("urlid");
		String AN_URL = 
				"SELECT img_url FROM good_image WHERE img_id = ?";
		try {
			ResultSet r = DBKit.query(AN_URL, urlid);
			if(r.next()){
				resp.addProperty("result", true);
				resp.addProperty("url", r.getString("img_url"));
			}else{
				resp.addProperty("result", false);
				return;
			}
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void terminalGoods(HttpServletRequest request, Gson g, JsonObject resp) {
		String tmlid = request.getParameter("tmlid");
		String TML_GOODS = "SELECT * FROM goods_terminal_mapping AS a, goods_describe AS b, good_position_mapping AS c WHERE a.terminal_id = ? AND a.good_psi = c.position_id AND c.good_id = b.good_id;";
		try {
			ResultSet r = DBKit.query(TML_GOODS, tmlid);
			JsonArray goods = new JsonArray();
			while (r.next()) {
				int id = r.getInt("b.good_id");
				String name = r.getString("b.good_name");
				float price = r.getFloat("a.good_price");
				if(price == 0){
					price = r.getFloat("b.good_price");
				}
				int urlId = r.getInt("a.url_id");
				if(urlId==0){
					urlId = r.getInt("b.url_id");
				}
				int stock = r.getInt("a.good_stock");
				int x = r.getInt("c.x");
				int y = r.getInt("c.y");
				String desc = r.getString("b.good_describe");
				GoodV1 good = new GoodV1(id, name, price, urlId, desc, stock, x, y);
				goods.add(g.toJsonTree(good));
			}
			resp.add("goods", goods);
			resp.addProperty("result", true);
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void alterKind(HttpServletRequest request, JsonObject resp) {
		String url = request.getParameter("url");
		String urlid = request.getParameter("urlid");
		String imgid = urlid;
		String name = request.getParameter("name");
		String price = request.getParameter("price");
		String desc = request.getParameter("desc");
		String goodid = request.getParameter("goodid");
		if (url == null) {
			String UPDATE_KIND = "UPDATE goods_describe SET good_name = ?, good_price = ?, good_describe = ? WHERE good_id = ?";
			try {
				DBKit.update(UPDATE_KIND, name, price, desc, goodid);
				resp.addProperty("result", true);
				resp.addProperty("out", "修改货物成功");
				ControllerWeb.allrec();
			} catch (SQLException | IOException e) {
				resp.addProperty("out", "修改货物失败");
				resp.addProperty("result", false);
				e.printStackTrace();
			}
		} else {
			String UPDATE_KIND_PIC = "UPDATE goods_describe SET good_name = ?, good_price = ?, good_describe = ?, url_id = ? WHERE good_id = ?";
			try {
				int id = DBKit.update(ADD_IMG, url, name + "_pic");
				DBKit.update(UPDATE_KIND_PIC, name, price, desc, id, goodid);
				resp.addProperty("result", true);
				resp.addProperty("out", "修改货物成功");
				ControllerWeb.allrec();
			} catch (SQLException | IOException e) {
				resp.addProperty("out", "修改货物失败");
				resp.addProperty("result", false);
				e.printStackTrace();
			}
		}
	}

	private void getAllGood(String result, Gson g, JsonObject resp) {
		try {
			ResultSet r = DBKit.query(ALL_GOOD);
			JsonArray goods = new JsonArray();
			while (r.next()) {
				int id = r.getInt("good_id");
				String name = r.getString("good_name");
				float price = r.getFloat("good_price");
				int urlId = r.getInt("url_id");
				String desc = r.getString("good_describe");
				GoodV1 good = new GoodV1(id, name, price, urlId, desc);
				goods.add(g.toJsonTree(good));
			}
			resp.add("goods", goods);
			resp.addProperty(result, true);
		} catch (SQLException e) {
			resp.addProperty(result, false);
			e.printStackTrace();
		}
	}

	private void addGood(HttpServletRequest request, JsonObject resp) {
		System.out.println("case 3");
		String url = request.getParameter("url");
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		String price = request.getParameter("price");
		try {
			DBKit.update(ADD_IMG, url, name + "_pic");
			ResultSet r = DBKit.query(AN_IMG, name + "_pic");
			if (!r.next()) {
				resp.addProperty("result", false);
				return;
			}
			int imgId = r.getInt("img_id");
			DBKit.update(ADD_GOOD, name, price, imgId, desc);
			r.close();
			r.getStatement().close();
			resp.addProperty("result", true);
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void getAllTmlInfo(Gson g, JsonObject resp) {
		try {
			JsonObject ts = new JsonObject();
			ResultSet r = DBKit.query(ALL_TERMINALS);
			while (r.next()) {
				Terminal t = new Terminal();
				t.setTerminalUUID(r.getString("terminal_uuid"));
				t.setTerminalId(r.getInt("terminal_id"));
				t.setTerminalNumber(r.getString("terminal_nbr"));
				JsonObject jt = g.toJsonTree(t).getAsJsonObject();
				ts.add(r.getInt("terminal_id") + "", jt);
			}
			r.close();
			r.getStatement().close();
			resp.addProperty("result", true);
			resp.add("terminals", ts);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addProperty("result", false);
		}
	}

	private void addGood2Tml(HttpServletRequest request, JsonObject resp) {
		try {
			String tml = request.getParameter("tml");
			String gdid = request.getParameter("gdid");
			String x = request.getParameter("x");
			String y = request.getParameter("y");
			String price = request.getParameter("price");
			String stock = request.getParameter("stock");
			String urlid = request.getParameter("urlid");
			String uuid = "";
			
			ResultSet r = DBKit.query(A_TERMINAL_UUID, tml);
			r.next();
			uuid = r.getString("terminal_uuid");
			// 添加货物位置
			int key = DBKit.update(ADD_GOOD_TML_PSI, 0, x, y, tml, gdid, stock);
			// 然后将货物位置添加到终端中
			key = DBKit.update(ADD_GOOD_TML_MAP, tml, gdid, price, stock, key, urlid);
			// 提示服务器更新界面
			ControllerWeb.rec1(uuid);
			resp.addProperty("key", key);
			resp.addProperty("result", true);
		} catch (Exception e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

	private void add_terminal(HttpServletRequest request, JsonObject resp) throws IOException {
		String nbr = request.getParameter("nbr");
		String uuid = request.getParameter("uuid");
		try {
			DBKit.update(ADD_TERMINAL, nbr, uuid);
			resp.addProperty("result", true);
			ResultSet r = DBKit.query(A_TERMINAL, nbr, uuid);
			if (!r.next()) {
				resp.addProperty("result", false);
				return;
			}
			resp.addProperty("terminal_id", r.getInt("terminal_id"));
			ControllerWeb.rec(uuid, nbr);
			r.close();
			r.getStatement().close();
		} catch (SQLException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}
	}

}
