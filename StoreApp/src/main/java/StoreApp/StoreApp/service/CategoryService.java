package StoreApp.StoreApp.service;

import java.util.List;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
	
	Category saveCategory(Category category);

	Category getCategoryById(int id);

	Category updateCategory(Category category);

	void deleteCategoryById(int id);

	List<Category> findAll();

	Page<Category> findAllPageAble(Pageable pageable);

	Page<Category> findByCategory_NameContaining(String name, Pageable pageable);

}
