package Jitflix.Jitflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan({"Jitflix.Jitflix.entity", "Jitflix.Jitflix.entity,pg"})
@SpringBootApplication
public class JitflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(JitflixApplication.class, args);
		System.out.println("Hello World");
	}

}
