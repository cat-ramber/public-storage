package me.code.publicStorage.Security;

import me.code.publicStorage.Repositories.IUserRepository;
import me.code.publicStorage.Services.JWTService;
import me.code.publicStorage.Services.userService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JWTConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTService jwtService,
            IUserRepository userRepository,
            OAuth2Success oAuth2Success
    )
    {
http.csrf(AbstractHttpConfigurer::disable)
        .oauth2Login(oauth2->oauth2.successHandler(oAuth2Success))
        .authorizeHttpRequests(auth ->{

    auth.requestMatchers("/user/login").permitAll()
            .requestMatchers("/user/create").permitAll()
            .anyRequest().authenticated();
}).addFilterBefore(new AuthFilter(jwtService, userRepository), OAuth2LoginAuthenticationFilter.class);
return http.build();

    }
}