package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.dto.ViewingHistoryDTO;
import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.entity.pg.ViewingHistory;
import Jitflix.Jitflix.service.EntityToDtoConverter;
import Jitflix.Jitflix.service.UserService;
import Jitflix.Jitflix.service.ViewingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watch")
public class ViewingHistoryController {

    private final ViewingHistoryService historyService;
    private final UserService userService;
    @Autowired
    private EntityToDtoConverter converter;

    public ViewingHistoryController(ViewingHistoryService viewingHistoryService,
                                    UserService userService) {
        this.historyService = viewingHistoryService;
        this.userService = userService;
    }


    @GetMapping("/history/{movieId}/{email}")
    public ResponseEntity<ViewingHistoryDTO> getViewingHistory(
            @PathVariable String movieId,
            @PathVariable String email) {
        System.out.println("movieId: " + movieId);
        System.out.println("email: " + email);
        AppUser appUser = userService.getUserByEmail(email);
        if (appUser == null) {
            return ResponseEntity.badRequest().build();
        }
        ViewingHistory viewingHistory =
                historyService.getViewingHistoryByMovieIdAndAppUser(movieId,
                        appUser);
        ViewingHistoryDTO dto = converter.convertToDto(viewingHistory);

        return ResponseEntity.ok(dto);

    }


    @PostMapping()
    public ResponseEntity<?> addViewingHistory(
            @RequestBody ViewingHistory viewingHistory) {

        String movieId = viewingHistory.getMovieId();
        int lastStoppedMinute = viewingHistory.getLastStoppedMinute();
        String email = viewingHistory.getAppUser().getEmail();

        System.out.println("movieId: " + movieId);
        System.out.println("lastStoppedMinute: " + lastStoppedMinute);
        System.out.println("email: " + email);

        AppUser appUser = userService.getUserByEmail(email);
        if (appUser == null) {
            return ResponseEntity.badRequest().build();
        }
        ViewingHistory viewIngHistory =
                historyService.getViewingHistoryByMovieIdAndAppUser(movieId,
                        appUser);
        if (viewIngHistory != null) {
            historyService.updateViewingHistory(viewIngHistory,
                    lastStoppedMinute);
        } else {
            historyService.createViewingHistory(viewingHistory);
        }


        return ResponseEntity.ok().build();
    }

}
