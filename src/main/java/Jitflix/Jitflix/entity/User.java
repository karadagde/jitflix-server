package Jitflix.Jitflix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter // generates getters for all fields
@Setter // generates setters for all fields
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    @NotEmpty(message = "email cannot be empty or null")
    private String email;
    private String hashedPassword;
    private String language;
    private String country;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ipAddresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ViewingHistory> viewingHistories;
}
