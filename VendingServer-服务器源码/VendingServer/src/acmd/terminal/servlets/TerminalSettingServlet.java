package acmd.terminal.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import acmd.terminal.request.ResourceRequest;

/**
 * Servlet implementation class TerminalSettingServlet
 */
@WebServlet("/terminal-setting")
public class TerminalSettingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TerminalSettingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get");
		response.getWriter().append("do NOT use type get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("terminal setting");
		String parameter = request.getParameter("data");
		System.out.println(parameter);
		ResourceRequest res  = ResourceRequest.ResourceBuilder();
		JsonObject result = null;;
		try {
			result = res.requestAnalyse(parameter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("terminal" + result);
		response.getWriter().write(result.toString());
	}

}
