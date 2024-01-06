package StoreApp.StoreApp.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "expiry_date")
    private Date expirationDate;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
