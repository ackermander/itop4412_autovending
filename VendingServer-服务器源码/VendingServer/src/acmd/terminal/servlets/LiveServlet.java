package acmd.terminal.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import acmd.terminal.kits.ResponseKit;
import acmd.terminal.request.LiveTerminalRequest;

/**
 * Servlet implementation class LiveServlet
 */
@WebServlet("/islive")
public class LiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LiveServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String parameters = request.getParameter("data");
		System.out.println("Live Servlet: " + parameters);
		LiveTerminalRequest liveRequest = LiveTerminalRequest.LiveCommunicate();
		int isLive = liveRequest.liveAnalyse(parameters);
		JsonObject JSONResponse = ResponseKit.liveResponseV1(isLive);
		response.getWriter().write(JSONResponse.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
