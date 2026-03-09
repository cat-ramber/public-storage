package me.code.publicStorage.Security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import me.code.publicStorage.Models.User;
import me.code.publicStorage.Repositories.IUserRepository;
import me.code.publicStorage.Services.JWTService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
private final JWTService jwtService;
private final IUserRepository IUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();//gets the typ of auth it is
        if(auth instanceof OAuth2AuthenticationToken token){
            oAuth2token(request,response,filterChain,token);
        } else{
            jwtToken(request,response,filterChain);
        }
    }
    private void oAuth2token(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain,OAuth2AuthenticationToken token) throws ServletException, IOException {

        String id=token.getName();
        String provider=token.getAuthorizedClientRegistrationId();

        Optional<User> optionalUser =IUserRepository.findByOidcIdAndOidcProvider(id,provider);
        if(optionalUser.isEmpty()){

            response.setStatus(401);
            SecurityContextHolder.getContext().setAuthentication(null);//sets auth to null to make sure the user can't use anything
            return;
        }
        User user=optionalUser.get();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,user.getPassword(),new ArrayList<>()));
        filterChain.doFilter(request,response);
    }

    private void jwtToken(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null||authHeader.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }
        if(authHeader.length()<"Bearer".length()){
            return;
        }
        String token=authHeader.substring("Bearer ".length());
        UUID userId;

        try{
            userId=jwtService.checkToken(token);
        }catch (JWTVerificationException e){
            response.setStatus(401);
            return;
        }

        Optional<User> optionalUser= IUserRepository.findById(userId);

        if(optionalUser.isEmpty()){
            response.setStatus(401);
            return;
        }

        User user=optionalUser.get();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,user.getPassword(),new ArrayList<>()));
        filterChain.doFilter(request,response);
    }
}
