package StoreApp.StoreApp.service;

import StoreApp.StoreApp.entity.ProductImage;

public interface ProductImageService {

	void save(ProductImage productImage);

	void deleteById(int id);

}
