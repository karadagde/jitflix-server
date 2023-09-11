package Jitflix.Jitflix.service.database;

import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.entity.pg.ViewingHistory;
import Jitflix.Jitflix.repository.pg.ViewingHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ViewingHistoryService {

    private final ViewingHistoryRepository viewingHistoryRepository;
    private final UserService userService;

    public ViewingHistoryService(
            ViewingHistoryRepository viewingHistoryRepository,
            UserService userService) {
        this.viewingHistoryRepository = viewingHistoryRepository;
        this.userService = userService;
    }


    public ViewingHistory getViewingHistoryByMovieIdAndAppUser(String movieId,
                                                               AppUser appUser) {
        return viewingHistoryRepository.findViewingHistoryByMovieIdAndAppUser(
                movieId, appUser);
    }

    public void updateViewingHistory(ViewingHistory viewingHistory,
                                     int lastStoppedMinute) {


        AppUser appUser = userService.getUserByEmail(
                viewingHistory.getAppUser().getEmail());
        if (appUser == null) {
            return;
        }
        viewingHistory.setAppUser(appUser);
        viewingHistory.setLastStoppedMinute(lastStoppedMinute);
        viewingHistoryRepository.save(viewingHistory);
    }

    public void createViewingHistory(ViewingHistory viewingHistory) {
        AppUser appUser = userService.getUserByEmail(
                viewingHistory.getAppUser().getEmail());
        if (appUser == null) {
            return;
        }
        viewingHistory.setAppUser(appUser);

        viewingHistoryRepository.save(viewingHistory);
    }
}
