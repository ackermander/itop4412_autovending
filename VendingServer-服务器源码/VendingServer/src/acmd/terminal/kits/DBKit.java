package acmd.terminal.kits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import acmd.terminal.kits.thread.ControllerWeb;
import acmd.terminal.listeners.MainListener;

public class DBKit {
	private final static ComboPooledDataSource pool = new ComboPooledDataSource();
	private static Connection connection = null;
	private static final Properties properties = pool.getProperties();

	static {
	}

	private Connection getConnection() throws SQLException {
		Connection connection = pool.getConnection();
		DatabaseMetaData metaData = connection.getMetaData();
		return connection;
	}

	public static int update(String sql, Object... os) throws SQLException {
		if (connection == null||connection.isClosed()) {
			connection = pool.getConnection();
		}
		int key = 0;
		PreparedStatement p = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		if (p == null) {
			throw new SQLException("No statement working");
		}
		int i = 0;
		for (Object o : os) {
			i++;
			p.setObject(i, o);
		}
		int result = p.executeUpdate();
		ResultSet r = p.getGeneratedKeys();
		if(r.next()){
			key = r.getInt(1);
		}
		p.close();
		return key;
	}

	public static ResultSet query(String sql, Object... os) throws SQLException {
		if (connection == null||connection.isClosed() ) {
			connection = pool.getConnection();
		}
		PreparedStatement p = connection.prepareStatement(sql);
		if (p == null) {
			throw new SQLException("No statement working");
		}
		ResultSet r = null;
		int i = 0;
		for (Object o : os) {
			i++;
			p.setObject(i, o);
		}
		r = p.executeQuery();
		if (r == null) {
			throw new SQLException("Result Set is not queried");
		}

		return r;
	}

	public static boolean exist(String sql, Object... os) throws SQLException {
		if (connection == null||connection.isClosed()) {
			connection = pool.getConnection();
		}
		ResultSet r = query(sql, os);
		if (r.next()) {
			r.getStatement().close();
			r.close();
			return true;
		}
		r.getStatement().close();
		r.close();
		return false;
	}
	
	public static final void callForpsi(Object ... os) throws SQLException{
		if (connection == null||connection.isClosed()) {
			connection = pool.getConnection();
		}
		String call = "{CALL position_pdu(?,?,?,?,?)}";
		CallableStatement cal = connection.prepareCall(call);
		int i = 0;
		for(Object o : os){
			i++;
			cal.setObject(i, o);
		}
		cal.execute();
		cal.close();
	}

	public static void transaction(String... sqls) {
		Connection c = null;
		Statement s = null;
		try {
			c = pool.getConnection();
			s = c.createStatement();
			for (String sql : sqls) {
				s.execute(sql);
			}
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
				if (s != null) {
					s.close();
				}
				if (c != null) {
					c.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static boolean tableExist(String table) {

		return false;
	}

	public static boolean updateConfig(Properties p) throws FileNotFoundException {
//		try {
//			ControllerWeb.closeAllSession();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		Properties fp = new Properties();
		Properties cp = (Properties) p.clone();
		String path = MainListener.getRealPath() + "/WEB-INF/classes/c3p0.properties";
		File f = new File(path);
		FileInputStream fin = null;
		try {
			if (f.exists()) {
				fin = new FileInputStream(f);
				fp.load(fin);
				fp.putAll(cp);
				cp = fp;
				fin.close();
			}
		} catch (IOException e4) {
			e4.printStackTrace();
		}
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
		setProperties2Class(cp, clazz);
		try {
			if (connection != null)
				connection.close();
			connection = pool.getConnection();
			if (!f.exists()) {
				System.out.println("create file");
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileOutputStream out = new FileOutputStream(f);
			cp.store(out, "resotre");
			out.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void setProperties2Class(Properties cp, Class<?> clazz) {
		Set<Entry<Object, Object>> set = cp.entrySet();
		Iterator<Entry<Object, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> next = it.next();
			String key = (String) next.getKey();
			System.out.println(next.getValue());
			String unname = key.substring(5);
			String name = captureName(unname);
			try {
				Method method = clazz.getMethod("set" + name, int.class);
				method.invoke(pool, Integer.parseInt((String) next.getValue()));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				try {
					Method method = clazz.getMethod("set" + name, String.class);
					method.invoke(pool, (String) next.getValue());
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e1) {
					try {
						Method method = clazz.getMethod("set" + name, boolean.class);
						method.invoke(pool, Boolean.parseBoolean((String) next.getValue()));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e2) {
					}
				}
			}
		}
	}

	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}
	
	public static void runscript(){
		String path = MainListener.getRealPath() + "/WEB-INF/SCRIPT";
		System.out.println(path);
		File f = new File(path);
		if(f.isDirectory()){
			System.out.println("folder");
			File[] fs = f.listFiles();
			for(File script: fs){
				System.out.println(script);
				String sc = "" ;
				try {
					FileInputStream fin = new FileInputStream(script);
					int c = 0;
					while((c=fin.read()) != -1){
						sc += (char) c;
					}
					fin.close();
					update(sc);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
