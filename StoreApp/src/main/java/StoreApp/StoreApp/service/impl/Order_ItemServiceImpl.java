package StoreApp.StoreApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.repository.Order_ItemRepository;
import StoreApp.StoreApp.service.Order_ItemService;

@Service
public class Order_ItemServiceImpl implements Order_ItemService{

	@Autowired
	Order_ItemRepository order_ItemRepository;
	@Override
	public Order_Item saveOrder_Item(Order_Item order_Item) {
		return order_ItemRepository.save(order_Item);
	}
	@Override
	public List<Order_Item> getAllByOrder_Id(int id) {
		// TODO Auto-generated method stub
		return order_ItemRepository.findAllByOrder_id(id);
	}
	@Override
	public void deleteById(int id) {
		order_ItemRepository.deleteById(id);
	}
}
