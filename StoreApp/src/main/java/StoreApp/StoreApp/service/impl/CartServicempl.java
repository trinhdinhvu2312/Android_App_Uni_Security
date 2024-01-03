package StoreApp.StoreApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Cart;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.repository.CartRepository;
import StoreApp.StoreApp.service.CartService;

@Service
public class CartServicempl implements CartService{

	@Autowired
	CartRepository cartRepository;
	/**
	 *
	 */
	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(id);
	}
	@Override
	public List<Cart> GetAllCartByUser_id(String user_id) {
		// TODO Auto-generated method stub
		return cartRepository.findAllByUser_id(user_id);
	}
	@Override
	public Cart saveCart(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.save(cart);
	}
	@Override
	public int getProductByCartId(int cart_id) {
		// TODO Auto-generated method stub
		return cartRepository.findProductByCart_id(cart_id);
	}
}
