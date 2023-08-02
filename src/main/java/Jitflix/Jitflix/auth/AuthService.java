package Jitflix.Jitflix.auth;import Jitflix.Jitflix.entity.pg.AppUser;import Jitflix.Jitflix.entity.pg.Role;import Jitflix.Jitflix.repository.pg.UserRepository;import Jitflix.Jitflix.service.JwtService;import com.fasterxml.jackson.databind.ObjectMapper;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import lombok.RequiredArgsConstructor;import org.springframework.http.HttpHeaders;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;import java.io.IOException;@Service@RequiredArgsConstructorpublic class AuthService {    private final UserRepository repository;    private final PasswordEncoder passwordEncoder;    private final JwtService jwtService;    private final AuthenticationManager authenticationManager;    public AuthenticationResponse register(            RegistrationRequest registrationRequest) {        var user = new AppUser();        user.setFirstName(registrationRequest.getFirstName());        user.setLastName(registrationRequest.getLastName());        user.setEmail(registrationRequest.getEmail());        user.setPassword(                passwordEncoder.encode(registrationRequest.getPassword()));        user.setLanguage(registrationRequest.getLanguage());        user.setCountry(registrationRequest.getCountry());        user.setRole(Role.USER);        repository.save(user);        var jwtToken = jwtService.generateToken(user);        var refreshToken = jwtService.generateRefreshToken(user);        return AuthenticationResponse.builder()                .accessToken(jwtToken)                .refreshToken(refreshToken)                .build();    }    public AuthenticationResponse authenticate(            AuthenticationRequest authenticationRequest) {        authenticationManager.authenticate(                new org.springframework.security.authentication.                        UsernamePasswordAuthenticationToken(                        authenticationRequest.getEmail(),                        authenticationRequest.getPassword()));        var user = repository.findUserByEmail(                authenticationRequest.getEmail()).orElseThrow();        var jwtToken = jwtService.generateToken(user);        var refreshToken = jwtService.generateRefreshToken(user);        return AuthenticationResponse.builder()                .accessToken(jwtToken)                .refreshToken(refreshToken)                .build();    }    public void refreshToken(HttpServletRequest request,                             HttpServletResponse response) throws IOException {        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);        final String refreshToken;        final String userEmail;        if (authHeader == null || !authHeader.startsWith("Bearer ")) {            return;        }        refreshToken = authHeader.substring(7);        userEmail = jwtService.extractUsername(refreshToken);        if (userEmail != null) {            UserDetails user = repository.findUserByEmail(                    userEmail).orElseThrow();            if (jwtService.validateToken(refreshToken, user)) {                var accessToken = jwtService.generateToken(user);                var authResponse = AuthenticationResponse.builder()                        .accessToken(accessToken)                        .refreshToken(refreshToken)                        .build();                new ObjectMapper().writeValue(response.getOutputStream(),                        authResponse);            }        }    }}