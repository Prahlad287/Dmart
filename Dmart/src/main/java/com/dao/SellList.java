package com.dao;

public class SellList {
	public int itemcode;
	String itemName;
	double itemPrice;
	int itemquentity;
	double totalPrice;
	public SellList(int itemcode, String itemName, double itemPrice, int itemquentity, double totalPrice) {
		super();
		this.itemcode = itemcode;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemquentity = itemquentity;
		this.totalPrice = totalPrice;
	}
	public void setItemQuantity(int qty) {
		// TODO Auto-generated method stub
		
	}
	
	public int getItemCode() {
		// TODO Auto-generated method stub
		return itemcode;
	}
	public int getItemQuantity() {
		// TODO Auto-generated method stub
		return itemquentity;
	}
	public double getItemPrice() {
		// TODO Auto-generated method stub
		return itemPrice;
	}
	public String getItemName() {
		return itemName;
		
	}
	public double getTotalPrice() {
		double total=getItemQuantity()*getItemPrice();
		return total;
	}

}
