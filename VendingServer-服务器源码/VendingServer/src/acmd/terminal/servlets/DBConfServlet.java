package acmd.terminal.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import acmd.terminal.common.DBConf;
import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;
import acmd.terminal.listeners.MainListener;

/**
 * Servlet implementation class DBConfServlet
 */
@WebServlet("/dbconf")
public class DBConfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBConfServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("GET .");
	}
	private static final String DELETE_DATABASE = "DROP DATABASE new_schema";
	private static final String CREATE_DATABASE = "CREATE DATABASE new_schema";
	private static final String ADD_ADMIN = "INSERT INTO administrators (ad_name, ad_auth, ad_usr, ad_psw) VALUES ('admin', 0, 'admin', '63A9F0EA7BB98050796B649E85481845')";
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson g = new Gson();
		DBConf c = new DBConf(0);
		String parVersion = request.getParameter("version");
		if(parVersion == null) {
			DBConf c1 = new DBConf(-1);
			response.getWriter().write(g.toJson(c1));
			return ;
		}
		int v = Integer.parseInt(parVersion);
		switch(v){
		case 0:{
			String url = request.getParameter("url");
			String usr = request.getParameter("usr");
			String psw = request.getParameter("psw");
			String conf = request.getParameter("conf");
			Properties p = new Properties();
			p.put("c3p0.jdbcUrl", UserKit.toJdbcUrl(url, "sys"));
			p.put("c3p0.driverClass", "com.mysql.jdbc.Driver");
			p.put("c3p0.user", usr);
			p.put("c3p0.password", psw);
			boolean suc = DBKit.updateConfig(p);
			System.out.println("dbconf: " + suc);
			if(suc){
				//等于0 的时候, 就删除数据库, 并生成数据库.
				if(conf.equals("0")){
					try {
						System.out.println("delete database");
						DBKit.update(DELETE_DATABASE);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					try {
						System.out.println(CREATE_DATABASE);
						DBKit.update(CREATE_DATABASE);
						p.replace("c3p0.jdbcUrl", UserKit.toJdbcUrl(url, "new_schema"));
						System.out.println(p.get("c3p0.jdbcUrl"));
						suc = DBKit.updateConfig(p);
						System.out.println(suc);
						if(suc){
							DBKit.runscript();
							DBKit.update(ADD_ADMIN);
							c.setOut("配置成功, 后台管理用户默认用户名: admin, 密码: root");
						}else{
							c.setOut("创建数据库的时候出现了一些问题");
						}	
					} catch (SQLException e) {}
				}else{
					p.replace("c3p0.jdbcUrl", UserKit.toJdbcUrl(url, "new_schema"));
					DBKit.updateConfig(p);
					c.setOut("修改成功");
				}
			}else{
				c.setOut("配置出现错误, 保留原有配置");
			}
			break;
		}
		case 1:{
			try {
				ResultSet query = DBKit.query("SELECT * FROM administrators");
				query.next();
				System.out.println(query.getObject(1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		response.getWriter().write(g.toJson(c));
	}
	
	

}
