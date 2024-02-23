package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.helper.ConnectionProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HashMap<Integer, ArrayList<Object>> hashMap = new HashMap<>(); // HashMap to store item details

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Buy itemsList</title>");
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

        String customerName = request.getParameter("customerName");
        String itemCodeStr = request.getParameter("itemCode");
        int itemCode = Integer.parseInt(itemCodeStr);
        String quantityStr = request.getParameter("itemquentity");
        int quantity = Integer.parseInt(quantityStr);

        String itemName = null;
        double itemPrice = 0;
        Connection con = null;
		try {
			con = ConnectionProvider.getConnection();
		
        PreparedStatement psmtQuantity = con.prepareStatement("SELECT item_quantity FROM items WHERE item_code = ?");
        psmtQuantity.setInt(1, itemCode);
        ResultSet rsQuantity = psmtQuantity.executeQuery();

        int currentQuantity = 0;

        if (rsQuantity.next()) {
            currentQuantity = rsQuantity.getInt("item_quantity");
        }

        if (currentQuantity <= 0) {
            // If the current quantity is less than or equal to 0, the item is out of stock
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Out of Stock</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Out of Stock</h1>");
            out.println("<p>The item with code " + itemCode + " is out of stock.</p>");
            out.println("<a href='buy.html'>Back</a>");
            out.println("</body>");
            out.println("</html>");
            return;
        }else {
		
        // Retrieve item details from the database
        	if (currentQuantity>=quantity) {
				
			
            PreparedStatement psmt = con.prepareStatement("SELECT item_name, item_price FROM items WHERE item_code = ?");
            psmt.setInt(1, itemCode);
            ResultSet resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                itemName = resultSet.getString("item_name");
                itemPrice = resultSet.getDouble("item_price");
            } else {
                // If the item is not available in the database, display a message and return
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>No Item Available</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>No Item Available</h1>");
                out.println("<p>The item with code " + itemCode + " is not available.</p>");
                out.println("<a href='buy.html'>Back</a>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
           

        double totalPrice = itemPrice * quantity;

        // Store item details in the HashMap
        if (hashMap.containsKey(itemCode)) {
            // If the item code already exists in the HashMap, update the quantity and total price
            ArrayList<Object> existingItem = hashMap.get(itemCode);
            int existingQuantity = (int) existingItem.get(3);
            int newQuantity = existingQuantity + quantity;
            existingItem.set(3, newQuantity); // Update quantity
            double existingTotalPrice = (double) existingItem.get(4);
            double newTotalPrice = existingTotalPrice + totalPrice;
            existingItem.set(4, newTotalPrice); // Update total price
        } else {
            // If the item code doesn't exist in the HashMap, add a new entry
            ArrayList<Object> itemDetails = new ArrayList<>();
            itemDetails.add(itemCode);
            itemDetails.add(itemName);
            itemDetails.add(itemPrice);
            itemDetails.add(quantity);
            itemDetails.add(totalPrice);
            hashMap.put(itemCode, itemDetails);
            HttpSession session=request.getSession();
            session.setAttribute("hash", hashMap);
        }

        // Display the form for adding items
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Buy Items</title>");
        out.println("<style>");
        out.println("table { margin: auto; }");
        out.println("body { text-align: center; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Buy Items</h1>");

        // Display the table of items added so far
        out.println("<table border='1'>");
        out.println("<tr><th>Item Code</th><th>Item Name</th><th>Item Price</th><th>Quantity</th><th>Total Price</th></tr>");
        for (Map.Entry<Integer, ArrayList<Object>> entry : hashMap.entrySet()) {
            ArrayList<Object> values = entry.getValue();
            out.println("<tr>");
            out.println("<td>" + values.get(0) + "</td>");
            out.println("<td>" + values.get(1) + "</td>");
            out.println("<td>" + values.get(2) + "</td>");
            out.println("<td>" + values.get(3) + "</td>");
            out.println("<td>" + values.get(4) + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        // Link to add more items
      out.println("<br>");
        out.println("<input type='button' value='Add More' onclick=\"window.location.href='buy.html'\">");

        // Link to generate bill
        out.println("<input type='button' value='Generate Bill' onclick=\"window.location.href='GenerateBillServlet'\">");
       
        out.println("</body>");
        out.println("</html>");
    } 
        	else {
        		 out.println("<h1>Out of Stock</h1>");
                 out.println("<p>The item with code " + itemCode + " is out of stock.</p>");
    }
        }
       
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
		 }
		
    
}
