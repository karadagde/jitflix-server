package Jitflix.Jitflix.repository.pg;

import Jitflix.Jitflix.entity.pg.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {

    PlatformUser findPlatformUserByEmail(String email);
}
