package com.moblie.management.global.config;

import com.moblie.management.global.config.handler.CustomAccessDeniedHandler;
import com.moblie.management.global.jwt.filter.JwtFilter;
import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.global.jwt.filter.CustomLoginFilter;
import com.moblie.management.global.jwt.filter.CustomLogoutFilter;
import com.moblie.management.global.jwt.handler.CustomOAuthFailHandler;
import com.moblie.management.global.jwt.handler.CustomOAuthSuccessHandler;
import com.moblie.management.global.jwt.service.CustomOAuth2UserService;
import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuthFailHandler customOAuthFailHandler;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(0)
    public SecurityFilterChain mobilFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            }));

        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/signup", "/signin/**", "/login", "/reissue", "/images/**").permitAll()
                        .requestMatchers("/product/**", "/products/**", "/product/**", "/factory/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/admin/**", "/order/approve").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling((ex) -> ex.accessDeniedHandler(customAccessDeniedHandler));

        http
                .addFilterBefore(new JwtFilter(jwtUtil), CustomLoginFilter.class);
        //jwt
        http
                .addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisRefreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new JwtFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRefreshTokenService), LogoutFilter.class);

        //oauth
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customOAuthSuccessHandler)
                        .failureHandler(customOAuthFailHandler));

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

}
