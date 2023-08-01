package Jitflix.Jitflix.repository.pg;

import Jitflix.Jitflix.entity.pg.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findUserByEmail(String email);
}
