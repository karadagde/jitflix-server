package Jitflix.Jitflix.repository.pg;

import Jitflix.Jitflix.entity.pg.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findUserByEmail(String email);
}
