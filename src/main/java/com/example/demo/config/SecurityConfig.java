package com.example.demo.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.google.client-id:YOUR_CLIENT_ID}")
    private String googleClientId;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes whether Google OAuth is properly configured so that
     * Thymeleaf templates can conditionally show/hide the Google button.
     */
    @Bean
    public Boolean googleOAuthEnabled() {
        boolean enabled = googleClientId != null
                && !googleClientId.isBlank()
                && !googleClientId.equals("YOUR_CLIENT_ID");
        System.out.println(enabled
                ? "✅ Google OAuth is ENABLED (real credentials detected)"
                : "⚠️ Google OAuth is DISABLED (placeholder credentials — skipping oauth2Login)");
        return enabled;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("✅ Food Frenzy Security Configuration is being loaded!");
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/login", "/register", "/adminLogin", "/userLogin", "/products", "/location", "/about").permitAll()
                .requestMatchers("/css/**", "/JavaScript/**", "/Images/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/h2-console/**").permitAll()
                .anyRequest().permitAll()
            );

        // Only wire up OAuth2 login when real Google credentials are provided
        if (googleOAuthEnabled()) {
            http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/oauth2/success", true)
            );
        }

        http.logout(logout -> logout
                .logoutSuccessUrl("/home")
                .permitAll()
            );
        return http.build();
    }
}
