package Jitflix.Jitflix.service;

import Jitflix.Jitflix.entity.User;
import Jitflix.Jitflix.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElse(null);

        if (existingUser == null){
            return null;
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setHashedPassword(user.getHashedPassword());
        existingUser.setLanguage(user.getLanguage());
        existingUser.setCountry(user.getCountry());
        existingUser.setIpAddresses(user.getIpAddresses());
        return userRepository.save(existingUser);
    }


}
