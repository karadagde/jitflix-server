package Jitflix.Jitflix.dto;import lombok.Getter;import lombok.Setter;import java.time.LocalDateTime;@Getter@Setterpublic class ViewingHistoryDTO {    private long historyId;    private String movieId;    private LocalDateTime viewTimestamp;    private int lastStoppedMinute;    private UserDTO user;}