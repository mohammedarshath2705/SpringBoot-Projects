package com.geeksforgeeks.DigitalLibrary.security;

import com.geeksforgeeks.DigitalLibrary.security.filters.JwtRequestFilter;
import com.geeksforgeeks.DigitalLibrary.service.MyUserDetailsService;
import com.geeksforgeeks.DigitalLibrary.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil, JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);  // Disable CSRF as you might be using JWT (stateless)

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Define authorization rules
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/book/**").hasAnyRole("MEMBER", "LIBRARIAN")
                .requestMatchers("/member/**","/Issue_data/**").hasRole("LIBRARIAN")
                .requestMatchers("/public/**", "/auth/**").permitAll() // Open public and auth endpoints
                .anyRequest().authenticated() // Other endpoints require authentication
        );

        // You can remove this line if you're not using form login.
        // .formLogin(Customizer.withDefaults()); // Optional if you don't need traditional login forms.

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.myUserDetailsService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Encrypt passwords using BCrypt
    }
}
