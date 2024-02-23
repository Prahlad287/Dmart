package com.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/print")
public class ReadPdfServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the content type to application/pdf
        response.setContentType("application/pdf");

        // Get the path to the PDF file
        String filePath = "C:\\Users\\DELL\\git\\dmart\\Dmart\\CreatePdf.pdf";

        try (FileInputStream inputStream = new FileInputStream(filePath);
             OutputStream outputStream = response.getOutputStream()) {
            // Copy the content of the PDF file to the response output stream
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("success");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
            response.getWriter().write("Error reading PDF file.");
        }
    }
}
