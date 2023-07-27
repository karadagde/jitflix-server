package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.entity.pg.PlatformUser;
import Jitflix.Jitflix.service.PlatformUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PlatformUserService platformUserService;

    public UserController(PlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }


//    @GetMapping()
//    public String getUsers() {
//        return "Users";
//    }

    @GetMapping("/user/{email}")
    public PlatformUser getUserByEmail(@PathVariable String email) {
        return platformUserService.getUserByEmail(email);
    }


//    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
//    public ResponseEntity<PlatformUser> createUser(@RequestBody PlatformUser platformUser) throws IOException {
//
//        var user = platformUserService.getUserByEmail(platformUser.getEmail());
//
//        if (user != null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        try {
//            PlatformUser newPlatformUser = platformUserService.saveUser(platformUser);
//            if (newPlatformUser != null) {
//                return ResponseEntity.ok(newPlatformUser);
//            } else {
//                return ResponseEntity.badRequest().body(null);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//
//    }

    @PutMapping(path = "/update", consumes = "application/json",
                produces = "application/json")
    public ResponseEntity<PlatformUser> updateUser(
            @RequestBody PlatformUser platformUser) throws IOException {

        var user = platformUserService.getUserByEmail(platformUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            PlatformUser updatedPlatformUser = platformUserService.updateUser(
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