package acmd.terminal.listeners;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.thread.Controller;

/**
 * Application Lifecycle Listener implementation class MainListener
 *
 */
@WebListener
public class MainListener implements ServletContextListener, HttpSessionListener {

	private ExecutorService pool;
	private ServerSocket ss;
	/**
	 * Default constructor.
	 * @throws IOException 
	 */
	public MainListener() throws IOException {
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {

	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	private static String realPath;
	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		realPath = arg0.getServletContext().getRealPath("/");
	}
	
	public static String getRealPath(){
		return realPath;
	}

}
