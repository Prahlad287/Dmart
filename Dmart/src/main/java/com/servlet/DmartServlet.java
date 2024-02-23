package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


import com.dao.ItemList;
import com.dao.Userdao;
import com.helper.ConnectionProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DmartServlet")
public class DmartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Background Image Servlet</title>");
        out.println("<style>");
        out.println("body {");
        out.println("    background-image: url('img/Dmart.jpg');");
        out.println("    background-size: cover;");
        out.println("    background-repeat: no-repeat;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("</body>");
        out.println("</html>");
        // Retrieving form parameters
        int itemCode = Integer.parseInt(request.getParameter("itemCode"));
        String itemName = request.getParameter("itemName");
        double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
        int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
        
     
        ItemList list=new ItemList(itemCode, itemName, itemPrice, itemQuantity);
        Userdao userdao;
		try {
			userdao = new Userdao(ConnectionProvider.getConnection());
			 if (userdao.saveUser(list)) {
				 
				out.println("<h1 style='text-align: center;'>successfully write</h1>");		    		
		    	out.println("<br>");
		        out.println("<a style='text-align: center; display: block; margin: auto;' href='front.html'>Home</a>");
				}else {
					out.println("not writed");
				}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		
    }
}
 