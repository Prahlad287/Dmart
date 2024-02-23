package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Userdao {

	Connection connection;

	public Userdao(Connection con) {
		connection = con;
	}

	public Userdao() {

	}

	public boolean saveUser(ItemList list) {
		boolean f = false;
		try {
			// Check if the item_code already exists in the table
			PreparedStatement psmtSelect = connection
					.prepareStatement("SELECT item_name, item_quantity, total_price FROM items WHERE item_code = ?");
			psmtSelect.setInt(1, list.itemCode);
			ResultSet resultSet = psmtSelect.executeQuery();

			if (resultSet.next()) {
				// Item_code exists
				String itemName = resultSet.getString("item_name");
				int currentQuantity = resultSet.getInt("item_quantity");
				double totalPrice = resultSet.getDouble("total_price");

				if (itemName.equals(list.itemName)) {
					// Item_name matches, update item_quantity and total_price
					int newQuantity = currentQuantity + list.itemQuantity;
					totalPrice += list.itemPrice * list.itemQuantity;

					PreparedStatement psmtUpdate = connection.prepareStatement(
							"UPDATE items SET item_quantity = ?,item_price =?, total_price = ? WHERE item_code = ?");
					psmtUpdate.setInt(1, newQuantity);
					psmtUpdate.setDouble(2, list.itemPrice);
					psmtUpdate.setDouble(3, totalPrice);
					psmtUpdate.setInt(4, list.itemCode);

					int updateCount = psmtUpdate.executeUpdate();

					if (updateCount > 0) {
						System.out.println("Quantity and total price updated successfully");
						f = true;
					} else {
						System.out.println("Failed to update quantity and total price");
					}
				} else {
					// Item_name does not match
					System.out.println("Item name mismatch");
				}
			} else {
				// Item_code does not exist, insert a new record
				double totalPrice = list.itemPrice * list.itemQuantity;

				PreparedStatement psmtInsert = connection.prepareStatement(
						"INSERT INTO items (item_code, item_name, item_price, item_quantity, total_price) VALUES (?, ?, ?, ?, ?)");
				psmtInsert.setInt(1, list.itemCode);
				psmtInsert.setString(2, list.itemName);
				psmtInsert.setDouble(3, list.itemPrice);
				psmtInsert.setInt(4, list.itemQuantity);
				psmtInsert.setDouble(5, totalPrice);

				int insertCount = psmtInsert.executeUpdate();

				if (insertCount > 0) {
					System.out.println("New record inserted successfully");
					f = true;
				} else {
					System.out.println("Failed to insert new record");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

}
