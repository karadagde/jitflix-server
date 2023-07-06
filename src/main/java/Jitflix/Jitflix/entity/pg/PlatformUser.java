package Jitflix.Jitflix.entity.pg;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PlatformUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    @NotBlank(message = "email cannot be empty or null")
    private String email;
    private String hashedPassword;
    private String language;
    private String country;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ipAddresses;

    @OneToMany(mappedBy = "platformUser", cascade = CascadeType.ALL)
    private List<ViewingHistory> viewingHistories;
}
