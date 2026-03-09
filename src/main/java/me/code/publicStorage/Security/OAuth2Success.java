package me.code.publicStorage.Security;

import io.swagger.v3.core.util.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.code.publicStorage.Models.User;
import me.code.publicStorage.Services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



import java.io.IOException;
import java.util.Date;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2Success implements AuthenticationSuccessHandler {

    private final userService userService;
    @Lazy
    @Autowired
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.github.com").build();

    @Override
    public void onAuthenticationSuccess(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull Authentication authentication

    ) throws IOException {
        var oAuth2Token=(OAuth2AuthenticationToken) authentication;
        String oidcProvider=oAuth2Token.getAuthorizedClientRegistrationId();
        String oidcId=oAuth2Token.getName();

        Optional<User> user= userService.getOidcUser(oidcId,oidcProvider);
        if(user.isPresent()){
            response.getWriter().println("you logged in as "+user.get().getUserName());
            return;
        }

            assert oAuth2Token.getPrincipal() != null;

        var authClient = authorizedClientRepository.loadAuthorizedClient(oidcProvider,authentication,request);
        var authToken= authClient.getAccessToken().getTokenValue();

        //took way to long to figure out tried .retrieve didn't work
        var result =(WebClient.RequestHeadersSpec<?>) webClient.get().uri("/user").headers(h->h.setBearerAuth(authToken));
                var object=result.retrieve().toEntity(getWebUsername.class).block();

        if(object==null){
            response.getWriter().println("couldn't get the username");
            return;
        }
        if(object.getStatusCode()!= HttpStatus.OK){
            response.getWriter().println("Http error");
            return;
        }
        var body = object.getBody();
        if(body==null||body.getLogin()==null||body.getLogin().isBlank()){
            response.getWriter().println("couldn't get the username");
            return;
        }

        String username = object.getBody().getLogin();
        if (userService.userNameExist(username)){
            response.getWriter().println("that username already exists sorry");
            return;
        }
        userService.createOidcUser(username,oidcId,oidcProvider,new Date());

    }
    @Getter
    @Setter
    private static class getWebUsername{
        private String login;//dto takes the field login form the gitHub/user which is the username
    }
}
