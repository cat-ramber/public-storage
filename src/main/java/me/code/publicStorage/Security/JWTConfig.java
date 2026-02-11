package me.code.publicStorage.Security;

import me.code.publicStorage.Repositories.IUserRepository;
import me.code.publicStorage.Services.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JWTConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTService jwtService,
            IUserRepository userRepository
    )
    {
http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth ->{
    auth.requestMatchers("/user/login").permitAll()
            .requestMatchers("/user/create").permitAll()
            .anyRequest().authenticated();
}).addFilterBefore(new AuthFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class);
return http.build();

    }
}
