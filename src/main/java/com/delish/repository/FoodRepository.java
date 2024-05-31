package com.delish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delish.model.Food;

public interface FoodRepository extends JpaRepository<Food, Long>{

	List<Food> findByRestaurantId(Long restaurantId);
	
	@Query("SELECT f FROM Food f "
			+ "WHERE f.name LIKE %:keyword% "
			+ "OR f.foodCatefory.name LIKE %:keyword%")
	List<Food> searchFood(@Param("keyword") String keyword);
}
