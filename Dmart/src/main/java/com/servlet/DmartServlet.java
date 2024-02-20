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
        
        // Retrieving form parameters
        int itemCode = Integer.parseInt(request.getParameter("itemCode"));
        String itemName = request.getParameter("itemName");
        double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
        int itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
        
        // You can perform further operations with the retrieved data, such as inserting into a database
        // For now, let's just print the retrieved data
//        out.println("<h2>Item Details:</h2>");
//        out.println("<p>Item Code: " + itemCode + "</p>");
//        out.println("<p>Item Name: " + itemName + "</p>");
//        out.println("<p>Item Price: " + itemPrice + "</p>");
//        out.println("<p>Item Quantity: " + itemQuantity + "</p>");
        ItemList list=new ItemList(itemCode, itemName, itemPrice, itemQuantity);
        Userdao userdao;
		try {
			userdao = new Userdao(ConnectionProvider.getConnection());
			 if (userdao.saveUser(list)) {
		    		out.println("successfully write");
				}else {
					out.println("not writed");
				}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		
    }
}
 