package acmd.terminal.filters;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import acmd.terminal.common.Administrator;
import acmd.terminal.kits.DBKit;
import acmd.terminal.listeners.MainListener;

/**
 * Servlet Filter implementation class AdminFilter
 */
@WebFilter("/admin")
public class AdminFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AdminFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession s = req.getSession();
		Administrator ad = (Administrator) s.getAttribute("admin");
		String path = MainListener.getRealPath() + "/WEB-INF/classes/c3p0.properties";
		File config = new File(path);
		// 如果配置文件存在, 至少说明不是第一次打开服务器,
		// 如果配置文件不存在, 说明不能与数据库连接, 所以需要建立与数据库的连接, 并且会造成删除数据库
		if (config.exists()) {
			try {
				DBKit.query("select * from administrators where ad_id = 1");
				s.setAttribute("conf", 1);
				if (ad == null) {
					resp.sendRedirect("adsign");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				s.setAttribute("conf", 0);
			}
		} else {
			s.setAttribute("conf", 0);// 表示没有数据库配置
		}

		// pass the request along the filter chain
		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
