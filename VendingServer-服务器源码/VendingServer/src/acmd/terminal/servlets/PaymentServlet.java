package acmd.terminal.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import acmd.terminal.request.PayQRCodeRequest;

/**
 * Servlet implementation class PaymentServlet
 */
@WebServlet("/get-payment-code")
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("do NOT use type get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data = request.getParameter("data");
		System.out.println("data " + data);
		JsonObject respText = null;
		try {
			respText = PayQRCodeRequest.createQRCode(data);
		} catch (SQLException e) {
			respText = new JsonObject();
			respText.addProperty("error", "error");
			e.printStackTrace();
		}
		System.out.println("192.168.1.101:8080/VendingServer/pay?qr=" + respText.get("qr").getAsString()
				+ "&ex-time=" + respText.get("create_time").getAsLong() + 
				"&uuid="+ respText.get("uuid").getAsString() + "&position=" + respText.get("position").getAsInt());
		response.getWriter().append(respText.toString());
	}

}
