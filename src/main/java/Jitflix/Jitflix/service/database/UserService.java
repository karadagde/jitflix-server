package Jitflix.Jitflix.service.database;

import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.repository.pg.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser getUserByEmail(String email) throws
            UsernameNotFoundException {
        AppUser foundUser = userRepository.findUserByEmail(email);

        if (foundUser != null) {
            return foundUser;
        } else {
            throw new UsernameNotFoundException("User not found");

        }

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser updateUser(AppUser platformUser) {
        AppUser existingPlatformUser = userRepository.findById(
                platformUser.getUserId()).orElse(null);

        if (existingPlatformUser == null) {
            return null;
        }

        existingPlatformUser.setFirstName(platformUser.getFirstName());
        existingPlatformUser.setLastName(platformUser.getLastName());
        existingPlatformUser.setEmail(platformUser.getEmail());
        existingPlatformUser.setPassword(
                platformUser.getPassword());
        existingPlatformUser.setLanguage(platformUser.getLanguage());
        existingPlatformUser.setCountry(platformUser.getCountry());
        existingPlatformUser.setRole(platformUser.getRole());
//        existingUser.setIpAddresses(user.getIpAddresses());
        return userRepository.save(existingPlatformUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {

        AppUser foundUser = userRepository.findUserByEmail(username);

        if (foundUser != null) {
            return foundUser;
        } else {
            throw new UsernameNotFoundException("User not found");

        }
//        } catch (Exception e) {
//            throw new BadCredentialsException("User not found");
//        }
    }
}
