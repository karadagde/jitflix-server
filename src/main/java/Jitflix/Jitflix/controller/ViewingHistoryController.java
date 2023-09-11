package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.dto.ViewingHistoryDTO;
import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.entity.pg.ViewingHistory;
import Jitflix.Jitflix.service.database.EntityToDtoConverter;
import Jitflix.Jitflix.service.database.UserService;
import Jitflix.Jitflix.service.database.ViewingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/watch/history")
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


    @GetMapping("/{movieId}/{email}")
    public ResponseEntity<ViewingHistoryDTO> getViewingHistory(
            @PathVariable String movieId,
            @PathVariable String email) throws IOException {
        System.out.println("movieId: " + movieId);
        System.out.println("email: " + email);
        AppUser appUser = userService.getUserByEmail(email);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        ViewingHistory viewingHistory =
                historyService.getViewingHistoryByMovieIdAndAppUser(movieId,
                        appUser);
        ViewingHistoryDTO dto = converter.convertToDto(viewingHistory);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
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
