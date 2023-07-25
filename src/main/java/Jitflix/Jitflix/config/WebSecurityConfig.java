package Jitflix.Jitflix.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(withDefaults());

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/**").permitAll());
        http.cors(c-> c.configurationSource(request -> {
                            CorsConfiguration conf =  new CorsConfiguration();
                            conf.setAllowedOrigins(List.of("http://localhost:4200"));
                            conf.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
                            conf.setAllowCredentials(true);
                            return conf;
        }
        ));
        return http.build();
    }
}