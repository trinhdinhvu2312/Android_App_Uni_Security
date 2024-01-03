package StoreApp.StoreApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import StoreApp.StoreApp.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{

	void deleteById(int id);

}
