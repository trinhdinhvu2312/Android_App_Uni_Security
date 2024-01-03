package StoreApp.StoreApp.service;

import java.util.List;

import StoreApp.StoreApp.entity.Cart;
import StoreApp.StoreApp.entity.Product;

public interface CartService {
	
	void deleteById(int id);
	
	List<Cart> GetAllCartByUser_id(String user_id);
	
	Cart saveCart(Cart cart);
	
	int getProductByCartId(int cart_id);
	
}
