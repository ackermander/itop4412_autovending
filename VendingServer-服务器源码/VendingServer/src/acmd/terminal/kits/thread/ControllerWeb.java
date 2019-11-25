package acmd.terminal.kits.thread;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import acmd.terminal.kits.DBKit;

@ServerEndpoint("/webcontroller/{uuid}")
public class ControllerWeb {
	public static final int VERSION = 5;
	public static final String ACTION_DEAL = "deal";
	private static int onlineDevices = 0;
	private static final Map<String, ControllerWeb> clients = new HashMap<>();
	private static final Map<String, ControllerWeb> unregs = new HashMap<>();
	private Session session;
	private String uuid;
	private String nbr;
	
	private static final String HAVE_TML = "SELECT * FROM terminals_describe WHERE terminal_uuid = ?";

	@OnOpen
	public void onOpen(@PathParam("uuid") String uuid, Session session) {
		System.out.println("WEBSOCKET: " + uuid);
		JsonObject resp = new JsonObject();
		resp.addProperty("version", 6);
		this.uuid = uuid;
		this.session = session;
		try {
			ResultSet r = DBKit.query(HAVE_TML, uuid);
			if (!r.next()) {
				System.out.println("false");
				resp.addProperty("result", false);
				unregs.put(uuid, this);
			} else {
				resp.addProperty("result", true);
				this.nbr = r.getString("terminal_nbr");
				clients.put(uuid, this);
				onlineDevices++;
			}
			r.close();
			r.getStatement().close();
		} catch (SQLException e) {
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			session.getBasicRemote().sendText(resp.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("close websocket: " + uuid);
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onlineDevices--;
		clients.remove(uuid);
		unregs.remove(uuid);
	}

	@OnClose
	public void onClose() {
		System.out.println("close websocket: " + uuid);
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onlineDevices--;
		clients.remove(uuid);
		unregs.remove(uuid);
	}

	@OnMessage
	public void onMessage(String text, Session session) {
		System.out.println(text);
	}

	public static void sendMessage(String uuid, String data) throws IOException {
		System.out.println("Websocket data: " + data);
		clients.get(uuid).session.getBasicRemote().sendText(data);
	}

	/**
	 * {version:5,action:"good-deal",x:1,y:1,}
	 * 
	 * @param x
	 *            < 16
	 * @param y
	 *            < 16
	 * @param version
	 * @param action
	 * @return the json string
	 */
	public static String ctrltml(int x, int y, int version, String action) {
		JsonObject cmd = new JsonObject();
		cmd.addProperty("x", x);
		cmd.addProperty("y", y);
		cmd.addProperty("version", version);
		cmd.addProperty("action", action);
		return cmd.toString();
	}
	
	public static void rec(String uuid, String nbr) throws IOException{
		boolean b = unregs.containsKey(uuid);
		System.out.println("rec: " + b);
		if(b){
			JsonObject jo = new JsonObject();
			jo.addProperty("version", 7);
			ControllerWeb c = unregs.get(uuid);
			c.setNbr(nbr);
			unregs.remove(uuid);
			clients.put(uuid, c);
			c.session.getBasicRemote().sendText(jo.toString());
		}
	}
	
	public static void rec1(String uuid) throws IOException{
		boolean b = clients.containsKey(uuid);
		System.out.println(clients.size());
		System.out.println("rec1: " + b);
		if(b){
			JsonObject jo = new JsonObject();
			jo.addProperty("version", 7);
			ControllerWeb c = clients.get(uuid);
			c.session.getBasicRemote().sendText(jo.toString());
		}
	}
	
	public static void allrec() throws IOException{
		JsonObject jo = new JsonObject();
		jo.addProperty("version", 7);
		for(Map.Entry<String, ControllerWeb> en : clients.entrySet()){
			ControllerWeb c = en.getValue();
			c.session.getBasicRemote().sendText(jo.toString());
		}
	}
	
	public static void closeAllSession() throws IOException{
		for(Map.Entry<String, ControllerWeb> en : clients.entrySet()){
			ControllerWeb c = en.getValue();
			c.session.close();
		}
		for(Map.Entry<String, ControllerWeb> en : unregs.entrySet()){
			ControllerWeb c = en.getValue();
			c.session.close();
		}
	}
	
	public static Map<String, ControllerWeb> allClients(){
		return clients;
	}
	public static Map<String, ControllerWeb> allUnregs(){
		return unregs;
	}
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNbr() {
		return nbr;
	}

	public void setNbr(String nbr) {
		this.nbr = nbr;
	}

}
