package Jitflix.Jitflix.repository.pg;

import Jitflix.Jitflix.entity.pg.ViewingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingHistoryRepository extends JpaRepository<ViewingHistory, Long> {
}
