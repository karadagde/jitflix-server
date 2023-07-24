package Jitflix.Jitflix;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EntityScan({"Jitflix.Jitflix.entity", "Jitflix.Jitflix.entity,pg"})
@EnableWebMvc
@EnableWebSecurity
@SpringBootApplication
public class JitflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(JitflixApplication.class, args);
		System.out.println("Hello World");
	}



}
