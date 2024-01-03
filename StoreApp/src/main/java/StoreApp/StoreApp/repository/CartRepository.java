package StoreApp.StoreApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import StoreApp.StoreApp.entity.Cart;
import StoreApp.StoreApp.entity.Product;

public interface CartRepository extends JpaRepository<Cart,Integer>{

	List<Cart> findAllByUser_id(String user_id);

	@Query(value="select product_id from fashionstore.cart where id = ?",nativeQuery = true)
	int findProductByCart_id(int cart_id);
}
