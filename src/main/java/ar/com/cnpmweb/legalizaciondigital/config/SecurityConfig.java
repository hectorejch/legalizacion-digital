package ar.com.cnpmweb.legalizaciondigital.config;

import ar.com.cnpmweb.legalizaciondigital.security.JwtAuthenticationFilter;
import ar.com.cnpmweb.legalizaciondigital.security.JwtAuthorizationFilter;
import ar.com.cnpmweb.legalizaciondigital.service.ApiKeyService;
import ar.com.cnpmweb.legalizaciondigital.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private ApiKeyService apiKeyService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/token").permitAll()
                // Deberías especificar que todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            // Asegúrate de que estos filtros estén configurados correctamente
            .addFilter(new JwtAuthenticationFilter(authenticationManager))
            .addFilterBefore(new JwtAuthorizationFilter(apiKeyService), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}