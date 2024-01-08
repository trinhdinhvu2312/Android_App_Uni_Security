package StoreApp.StoreApp.service.impl;

import java.util.List;
import java.util.Optional;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.utils.JwtTokenUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.repository.UserRepository;
import StoreApp.StoreApp.service.UserService;
import StoreApp.StoreApp.exception.ExpiredTokenException;

@Service
public class UserServiceImpl implements UserService{

	JwtTokenUtils jwtTokenUtils;

	@Autowired
	SessionFactory factory;
	
	private UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository=userRepository;
	}
	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public void deleteUserById(String id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}
	@Override
	public User GetUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}
	@Override
	public User findByIdAndRole(String id, String role) {
		return userRepository.findByIdAndRole(id, role);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Page<User> findAllPageAble(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User GetUserById(String Id) {
		return userRepository.findById(Id)
				.orElseThrow();
	}

	@Override
	public User getUserDetailsFromToken(String token) throws Exception {
		if(jwtTokenUtils.isTokenExpired(token)) {
			throw new ExpiredTokenException("Token is expired");
		}
		String id = jwtTokenUtils.extractUserId(token);
		User user = userRepository.findById(id).orElseThrow();

		if (user != null) {
			return user;
		} else {
			throw new Exception("User not found");
		}
	}

}
