package Jitflix.Jitflix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/viewing-history")
public class ViewingHistoryController {
    @GetMapping()
    public String getViewingHistory() {
        return "<h1>Viewing History</h1>";
    }
}
