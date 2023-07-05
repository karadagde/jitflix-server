package Jitflix.Jitflix.repository;

import Jitflix.Jitflix.entity.ViewingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingHistoryRepository extends JpaRepository<ViewingHistory, Long> {
}
