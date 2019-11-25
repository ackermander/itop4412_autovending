package acmd.terminal.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import acmd.terminal.common.Administrator;
import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;

/**
 * Servlet implementation class SigninServlet
 */
@WebServlet("/signin")
public class SigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SigninServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("get NOTHING. post SOMETHING.");
	}

	private static final String AD_QUERY = "SELECT ad_id, ad_name, ad_auth FROM administrators WHERE ad_usr = ? AND ad_psw = ?";
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String psw = null;
		String usr = null;
		String pswchk;
		try{
			usr = request.getParameter("username");
			psw = request.getParameter("password");
			pswchk = UserKit.md5(psw);
			ResultSet r = DBKit.query(AD_QUERY, usr, pswchk);
			if(r.next()){
				Administrator ad = new Administrator();
				ad.setId(r.getInt("ad_id"));
				ad.setSigndate(new Date());
				ad.setUsr(usr);
				request.getSession().setAttribute("admin", ad);
				response.sendRedirect("admin");
			}else{
				response.sendRedirect("adsign?error=pswornon");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			response.sendRedirect("adsign");
		}
		
	}

}
