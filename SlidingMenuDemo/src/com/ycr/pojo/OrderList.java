package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2636624994714183405L;
	private int type;//订单类型1=历史订2=当天订单
	private List<OrderInfo> orders;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<OrderInfo> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderInfo> orders) {
		this.orders = orders;
	}
	
}
