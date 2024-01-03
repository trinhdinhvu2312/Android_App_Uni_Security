package StoreApp.StoreApp.entity;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "product_Name", columnDefinition = "nvarchar(1111)")
	private String product_Name;

	@Column(name = "description", columnDefinition = "nvarchar(11111)")
	private String description;

	@Column(name = "sold")
	private int sold;

	@Column(name = "is_Active")
	private int is_Active;

	@Column(name = "is_Selling")
	private int is_Selling;

	@Column(name = "created_At")
	private Date created_At;

	@Column(name = "price")
	private int price;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnore
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> productImage;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Order_Item> order_Item;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Cart> cart;
}
