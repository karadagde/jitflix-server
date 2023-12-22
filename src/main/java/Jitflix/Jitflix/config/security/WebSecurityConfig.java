package Jitflix.Jitflix.config.security;

import Jitflix.Jitflix.exception.CustomBearerTokenAccessDeniedHandler;
import Jitflix.Jitflix.exception.CustomBearerTokenAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.cors(c -> c.configurationSource(request -> {
                    CorsConfiguration conf = new CorsConfiguration();
                    conf.setAllowedOrigins(List.of("http://localhost:4200",
                            "https://jitflix.netlify.app"));
                    conf.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "DELETE",
                                    "HEAD", "OPTIONS"));
                    conf.setAllowCredentials(true);
                    conf.setAllowedHeaders(List.of("*"));
                    conf.addExposedHeader("XSRF-TOKEN");
                    return conf;
                }
        ));


        http.exceptionHandling(e -> e

                .authenticationEntryPoint(
                        customBearerTokenAuthenticationEntryPoint)
                .accessDeniedHandler(customBearerTokenAccessDeniedHandler)

        );
        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(
                s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        // set the name of the attribute the CsrfToken will be populated on
        delegate.setCsrfRequestAttributeName("_csrf");
        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
        CsrfTokenRequestHandler requestHandler = delegate::handle;
        http
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                );


        http.authorizeHttpRequests(
                a -> a.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers(
                                "/api/v1/users/user/check/{email}")
                        .permitAll()
                        .anyRequest()
                        .authenticated());


        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
                new ClearSiteDataHeaderWriter(
                        ClearSiteDataHeaderWriter.Directive.COOKIES));

        http.logout((logout) -> logout.addLogoutHandler(clearSiteData)
                .logoutUrl("/api/v1/logout")
                .invalidateHttpSession(true)
                .deleteCookies("access_token", "refresh_token", "XSRF-TOKEN",
                        "JSESSIONID")
                .clearAuthentication(true)
                .logoutSuccessUrl("/api/v1/auth/initial")

        );

        return http.build();
    }


}