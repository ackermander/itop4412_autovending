package acmd.terminal.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class OpenServlet
 */
@WebServlet("/open")
public class OpenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("open");
		Map<String, String[]> parameterMap = request.getParameterMap();
		for(Map.Entry<String, String[]> p: parameterMap.entrySet()){
			System.out.println(p.getKey());
			for(String v: p.getValue()){
				System.out.println("  " + v);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("open");
		Map<String, String[]> parameterMap = request.getParameterMap();
		for(Map.Entry<String, String[]> p: parameterMap.entrySet()){
			System.out.println(p.getKey());
			for(String v: p.getValue()){
				System.out.println("  " + v);
			}
		}
	}

}
