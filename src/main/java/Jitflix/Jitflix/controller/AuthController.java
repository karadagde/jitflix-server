package Jitflix.Jitflix.controller;import Jitflix.Jitflix.auth.AuthenticationRequest;import Jitflix.Jitflix.auth.AuthenticationResponse;import Jitflix.Jitflix.auth.RegistrationRequest;import Jitflix.Jitflix.service.auth.AuthService;import jakarta.servlet.http.Cookie;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import lombok.RequiredArgsConstructor;import org.springframework.http.ResponseEntity;import org.springframework.security.web.csrf.CsrfToken;import org.springframework.web.bind.annotation.*;import java.io.IOException;@RestController@RequestMapping("/api/v1/auth")@RequiredArgsConstructorpublic class AuthController {    private final AuthService service;    @PostMapping("/register")    public ResponseEntity<AuthenticationResponse> register(            @RequestBody RegistrationRequest registrationRequest,            HttpServletResponse response) {        AuthenticationResponse tokenResponse = service.register(                registrationRequest);        setCookies(tokenResponse, response);        return ResponseEntity.ok(                new AuthenticationResponse(tokenResponse.getAccessToken(),                        tokenResponse.getRefreshToken(),                        tokenResponse.getRole()));    }    @PostMapping("/authenticate")    public ResponseEntity<AuthenticationResponse> authenticate(            @RequestBody AuthenticationRequest authenticationRequest,            HttpServletResponse response) {        AuthenticationResponse tokenResponse = service.authenticate(                authenticationRequest);        setCookies(tokenResponse, response);        return ResponseEntity.ok(                new AuthenticationResponse(tokenResponse.getAccessToken(),                        tokenResponse.getRefreshToken(),                        tokenResponse.getRole()));    }    @PostMapping("/refresh-token")    public ResponseEntity<AuthenticationResponse> refreshToken(            HttpServletRequest request,            HttpServletResponse response) throws IOException {        AuthenticationResponse tokenResponse = service.refreshToken(request,                response);        setCookies(tokenResponse, response);        return ResponseEntity.ok(                new AuthenticationResponse(tokenResponse.getAccessToken(),                        tokenResponse.getRefreshToken(),                        tokenResponse.getRole()));    }    @GetMapping("/initial")    public CsrfToken csrf(CsrfToken token) {        return token;    }    private void setCookies(AuthenticationResponse tokenResponse,                            HttpServletResponse response) {        // Setting JWT as HttpOnly cookie        Cookie jwtCookie = new Cookie("access_token",                tokenResponse.getAccessToken());        jwtCookie.setHttpOnly(true);        jwtCookie.setSecure(                false); // in production, you should set this to true        jwtCookie.setPath("/");        String cookieHeader = String.format(                "%s=%s; Path=%s; HttpOnly; SameSite=Lax",                jwtCookie.getName(),                jwtCookie.getValue(),                jwtCookie.getPath()        );//        response.addCookie(jwtCookie);        response.addHeader("Set-Cookie", cookieHeader);        // Setting refresh token as HttpOnly cookie        Cookie refreshTokenCookie = new Cookie("refresh_token",                tokenResponse.getRefreshToken());        refreshTokenCookie.setHttpOnly(true);        refreshTokenCookie.setSecure(                false); // in production, you should set this to true        refreshTokenCookie.setPath("/");        String refHeader = String.format(                "%s=%s; Path=%s; HttpOnly; SameSite=Lax",                refreshTokenCookie.getName(),                refreshTokenCookie.getValue(),                refreshTokenCookie.getPath()        );//        response.addCookie(refreshTokenCookie);        response.addHeader("Set-Cookie", refHeader);    }}