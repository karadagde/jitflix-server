package Jitflix.Jitflix.config;

import Jitflix.Jitflix.entity.pg.AppUser;
import Jitflix.Jitflix.service.JwtService;
import Jitflix.Jitflix.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {

            Cookie[] cookies = request.getCookies();
            System.out.println("cookies: " + Arrays.toString(cookies));
            String jwt = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("access_token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
            System.out.println("jwt: " + jwt);
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }
            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null &&
                SecurityContextHolder.getContext().getAuthentication() ==
                null) {

                AppUser userDetails = userService.getUserByEmail(
                        userEmail);


                if (jwtService.validateToken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null,
                                    userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(
                                    request));
                    SecurityContextHolder.getContext().setAuthentication(
                            authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
            throw e;
        } catch (SignatureException e) {
            request.setAttribute("exception", "SignatureException");
            throw e;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", "MalformedJwtException");
            throw e;
        } catch (Exception e) {
            request.setAttribute("exception", e);
            throw e;

        }

    }
}
