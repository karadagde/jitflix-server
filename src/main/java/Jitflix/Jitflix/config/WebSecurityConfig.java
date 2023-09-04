package Jitflix.Jitflix.config;

import Jitflix.Jitflix.auth.AuthEntryPointJwt;
import Jitflix.Jitflix.exception.CustomBearerTokenAccessDeniedHandler;
import Jitflix.Jitflix.exception.CustomBearerTokenAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;


    private final AuthEntryPointJwt authEntryPointJwt;

    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;

    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(c -> c.disable());
        http.csrf(c -> c.ignoringRequestMatchers("/api/v1/auth/**",
                "/api/v1/watch/history/**")
        );
        http.cors(c -> c.configurationSource(request -> {
                    CorsConfiguration conf = new CorsConfiguration();
                    conf.setAllowedOrigins(List.of("http://localhost:4200",
                            "https://jitflix.netlify.app"));
                    conf.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "DELETE"));
                    conf.setAllowCredentials(true);
                    conf.setAllowedHeaders(List.of("*"));

                    return conf;
                }
        ));


        http.authorizeHttpRequests(
                a -> a.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/api/v1/users/user/check/{email}")
                        .permitAll()
//                        .requestMatchers("/api/v1/movies/watch/**")
//                        .permitAll()
                        .anyRequest()
                        .authenticated());
        http.exceptionHandling(e -> e
//                .authenticationEntryPoint(authEntryPointJwt));
                        .authenticationEntryPoint(
                                customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(customBearerTokenAccessDeniedHandler)

        );

        http.sessionManagement(
                s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        http.authenticationProvider(authenticationProvider)
//        don't know
//        why absence of this line is not causing any error or behaviour change

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}