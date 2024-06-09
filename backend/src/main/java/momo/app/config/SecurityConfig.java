package momo.app.config;

import lombok.RequiredArgsConstructor;
import momo.app.auth.jwt.JwtAuthenticationEntryPoint;
import momo.app.auth.jwt.filter.JwtAuthenticationProcessingFilter;
import momo.app.auth.jwt.JwtAccessDeniedHandler;
import momo.app.auth.oauth2.handler.LoginFailureHandler;
import momo.app.auth.oauth2.handler.LoginSuccessHandler;
import momo.app.auth.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/testTokens", "/ws").permitAll()
                        .requestMatchers("/v3/**", "swagger-ui/**").permitAll()
                        .requestMatchers("/api/v1/tokens/**").permitAll()
                        .requestMatchers("/api/v1/sign-up").hasRole("GUEST")
                        .requestMatchers("/api/v1/gatherings/**").hasRole("USER")
                        .requestMatchers("/api/v1/applications/**").hasRole("USER")
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    exceptionHandlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .sessionManagement(SessionManagementConfigurer ->
                        SessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )
                .addFilterAfter(jwtAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login((oauth2) -> {
                            oauth2.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                    .userService(customOAuth2UserService))
                                    .successHandler(loginSuccessHandler)
                                    .failureHandler(loginFailureHandler);
                            oauth2.authorizationEndpoint(authorizationEndpointConfig ->
                                    authorizationEndpointConfig.baseUri("/api/oauth2/authorize"));
                            oauth2.redirectionEndpoint(redirectionEndpointConfig ->
                                    redirectionEndpointConfig.baseUri("/api/oauth2/callback/google"));
                        });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}