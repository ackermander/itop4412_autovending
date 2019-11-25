package acmd.terminal.junit;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import acmd.terminal.common.Administrator;
import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;
import acmd.terminal.request.ResourceRequest;

public class UnitOperate {

	@Test
	public void test1() throws SQLException, InterruptedException {
		ComboPooledDataSource pool = new ComboPooledDataSource();
		Connection connection = pool.getConnection();
//		PreparedStatement p = connection.prepareStatement(
//				"SELECT * FROM vdnsvr.goods_describe AS a, vdnsvr.goods_terminal_mapping AS b, vdnsvr.terminals_describe AS c WHERE b.good_id = a.good_id AND b.terminal_id = c.terminal_id;");
		PreparedStatement p = connection.prepareStatement("CREATE DATABASE testtable");
		int r = p.executeUpdate();
	}
	
	@Test
	public void test2() {
		JsonElement parse = new JsonParser().parse("{\"log\":\"aaa\"}");
		JsonObject jo = parse.getAsJsonObject();
		JsonElement jsonElement = jo.get("log");
		System.out.println(jsonElement.isJsonObject());
		System.out.println(jsonElement.getAsString());
	}

	@Test
	public void test3() {
		long time = UserKit.timeParser("2018-8-8 8:08:08").getTime();
		System.out.println(time);

	}
	/**
	 * {"model":"Chrome","request-type":"get-resources","version":1,"serial":"unknown","uuid":null,"action":"getResAction","time":1520143925504}
	 * @throws SQLException 
	 */
	@Test
	public void test4() throws SQLException{
		String jos = "{\"model\":\"x86\",\"request-type\":\"get-resources\",\"version\":1,\"serial\":\"unknown\",\"uuid\":\"39e26986fcc3a8d9\",\"action\":\"getResAction\",\"time\":1520143925504}";
		JsonObject jo = new JsonParser().parse(jos).getAsJsonObject();
		ResourceRequest res = ResourceRequest.ResourceBuilder();
		JsonObject requestAnalyse = res.requestAnalyse(jos);
		System.out.println(requestAnalyse);
	}
	public class Threadtest5 extends Thread{
		public boolean a = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(a){
				System.out.println("in");
			}
		}
	}
	@Test
	public void test5() throws IOException, InterruptedException{
		Threadtest5 t = new Threadtest5();
		
		t.start();
		Thread.sleep(1000);
		t.a = false;
		while(true){
		}
	}
	
	@Test
	public void test6() throws IOException{
		ServerSocket ss = new ServerSocket(9090);
		Socket accept = ss.accept();
		InputStream in = accept.getInputStream();
		BufferedInputStream bin = new BufferedInputStream(in);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		DataInputStream dis = new DataInputStream(in);
		while(true){
			String readUTF = dis.readUTF();
			System.out.println(readUTF);
		}
		
	}
	@Test
	public void test7() throws UnknownHostException, IOException, InterruptedException{
		Socket s = new Socket("localhost", 9090);
		OutputStream out = s.getOutputStream();
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
		PrintWriter pw = new PrintWriter(out);
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeUTF("���");
		dos.writeUTF("��Һ�");
		dos.flush();
		while(true){
			Thread.sleep(10000);
		}
	}
	
	@Test
	public void test8() {
		UUID uuid = new UUID(200, 200);
		String string = UUID.fromString("481a3376-5b98-406a-a254-357c2dad39ee").toString();
		System.out.println(string);
		System.out.println(UUID.randomUUID().toString());
	}
	
	public Object getMember(JsonObject jo, String name){
		JsonElement ele = jo.get(name);
		
		return null;
	}
	@Test
	public void test9(){
		String json = "{}";
	}
	
	@Test
	public void test0() throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = fac.newDocumentBuilder();
		doc = builder.parse("src/c3p0-config.xml");
		Element root = doc.getDocumentElement();
		NodeList nameConfig = root.getElementsByTagName("property");
	
		NodeList child = root.getChildNodes();
		for(int i = 0; i < nameConfig.getLength(); i++){
			Element e = (Element) nameConfig.item(i);
			NodeList property = e.getElementsByTagName("property");
			System.out.println(e.getAttribute("name"));
//			for(int j = 0; j < property.getLength(); j++){
//				Element ele = (Element) property.item(j);
//				System.out.println(" " + ele.getAttribute("name"));
//			}
		}
		
	}
	
	@Test
	public void test10() throws ClassNotFoundException, IOException{
		Properties p = new Properties();
		p.load(ClassLoader.getSystemResourceAsStream("c3p0.properties"));
		p.setProperty("c3p0.user", "ok");
		System.out.println(p.getProperty("c3p0.user"));
		System.out.println(p.getProperty("ok"));
		
	}
	@Test
	public void test11() throws ClassNotFoundException{
		
		Class<?> clazz = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
		Method[] methods = clazz.getMethods();
		for(Method m : methods){
			Class<?>[] ty = m.getParameterTypes();
			System.out.println(m.getName());
			for(Class c: ty){
				System.out.println(" " + c);
			}
		}
	}
	
	@Test
	public void test12(){
		String s = "c3p0.automaticTestTable";
		String[] split = s.split(".");
		String substring = s.substring(5);
		System.out.println(substring);
		System.out.println(split.length);
		System.out.println(s.split("."));
		
	}
	@Test
	public void test13(){
		System.out.println(UserKit.md5("root"));
	}
	
	@Test
	public void test14(){
		JsonObject jo = new JsonObject();
		Gson g = new Gson();
		Administrator a = new Administrator();
		a.setId(1);
		JsonObject j = g.toJsonTree(a).getAsJsonObject();
		System.out.println(j.toString());
		System.out.println(UserKit.randomname());
	}
	
	@Test
	public void test15(){
        File file = new File("HelloWorld.java");  
        String fileName = file.getName();  
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);  
        System.out.println(suffix);  
	}
	@Test
	public void test16() throws SQLException{
		String ADD_IMG = 
				"INSERT INTO good_image (img_url, img_name) VALUE (?,?)";
		int r = DBKit.update(ADD_IMG, "/A", "NAME"+ "_pic");
		System.out.println(r);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
