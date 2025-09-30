package com.app.budgets.config.security;

import com.app.budgets.config.security.oauth2.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS))

                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**", "/public").permitAll()
                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2 -> oauth2
                        .failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) -> {
                            log.error("OAuth2 error: {}", ex.getMessage());
                            handlerExceptionResolver.resolveException(request, response, null, ex);
                        })
                        .successHandler(oAuth2SuccessHandler))

                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                        }))

                .build();
    }

    // @Bean
    // public ClientRegistrationRepository clientRegistrationRepository() {
    // ClientRegistration google = ClientRegistration.withRegistrationId("google")
    // .clientId("your-client-id")
    // .clientSecret("your-client-secret")
    // .scope("openid", "profile", "email")
    // .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
    // .tokenUri("https://oauth2.googleapis.com/token")
    // .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
    // .userNameAttributeName("sub")
    // .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
    // .clientName("Google")
    // .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    // .build();
    //
    // return new InMemoryClientRegistrationRepository(google);
    // }
}
