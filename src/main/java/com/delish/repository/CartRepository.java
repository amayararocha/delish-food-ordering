package com.delish.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delish.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

	public Cart findByCustumerId(Long userId); 
}
