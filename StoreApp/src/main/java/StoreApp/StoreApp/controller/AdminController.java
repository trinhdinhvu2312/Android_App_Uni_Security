package StoreApp.StoreApp.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import StoreApp.StoreApp.entity.Category;
import StoreApp.StoreApp.entity.Order;
import StoreApp.StoreApp.entity.Order_Item;
import StoreApp.StoreApp.entity.Product;
import StoreApp.StoreApp.entity.ProductImage;
import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.model.Mail;
import StoreApp.StoreApp.service.CategoryService;
import StoreApp.StoreApp.service.CloudinaryService;
import StoreApp.StoreApp.service.MailService;
import StoreApp.StoreApp.service.OrderService;
import StoreApp.StoreApp.service.Order_ItemService;
import StoreApp.StoreApp.service.ProductImageService;
import StoreApp.StoreApp.service.ProductService;
import StoreApp.StoreApp.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    Order_ItemService order_ItemService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    MailService mailService;

    @Autowired
    HttpSession session;

    @Autowired
    ProductImageService productImageService;

    @GetMapping("/signin-admin")
    public String SignInAdminView(Model model) {
        String err_sign_admin = (String) session.getAttribute("err_sign_admin");
        model.addAttribute("err_sign_admin", err_sign_admin);
        session.setAttribute("err_sign_admin", null);
        return "signin-admin";
    }

    @PostMapping("/signin-admin")
    public String SignInAdminHandel(@ModelAttribute("login-name") String login_name,
                                    @ModelAttribute("pass") String pass, Model model) throws Exception {
        User admin = userService.findByIdAndRole(login_name, "admin");
        System.out.println(admin);
        if (admin == null) {
            session.setAttribute("err_sign_admin", "Username or Password is not correct!");
            return "redirect:/api/admin/signin-admin";
        } else {
            String decodedValue = new String(Base64.getDecoder().decode(admin.getPassword()));
            if (!decodedValue.equals(pass)) {
                session.setAttribute("err_sign_admin", "Username or Password is not correct!");
                return "redirect:/api/admin/signin-admin";
            } else {
                System.out.println(admin);
                session.setAttribute("admin", admin);
                return "redirect:/api/admin/dashboard";
            }
        }
    }

    @GetMapping("/logout-admin")
    public String LogOutAdmin(Model model) {
        session.setAttribute("admin", null);
        return "redirect:/api/admin/signin-admin";
    }

    @GetMapping("/dashboard")
    public String DashboardView(Model model) {
        User admin = (User) session.getAttribute("admin");
        System.out.println("======");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            List<Order> listOrder = orderService.findAll();
            List<Product> listProduct = productService.getAllProduct();
            List<User> listUser = userService.findAll();
            List<Category> listCategory = categoryService.findAll();

            model.addAttribute("Total_Order", listOrder.size());
            model.addAttribute("Total_Product", listProduct.size());
            model.addAttribute("Total_User", listUser.size());
            model.addAttribute("Total_Category", listCategory.size());
            return "dashboard";
        }
    }

    @GetMapping("/dashboard-invoice/{id}")
    public String InvoiceView(@PathVariable int id, Model model, HttpServletRequest request) {
        Order order = orderService.findById(id);
        List<Order_Item> listOrder_Item = order_ItemService.getAllByOrder_Id(order.getId());
        model.addAttribute("listOrder_Item", listOrder_Item);
        model.addAttribute("order", order);
        return "dashboard-invoice";
    }

    @GetMapping("/dashboard-orders")
    public String DashboardOrderView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Pageable pageable = PageRequest.of(0, 3);
            Page<Order> pageOrder = orderService.findAll(pageable);
            model.addAttribute("pageOrder", pageOrder);
            return "dashboard-orders";
        }
    }

    @GetMapping("/dashboard-orders/{page}")
    public String DashboardOrderPageView(@PathVariable int page, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Pageable pageable = PageRequest.of(page, 3);
            Page<Order> pageOrder = orderService.findAll(pageable);
            model.addAttribute("pageOrder", pageOrder);
            return "dashboard-orders";
        }
    }

    @PostMapping("/send-message")
    public String SendMessage(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("email") String email, HttpServletRequest request) throws Exception {
        String referer = request.getHeader("Referer");
        System.out.println(message);
        System.out.println(email);
        Mail mail = new Mail();
        mail.setMailFrom("trinhdinhvu2312@gmail.com");
        mail.setMailTo(email);
        mail.setMailSubject("This is message from Male fashion.");
        mail.setMailContent(message);
        mailService.sendEmail(mail);
        return "redirect:" + referer;
    }

    @GetMapping("/delete-order/{id}")
    public String DeleteOrder(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            String referer = request.getHeader("Referer");
            Order order = orderService.findById(id);
            System.out.println(order);
            if (order != null) {
                for (Order_Item y : order.getOrder_Item()) {
                    order_ItemService.deleteById(y.getId());
                }
                orderService.deleteById(id);
            }
            return "redirect:" + referer;
        }
    }

    @GetMapping("dashboard-wallet")
    public String DashboardWalletView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            List<Order> listOrder = orderService.findAll();
            List<Order> listPaymentWithMomo = orderService.findAllByPayment_Method("Pay with ZaloPay");
            List<Order> listPaymentOnDelivery = orderService.findAllByPayment_Method("Pay on Delivery");
            int TotalMomo = 0;
            int TotalDelivery = 0;
            for (Order y : listPaymentWithMomo) {
                TotalMomo = TotalMomo + y.getTotal();
            }
            for (Order y : listPaymentOnDelivery) {
                TotalDelivery = TotalDelivery + y.getTotal();
            }
            model.addAttribute("TotalMomo", TotalMomo);
            model.addAttribute("TotalDelivery", TotalDelivery);
            model.addAttribute("TotalOrder", listOrder.size());
            return "dashboard-wallet";
        }
    }

    @GetMapping("/dashboard-product")
    public String DashboardProductView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            List<Category> listCategories = categoryService.findAll();
            Pageable pageable = PageRequest.of(0, 3);
            Page<Product> pageProduct = productService.findAll(pageable);
            model.addAttribute("pageProduct", pageProduct);
            model.addAttribute("listCategories", listCategories);
            return "dashboard-product";
        }
    }

    @GetMapping("/dashboard-category")
    public String DashboardCategoryView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Pageable pageable = PageRequest.of(0, 5);
            Page<Category> pageCategory = categoryService.findAllPageAble(pageable);
            model.addAttribute("pageCategory", pageCategory);
            return "dashboard-category";
        }
    }

    @GetMapping("dashboard-product/{page}")
    public String DashboardMyProductPageView(@PathVariable int page, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            List<Category> listCategories = categoryService.findAll();
            Pageable pageable = PageRequest.of(page, 3);
            Page<Product> pageProduct = productService.findAll(pageable);
            model.addAttribute("pageProduct", pageProduct);
            model.addAttribute("listCategories", listCategories);
            return "dashboard-product";
        }
    }

    @GetMapping("dashboard-category/{page}")
    public String DashboardMyCategoryPageView(@PathVariable int page, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Pageable pageable = PageRequest.of(page, 5);
            Page<Category> pageCategory = categoryService.findAllPageAble(pageable);
            model.addAttribute("pageCategory", pageCategory);
            return "dashboard-category";
        }
    }

    @PostMapping("/dashboard-product/search")
    public String DashboardProductSearch(@ModelAttribute("search-input") String search_input,
                                         @ModelAttribute("category-selected") int category_selected, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Page<Product> pageProduct = null;
            Pageable pageable = PageRequest.of(0, 3);
            if (category_selected > 0) {
                pageProduct = productService.findByProduct_NameAndCategory_idContaining(search_input, category_selected,
                        pageable);
            } else {
                pageProduct = productService.findByProduct_NameContaining(search_input, pageable);
            }
            List<Category> listCategories = categoryService.findAll();
            String nameCategory = null;
            if (category_selected == 0) {
                nameCategory = null;
            } else {
                for (Category y : listCategories) {
                    if (y.getId() == category_selected) {
                        nameCategory = y.getCategory_Name();
                    }
                }
            }
            System.out.println(nameCategory);
            model.addAttribute("pageProduct", pageProduct);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("search_dashboard", "search_dashboard");
            model.addAttribute("search_input", search_input);
            model.addAttribute("nameCategory", nameCategory);
            session.setAttribute("search_input_dashboard", search_input);
            session.setAttribute("category_selected", category_selected);
            return "dashboard-product";
        }
    }

    @PostMapping("/dashboard-category/search")
    public String DashboardProductSearch(@ModelAttribute("search-input") String search_input, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Page<Category> pageCategory = null;
            Pageable pageable = PageRequest.of(0, 3);
            pageCategory = categoryService.findByCategory_NameContaining(search_input, pageable);
            model.addAttribute("pageProduct", pageCategory);
            model.addAttribute("search_dashboard", "search_dashboard");
            model.addAttribute("search_input", search_input);
            session.setAttribute("search_input_dashboard", search_input);

            return "dashboard-product";
        }
    }

    @GetMapping("/dashboard-product/search/{page}")
    public String DashboardProductSearchPage(@PathVariable int page, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String search_input = (String) session.getAttribute("search_input_dashboard");
            int category_selected = (int) session.getAttribute("category_selected");
//			int category_selected = 0;
            Page<Product> pageProduct = null;
            Pageable pageable = PageRequest.of(page, 3);
            if (category_selected > 0) {
                pageProduct = productService.findByProduct_NameAndCategory_idContaining(search_input, category_selected,
                        pageable);
            } else {
                pageProduct = productService.findByProduct_NameContaining(search_input, pageable);
            }
            List<Category> listCategories = categoryService.findAll();
            model.addAttribute("pageProduct", pageProduct);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("search_dashboard", "search_dashboard");
            model.addAttribute("search_input", search_input);
            model.addAttribute("category_selected", category_selected);
            session.setAttribute("search_input_dashboard", search_input);
            return "dashboard-product";
        }
    }

    @GetMapping("/dashboard-category/search/{page}")
    public String DashboardCategorySearchPage(@PathVariable int page, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String search_input = (String) session.getAttribute("search_input_dashboard");
            Page<Category> pageCategory = null;
            Pageable pageable = PageRequest.of(page, 5);
            pageCategory = categoryService.findByCategory_NameContaining(search_input, pageable);
            model.addAttribute("pageCategory", pageCategory);
            model.addAttribute("search_dashboard", "search_dashboard");
            model.addAttribute("search_input", search_input);
            session.setAttribute("search_input_dashboard", search_input);
            return "dashboard-category";
        }
    }

    @GetMapping("/dashboard-product/edit/{id}")
    public String DashboardMyProductEditView(@PathVariable int id, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            List<Category> listCategories = categoryService.findAll();
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("listCategories", listCategories);
            String editProduct = (String) session.getAttribute("editProduct");
            model.addAttribute("editProduct", editProduct);
            session.setAttribute("editProduct", null);
            return "dashboard-productedit";
        }
    }

    @PostMapping("/dashboard-product/edit")
    public String DashboardMyProductEditHandel(Model model, @ModelAttribute("product_id") int product_id,
                                               @ModelAttribute("product_name") String product_name, @ModelAttribute("price") String price,
                                               @ModelAttribute("availability") String availability, @ModelAttribute("category") int category,
                                               @ModelAttribute("description") String description, @ModelAttribute("listImage") MultipartFile[] listImage)
            throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            if (listImage != null) {
                Category cate = categoryService.getCategoryById(category);
                Product product = productService.getProductById(product_id);
//				System.out.println(cate);
//				long millis = System.currentTimeMillis();
//				Date create_at = new java.sql.Date(millis);
//				Product newPro = new Product();
                product.setProduct_Name(product_name);
                product.setPrice(Integer.parseInt(price));
                product.setQuantity(Integer.parseInt(availability));
                product.setCategory(cate);
                product.setDescription(description);
                productService.saveProduct(product);
                for (MultipartFile y : listImage) {
                    if (!y.isEmpty()) {
                        String urlImg = cloudinaryService.uploadFile(y);
                        ProductImage img = new ProductImage();
                        img.setProduct(product);
                        img.setUrl_Image(urlImg);
                        productImageService.save(img);
                    }
                }
                session.setAttribute("editProduct", "editProductSuccess");
                return "redirect:/api/admin/dashboard-product";
            } else {
                return "redirect:/api/admin/dashboard-product/edit/" + product_id;
            }

        }
    }

    @GetMapping("/dashboard-category/edit/{id}")
    public String DashboardMyCategoryEditView(@PathVariable int id, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
            String editCategory = (String) session.getAttribute("editCategory");
            model.addAttribute("editCategory", editCategory);
            session.setAttribute("editCategory", null);
            return "dashboard-categoryedit";
        }
    }

    @PostMapping("/dashboard-category/edit")
    public String DashboardMyCategoryEditHandel(Model model, @ModelAttribute("category_id") int category_id,
                                                @ModelAttribute("category_name") String category_name)
            throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Category category = categoryService.getCategoryById(category_id);
            category.setCategory_Name(category_name);
            category.setCategory_Image(null);
            categoryService.saveCategory(category);
            session.setAttribute("editCategory", "editCategorySuccess");
            return "redirect:/api/admin/dashboard-category";
        }
    }

    @GetMapping("/dashboard-product/delete/{id}")
    public String DeleteProduct(@PathVariable int id, HttpServletRequest request) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String referer = request.getHeader("Referer");
            productService.deleteProductById(id);
            return "redirect:" + referer;
        }
    }

    @GetMapping("/dashboard-category/delete/{id}")
    public String DeleteCategoryById(@PathVariable int id, HttpServletRequest request) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String referer = request.getHeader("Referer");
            categoryService.deleteCategoryById(id);
            return "redirect:" + referer;
        }
    }

    @GetMapping("/dashboard-product/delete-image/{id}")
    public String DeleteImage(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        productImageService.deleteById(id);
        return "redirect:" + referer;
    }

    @GetMapping("dashboard-addproduct")
    public String DashboardAddProductView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String addProduct = (String) session.getAttribute("addProduct");
            model.addAttribute("addProduct", addProduct);
            session.setAttribute("addProduct", null);
            List<Category> listCategories = categoryService.findAll();
            model.addAttribute("listCategories", listCategories);
            return "dashboard-addproduct";
        }
    }

    @GetMapping("dashboard-addcategory")
    public String DashboardAddCategoryView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            String addCategory = (String) session.getAttribute("addCategory");
            model.addAttribute("addCategory", addCategory);
            session.setAttribute("addCategory", null);
            return "dashboard-addcategory";
        }
    }

    @PostMapping("dashboard-addproduct")
    public String DashboardAddProductHandel(Model model, @ModelAttribute("product_name") String product_name,
                                            @ModelAttribute("price") String price, @ModelAttribute("availability") String availability,
                                            @ModelAttribute("category") int category, @ModelAttribute("description") String description,
                                            @ModelAttribute("listImage") MultipartFile[] listImage) throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            if (listImage != null) {
                Category cate = categoryService.getCategoryById(category);
                System.out.println(cate);
                long millis = System.currentTimeMillis();
                Date create_at = new java.sql.Date(millis);
                Product newPro = new Product();
                newPro.setCreated_At(create_at);
                newPro.setDescription(description);
                newPro.setIs_Active(1);
                newPro.setIs_Selling(1);
                newPro.setPrice(Integer.parseInt(price));
                newPro.setProduct_Name(product_name);
                newPro.setQuantity(Integer.parseInt(availability));
                newPro.setSold(0);
                newPro.setCategory(cate);
                productService.saveProduct(newPro);
                List<Product> listProducts = productService.getAllProduct();
                newPro = listProducts.get(listProducts.size() - 1);
                for (MultipartFile y : listImage) {
                    String urlImg = cloudinaryService.uploadFile(y);
                    ProductImage img = new ProductImage();
                    img.setProduct(newPro);
                    img.setUrl_Image(urlImg);
                    productImageService.save(img);
                }
                session.setAttribute("addProduct", "addProductSuccess");
                return "redirect:/api/admin/dashboard-product";
            } else {
                return "dashboard-addproduct";
            }

        }
    }

    @PostMapping("dashboard-addcategory")
    public String DashboardAddCategoryHandel(Model model, @ModelAttribute("category_name") String category_name) throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/api/admin/signin-admin";
        } else {
            Category newcate = new Category();
            newcate.setCategory_Name(category_name);
            newcate.setCategory_Image(null);
            categoryService.saveCategory(newcate);
            List<Category> listCategory = categoryService.findAll();
            newcate = listCategory.get(listCategory.size() - 1);
            session.setAttribute("addCategory", "addCategorySuccess");
            return "redirect:/api/admin/dashboard-category";
        }
    }

    @GetMapping("/redirect")
    public String Redirect(Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
