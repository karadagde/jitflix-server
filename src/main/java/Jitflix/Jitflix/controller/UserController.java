package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{email}")
    public AppUser getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }


    @GetMapping("/user/check/{email}")
    public Boolean checkIfUserExists(@PathVariable String email) throws
            IOException {
        try {
            AppUser user = userService.getUserByEmail(email);
            return user != null;

        } catch (Exception e) {
            return false;
        }
    }


    @PutMapping(path = "/update", consumes = "application/json",
                produces = "application/json")
    public ResponseEntity<AppUser> updateUser(
            @RequestBody AppUser platformUser) throws IOException {

        var user = userService.getUserByEmail(platformUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            AppUser updatedPlatformUser = userService.updateUser(
                    platformUser);
            if (updatedPlatformUser != null) {
                return ResponseEntity.ok(updatedPlatformUser);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}