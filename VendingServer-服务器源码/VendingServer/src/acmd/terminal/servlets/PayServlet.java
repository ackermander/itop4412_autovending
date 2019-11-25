package acmd.terminal.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import acmd.terminal.kits.thread.ControllerWeb;
import acmd.terminal.request.PaymentRequest;

/**
 * Servlet implementation class PayServlet
 */
@WebServlet("/pay")
public class PayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final HashMap<Integer, String> mapping = new HashMap<>();

	public void setMapping(int i, String s) {

	}

	public PayServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String qr = request.getParameter("qr");
		String time = request.getParameter("ex-time");
		if (qr != null && time != null) {
			request.getRequestDispatcher("payconfirm.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("pay posted");
		
		String checkName = request.getParameter("check-name");
		String action = request.getParameter("action");
		int version = Integer.parseInt(request.getParameter("version"));
		int position = Integer.parseInt(request.getParameter("position"));
		String uuid = request.getParameter("terminal-uuid");
		
		JsonObject data = new JsonObject();
		data.addProperty("check-name", checkName);
		data.addProperty("version", version);
		data.addProperty("action", action);
		data.addProperty("uuid", uuid);
		data.addProperty("position", position);
		System.out.println(data.toString());
		PaymentRequest pay = new PaymentRequest();
		JsonObject ck = new JsonObject();
		try {
			ck = pay.confirmedCheck(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int r = ck.get("r").getAsInt();
		System.out.println(ck.toString());
		if(r == 1){
			ControllerWeb.sendMessage(uuid, ControllerWeb.ctrltml(ck.get("x").getAsInt(),
					ck.get("y").getAsInt(), 
					ControllerWeb.VERSION, 
					ControllerWeb.ACTION_DEAL));
			response.sendRedirect("payfinish.jsp");
		}
	}

}
