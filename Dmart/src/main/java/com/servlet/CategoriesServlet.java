package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.helper.ConnectionProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CategoriesServlet")
public class CategoriesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setContentType("image/jpg");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Items</title></head><body>");

        // Connect to your database and fetch items from the item table
        try (Connection con = ConnectionProvider.getConnection()) {
            PreparedStatement psmtQuantity = con.prepareStatement("SELECT item_name FROM items");
            ResultSet rsQuantity = psmtQuantity.executeQuery();

            out.println("<h2>Items</h2>");
            out.println("<ul>");
            // Print out the items
            while (rsQuantity.next()) {
                String itemName = rsQuantity.getString("item_name");
                out.println("<li>" + itemName + "</li>");
            }
            out.println("</ul>");

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

