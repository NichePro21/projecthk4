package ManagementSystem.fpt.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String street;
    @Column(nullable = true)

    private String city;
    @Column(nullable = true)

    private String state;
    @Column(nullable = true)

    private String zipCode;
    @Column(nullable = true)

    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
