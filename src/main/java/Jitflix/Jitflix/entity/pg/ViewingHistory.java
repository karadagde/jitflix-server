package Jitflix.Jitflix.entity.pg;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ViewingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historyId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
//    @JsonBackReference
    private AppUser appUser;

    private String movieId;

    private LocalDateTime viewTimestamp;
    private int lastStoppedMinute;

}