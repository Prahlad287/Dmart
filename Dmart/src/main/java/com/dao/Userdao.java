package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class Userdao {
	
	Connection connection;
	public Userdao(Connection con) {
		connection=con;
	}
	public Userdao(){
		
	}
	public boolean saveUser(ItemList list) {
		boolean f=false;
		try {
		PreparedStatement psmt1 = connection.prepareStatement("INSERT INTO items (item_code, item_name, item_price, item_quantity, total_price) VALUES (?, ?, ?, ?, ?)");

		
	//		PreparedStatement psmt1=connection.prepareStatement("insert into items values(?,?,?,?,?)");
			psmt1.setInt(1, list.itemCode);
			psmt1.setString(2, list.itemName);
			psmt1.setDouble(3, list.itemPrice);
			psmt1.setInt(4, list.itemQuantity);
			  double totalPrice =list.itemPrice * list.itemQuantity;
			  psmt1.setDouble(5, totalPrice);
			
			int count= psmt1.executeUpdate();
			
			if (count > 0) {
				System.out.println("data saved successfully");
				f = true;
			} else {
				System.out.println("data not saved");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
		
	}

}
