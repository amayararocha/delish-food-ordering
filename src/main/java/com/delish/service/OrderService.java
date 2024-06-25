package com.delish.service;

import java.util.List;

import com.delish.model.Order;
import com.delish.model.User;
import com.delish.request.OrderRequest;

public interface OrderService {
	
	public Order createOrder(OrderRequest order, User user);
	
	public Order updateOrder(Long orderId, String orderStatus) throws Exception;
	
	public void deleteOrder(Long orderId) throws Exception;
	
	public List<Order> getUserOrder(Long userId) throws Exception;
	
	public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;

}