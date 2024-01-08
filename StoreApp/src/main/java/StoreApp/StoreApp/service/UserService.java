package StoreApp.StoreApp.service;

import java.util.List;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	List<User> getAllUser();

	User saveUser(User user);

	User updateUser(User user);

	void deleteUserById(String id);
	
	User GetUserByEmail(String email);

	User GetUserById(String Id);

	User findByIdAndRole(String id, String role);

	List<User> findAll();

	User getUserDetailsFromToken(String token) throws Exception;
	Page<User> findAllPageAble(Pageable pageable);
}
