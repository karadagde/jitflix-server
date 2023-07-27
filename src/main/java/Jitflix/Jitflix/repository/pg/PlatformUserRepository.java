package Jitflix.Jitflix.repository.pg;

import Jitflix.Jitflix.entity.pg.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {

    PlatformUser findPlatformUserByEmail(String email);
}
