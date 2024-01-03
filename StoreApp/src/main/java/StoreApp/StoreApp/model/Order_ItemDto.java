package StoreApp.StoreApp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;
@Data
public class Order_ItemDto{
	private int id;
	private int count;
	private ProductDto product;
//	private OrderDto order;
}
