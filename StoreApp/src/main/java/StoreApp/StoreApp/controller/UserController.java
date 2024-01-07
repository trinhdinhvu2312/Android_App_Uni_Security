package StoreApp.StoreApp.controller;

import javax.servlet.http.HttpSession;

import StoreApp.StoreApp.model.AuthResponseDTO;
import StoreApp.StoreApp.model.MyUserDetails;
import StoreApp.StoreApp.service.JwtTokenService;
import StoreApp.StoreApp.service.impl.MyUserDetailService;
import StoreApp.StoreApp.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.model.Mail;
import StoreApp.StoreApp.service.CloudinaryService;
import StoreApp.StoreApp.service.MailService;
import StoreApp.StoreApp.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    HttpSession session;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<AuthResponseDTO> Login(String id, String password) {
        System.out.println(id);
        User userFind = userService.findByIdAndRole(id, "user");

        if (userFind != null && userFind.getPassword() != null) {
            if (bCryptPasswordEncoder.matches(password, userFind.getPassword())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(id, password)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                MyUserDetails userDetails = (MyUserDetails) myUserDetailService.loadUserByUsername(id);
                String token = jwtTokenUtils.generateToken(userDetails);
                jwtTokenService.saveToken(id, token);

                // Set response
                AuthResponseDTO response = new AuthResponseDTO(token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserById(@RequestParam("token") String token) {
        try {
            String userId = jwtTokenUtils.extractUserId(token);
            User user = userService.GetUserById(userId);

            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/signup", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<User> SignUp(String username, String fullname, String email, String password) {
        User user = userService.findByIdAndRole(username, "user");
        if (user != null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            String encodedValue = bCryptPasswordEncoder.encode(password);
            String avatar = "https://haycafe.vn/wp-content/uploads/2022/02/Avatar-trang-den.png";
            User newUser = userService.saveUser(new User(username, "default", "user", encodedValue, fullname, avatar,
                    email, null, null, null, null));
            System.out.println(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/forgot", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> ForgotPassword(String id) {
        User user = userService.findByIdAndRole(id, "user");
        if (user != null) {
            int code = (int) Math.floor(((Math.random() * 899999) + 100000));
            Mail mail = new Mail();
            mail.setMailFrom("trinhdinhvu2312@gmail.com");
            mail.setMailTo(user.getEmail());
            mail.setMailSubject("Forgot Password");
            mail.setMailContent("Your code is: " + code);
            mailService.sendEmail(mail);
            session.setAttribute("code", code);
            System.out.println(code);
            return new ResponseEntity<String>(new Gson().toJson(String.valueOf(code)), HttpStatus.OK);
        } else
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/forgotnewpass", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> ForgotNewPass(String id, String password) {
        User user = userService.findByIdAndRole(id, "user");
        if (user != null) {
            String encodedValue = bCryptPasswordEncoder.encode(password);
            user.setPassword(encodedValue);
            userService.saveUser(user);
            return new ResponseEntity<String>(password, HttpStatus.OK);
        } else
            return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(path = "/changepassword", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> ChangePassword(String id, String password) {
        User user = userService.findByIdAndRole(id, "user");
        if (user != null) {
            String encodedValue = bCryptPasswordEncoder.encode(password);
            user.setPassword(encodedValue);
            userService.saveUser(user);
            return new ResponseEntity<String>(password, HttpStatus.OK);
        } else
            return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(path = "/checkoldpassword", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Boolean> CheckOldPassword(String id, String oldPassword) {
        User user = userService.findByIdAndRole(id, "user");

        if (user != null) {
            // Kiểm tra xem mật khẩu cũ nhập từ ứng dụng có khớp với mật khẩu lưu trữ trên server không
            boolean isOldPasswordCorrect = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());

            // Trả về kết quả
            return new ResponseEntity<>(isOldPasswordCorrect, HttpStatus.OK);
        } else {
            // Trường hợp không tìm thấy người dùng
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(path = "/update", consumes = "multipart/form-data")
    public ResponseEntity<User> UpdateAvatar(String id, MultipartFile avatar, String fullname, String email,
                                             String phoneNumber, String address) {
        User user = userService.findByIdAndRole(id, "user");
        if (user != null) {
            if (avatar != null) {
                String url = cloudinaryService.uploadFile(avatar);
                user.setAvatar(url);
            }
            user.setUser_Name(fullname);
            user.setEmail(email);
            user.setPhone_Number(phoneNumber);
            user.setAddress(address);
            userService.saveUser(user);
//			if(user.getPassword()!=null)
//				user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(path = "/google", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<User> LoginWithGoogle(String id, String fullname, String email, String avatar) {
        User user = userService.findByIdAndRole(id, "user");
        if (user == null) {
            user = userService
                    .saveUser(new User(id, "google", "user", null, fullname, avatar, email, null, null, null, null));
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
