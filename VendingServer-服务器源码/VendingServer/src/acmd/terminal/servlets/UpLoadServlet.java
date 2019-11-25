package acmd.terminal.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.JsonObject;

import acmd.terminal.kits.DBKit;
import acmd.terminal.kits.UserKit;
import acmd.terminal.listeners.MainListener;

/**
 * Servlet implementation class UpLoadServlet
 */
@WebServlet("/upload")
public class UpLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpLoadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	public static final String ADD_IMG = 
			"INSERT INTO good_image (img_url, img_name) VALUE (?,?)";
	public static final String AN_IMG =
			"SELECT img_id FROM good_image WHERE img_name = ?";
	private static final String a=null;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json");
		JsonObject resp = new JsonObject();
		DiskFileItemFactory fac = new DiskFileItemFactory();
		fac.setSizeThreshold(4096);
		ServletFileUpload up = new ServletFileUpload(fac);
		try {
			ArrayList<FileItem> list = (ArrayList<FileItem>) up.parseRequest(request);
			System.out.println(list.size());
			String path = MainListener.getRealPath();
			for (FileItem fi : list) {
				InputStream in = fi.getInputStream();
				String name = UserKit.randomname();
				String partpath = "/IMG-RES/" + name + "." + fileType(fi.getName());
				File nativeFile = new File(path + partpath);
				if (nativeFile.exists()) {
					resp.addProperty("result", false);
					return;
				}
				nativeFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(nativeFile);
				byte buffer[] = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				resp.addProperty("result", true);
				resp.addProperty("path", "/VendingServer" + partpath);
			}
		} catch (FileUploadException e) {
			resp.addProperty("result", false);
			e.printStackTrace();
		}	
		System.out.println(resp.toString());
		response.getWriter().write(resp.toString());
	}

	public String fileType(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;

	}

}
