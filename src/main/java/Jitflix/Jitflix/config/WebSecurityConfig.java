package Jitflix.Jitflix.config;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(c -> c.disable());
        http.csrf(c -> c.ignoringRequestMatchers("/api/v1/auth/**"));
        http.cors(c -> c.configurationSource(request -> {
                    CorsConfiguration conf = new CorsConfiguration();
                    conf.setAllowedOrigins(List.of("http://localhost:4200"));
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
                        .requestMatchers("/api/v1/movies/**").permitAll()
                        .anyRequest()
                        .authenticated());
       
        http.sessionManagement(
                s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}