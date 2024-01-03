package StoreApp.StoreApp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;
@Data
public class ProductImageDto{
	private int id;
	private String url_Image;
//	private ProductDto product;

}
