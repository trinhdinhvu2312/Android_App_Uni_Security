package StoreApp.StoreApp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class CategoryDto{
	private int id;
	private String category_Name;
	private String category_Image;
	private List<ProductDto> product;
}