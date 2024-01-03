package StoreApp.StoreApp.service.impl;

import java.util.List;

import StoreApp.StoreApp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.repository.CategoryRepository;
import StoreApp.StoreApp.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	 CategoryRepository categoryRepository;
	
	@Override
	public Category saveCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(int id) {
		// TODO Auto-generated method stub
		return categoryRepository.getById(id);
	}

	@Override
	public Category updateCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.save(category);
	}
	
	@Override
	public void deleteCategoryById(int id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Page<Category> findAllPageAble(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	public Page<Category> findByCategory_NameContaining(String name, Pageable pageable) {
		return categoryRepository.findByCategory_NameContaining(name, pageable);
	}

}
