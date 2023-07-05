package Jitflix.Jitflix.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long historyId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String movieId;

    private LocalDateTime viewTimestamp;
    private int lastStoppedMinute;

}