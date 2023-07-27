package Jitflix.Jitflix.service;

import Jitflix.Jitflix.entity.pg.PlatformUser;
import Jitflix.Jitflix.repository.pg.PlatformUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlatformUserService implements UserDetailsService {
    private final PlatformUserRepository platformUserRepository;


    public PlatformUserService(PlatformUserRepository platformUserRepository) {
        this.platformUserRepository = platformUserRepository;
    }

    public PlatformUser getUserByEmail(String email) throws
            UsernameNotFoundException {
        return platformUserRepository.findPlatformUserByEmail(email);

    }


//    public PlatformUser getUserById(Long id) {
//        return platformUserRepository.findById(id).orElse(null);
//    }

//    public PlatformUser saveUser(PlatformUser platformUser) {
//        return platformUserRepository.save(platformUser);
//    }

    public void deleteUser(Long id) {
        platformUserRepository.deleteById(id);
    }

    public PlatformUser updateUser(PlatformUser platformUser) {
        PlatformUser existingPlatformUser = platformUserRepository.findById(
                platformUser.getUserId()).orElse(null);

        if (existingPlatformUser == null) {
            return null;
        }

        existingPlatformUser.setFirstName(platformUser.getFirstName());
        existingPlatformUser.setLastName(platformUser.getLastName());
        existingPlatformUser.setEmail(platformUser.getEmail());
        existingPlatformUser.setHashedPassword(
                platformUser.getHashedPassword());
        existingPlatformUser.setLanguage(platformUser.getLanguage());
        existingPlatformUser.setCountry(platformUser.getCountry());
        existingPlatformUser.setRole(platformUser.getRole());
//        existingUser.setIpAddresses(user.getIpAddresses());
        return platformUserRepository.save(existingPlatformUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        return platformUserRepository.findPlatformUserByEmail(username);
    }
}
