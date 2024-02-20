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

@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        out.println("Item Name: <input type='text' name='customerName'><br>");
        out.println("Item Price: <input type='number' name='itemCode'><br>");
      String customerName = request.getParameter("customerName");
      String itemCode = request.getParameter("itemCode");

        Connection con = null;
		try {
			con = ConnectionProvider.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT item_name, item_price FROM items WHERE item_code = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, itemCode);
            resultSet = statement.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Buy Items</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<h1>Buy Items</h1>");
            out.println("<form action='GenerateBillServlet' method='post'>");
            out.println("<input type='hidden' name='customerName' value='" + customerName + "'>");
            out.println("<input type='hidden' name='itemCode' value='" + itemCode + "'>");

            if (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                double itemPrice = resultSet.getDouble("item_price");

                out.println("Item Name: <input type='text' name='itemName' value='" + itemName + "' readonly><br>");
                out.println("Item Price: <input type='number' name='itemPrice' value='" + itemPrice
                        + "' step='0.01' readonly><br>");
            } else {
                out.println("Item Name: <input type='text' name='itemName' readonly><br>");
                out.println("Item Price: <input type='number' name='itemPrice' step='0.01' readonly><br>");
            }

            out.println("Total Price: <input type='number' name='totalPrice' step='0.01' required><br>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
