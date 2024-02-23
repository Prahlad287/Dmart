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

@WebServlet("/FetchItemsServlet")
public class FetchItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String firstLetter = request.getParameter("firstLetter");
        try (Connection con = ConnectionProvider.getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT item_name FROM items WHERE item_name LIKE ?")) {
            statement.setString(1, firstLetter + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                StringBuilder suggestionsHtml = new StringBuilder();
                while (resultSet.next()) {
                    String itemName = resultSet.getString("item_name");
                    suggestionsHtml.append("<div>").append(itemName).append("</div>");
                }
                out.println(suggestionsHtml.toString());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
}
