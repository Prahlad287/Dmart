package com.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.helper.ConnectionProvider;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GenerateBillServlet")
public class GenerateBillServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		HttpSession session = request.getSession();
		Map<Integer, ArrayList<Object>> hashMap = (Map<Integer, ArrayList<Object>>) session.getAttribute("hash");

		if (hashMap == null || hashMap.isEmpty()) {
			out.println("<h1>No items in the cart to generate a bill.</h1>");
			return;
		}

		try {
			Connection con = ConnectionProvider.getConnection();
			double totalBillAmount = 0;

			// Iterate through the items in the cart
			for (ArrayList<Object> itemDetails : hashMap.values()) {
				int itemCode = (int) itemDetails.get(0);
				int quantity = (int) itemDetails.get(3);
				double totalPrice = (double) itemDetails.get(4);

				// Fetch the current item quantity and total price from the database
				PreparedStatement psmtQuantity = con
						.prepareStatement("SELECT item_quantity, total_price FROM items WHERE item_code = ?");
				psmtQuantity.setInt(1, itemCode);
				ResultSet rsQuantity = psmtQuantity.executeQuery();

				int currentQuantity = 0;
				double currentTotalPrice = 0;

				if (rsQuantity.next()) {
					currentQuantity = rsQuantity.getInt("item_quantity");
					currentTotalPrice = rsQuantity.getDouble("total_price");
				}

				// Calculate the updated quantity and total price after the sale
				int updatedQuantity = currentQuantity - quantity;
				double updatedTotalPrice = currentTotalPrice - totalPrice;

				// Update item quantity and total price in the database
				PreparedStatement psmtUpdate = con
						.prepareStatement("UPDATE items SET item_quantity = ?, total_price = ? WHERE item_code = ?");
				psmtUpdate.setInt(1, updatedQuantity);
				psmtUpdate.setDouble(2, updatedTotalPrice);
				psmtUpdate.setInt(3, itemCode);
				psmtUpdate.executeUpdate();

				totalBillAmount += totalPrice;
			}

			// Generate the bill
			out.println("<h1>Bill Generated Successfully</h1>");
			out.println("<h2>Total Bill Amount: $" + totalBillAmount + "</h2>");

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
			   
			    // Create PDF document for each item
			    Document document = new Document(PageSize.A4);
		        String filePath = "C:\\Users\\DELL\\git\\dmart\\Dmart\\CreatePdf.pdf";
		        try {
	           
		            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		            document.open();
		            
		            Image img = Image.getInstance(getServletContext().getRealPath("/img/download.png"));
		            img.scaleAbsolute(100f, 90f); // Set image size as needed
		            document.add(img);

		            // Create PdfContentByte to add watermark
		            PdfContentByte canvas = writer.getDirectContentUnder();

		            // Set font and color for the watermark
		            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		            Font font = new Font(baseFont, 48, Font.BOLD);
		            canvas.setFontAndSize(baseFont, 48);
		            canvas.setColorFill(BaseColor.LIGHT_GRAY);

		            // Add watermark text diagonally across the page
		            for (int i = 0; i < 10; i++) { // Loop to cover the entire page
		                canvas.beginText();
		                canvas.showTextAligned(Element.ALIGN_CENTER, "Dmart", 297, 421, 45);
		                canvas.endText();
		            }

		           
		            // Add billing information
		            Paragraph billingInfo = new Paragraph("Billing Information");
		            billingInfo.setAlignment(Element.ALIGN_CENTER);
		            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
		            billingInfo.setFont(boldFont);
		            billingInfo.setSpacingAfter(10f);
		            document.add(billingInfo);
		        	        
		            PdfPTable table = new PdfPTable(5); // Create a table with 5 columns		          
		            // Add table headers
		            table.addCell("Item Code");
		            table.addCell("Item Name");
		            table.addCell("Item Price");
		            table.addCell("Item Quantity");
		            table.addCell("Total Price");

		            double totalPrice = 0; // Variable to calculate the total price
		            
		           
		            for (Map.Entry<Integer, ArrayList<Object>> entry2 : hashMap.entrySet()) {
		                ArrayList<Object> values2 = entry2.getValue();
		                // Add data to the table for each item
		                for (Object value : values2) {
		                    table.addCell(value.toString());
		                }
		                // Add the total price for the current item to the total price
		                totalPrice += Double.parseDouble(values2.get(4).toString());
		                
		            }
		            
		            
		            double totalgst=((totalPrice*5)/100);
		            table.addCell("SGST");
		            table.addCell(""); // Empty cell for Item Name column
		            table.addCell(""); // Empty cell for Item Price column
		            table.addCell(""); // Empty cell for Item Quantity column
		            table.addCell(String.valueOf(totalgst));
		            
		            table.addCell("CGST");
		            table.addCell(""); // Empty cell for Item Name column
		            table.addCell(""); // Empty cell for Item Price column
		            table.addCell(""); // Empty cell for Item Quantity column
		            table.addCell(String.valueOf(totalgst));
		            // Add a row for the total price
		            table.addCell("Total");
		            table.addCell(""); // Empty cell for Item Name column
		            table.addCell(""); // Empty cell for Item Price column
		            table.addCell(""); // Empty cell for Item Quantity column
		            table.addCell(String.valueOf(totalPrice+(totalgst*2))); // Total Price cell
		            
		            table.setSpacingBefore(10f); // You
		            document.add(table); // Add the table to the PDF document
		            
		           

		        } catch (FileNotFoundException | DocumentException e) {
		            e.printStackTrace();
		        } finally {
		            if (document != null) {
		                document.close();
		            }
		        }
		        hashMap.clear();
		        session.invalidate();
		        out.println("<br>");
		        out.println("<input type='button' value='Home' onclick=\"window.location.href='front.html'\">");
		      
		        out.println("<button onclick=\"window.open('print', '_blank')\">Print</button>");

			 

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
