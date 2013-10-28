package com.prueba.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.prueba.util.Constants;

@WebServlet(
		name="ImageServlet",
		urlPatterns={"/servlet/images/*"})
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private  Log logger = LogFactory.getLog(this.getClass());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		FileInputStream inputStream=null;
		try
		{
			String uploadDirectoryLocal=getServletContext().getRealPath("resources");
			String uploadDirectoryExternal = Constants.getPathImages();	
			String nombre = req.getParameter("nombre");
			String dir = "";
			if(nombre == null || nombre.trim().length() == 0)
			{
				nombre = "anonymous.gif";
				dir = uploadDirectoryLocal + File.separator + "images" + File.separator + nombre;
			}
			else
			{
				dir = uploadDirectoryExternal + File.separator + nombre;
			}
			inputStream = new FileInputStream(dir);
			byte content[] = new byte[inputStream.available()];
			inputStream.read(content);
			//resp.setHeader("Cache-Control", "max-age=31536000");
			resp.getOutputStream().write(content);
			inputStream.close();
		}catch (Exception e) {
			//No existe la imagen
			logger.error("Error.The image doesn't exist.Error:"+e);
		}finally{
			   if (inputStream != null) {
				     try {
				    	 inputStream.close();
				     } catch (Exception e) {
				    	 logger.error("Exception e:"+e);
				     }
			   }
		} 
	}

}
