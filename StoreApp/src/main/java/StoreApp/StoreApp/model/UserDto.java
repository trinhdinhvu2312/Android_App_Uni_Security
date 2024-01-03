package StoreApp.StoreApp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto{
	private String id;
	private String login_Type;
	private String role;
	private String password;
	private String user_Name;
	private String avatar;
	private String email;
	private String phone_Number;
//	private List<Order> order;
//	private List<Cart> cart;
}
