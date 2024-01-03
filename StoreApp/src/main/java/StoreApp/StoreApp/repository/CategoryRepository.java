package StoreApp.StoreApp.repository;

import StoreApp.StoreApp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import StoreApp.StoreApp.entity.Category;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
	
	Category getById(int id);

	@Query(value="select * from `fashionstore`.category where `fashionstore`.category.category_name like %?1%",nativeQuery = true)
	Page<Category> findByCategory_NameContaining(String name, Pageable pageable);
}
