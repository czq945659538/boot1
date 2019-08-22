package com.example.demo.controller;

import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import com.example.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callBack(@RequestParam (name="code") String code,@RequestParam(name="state") String state){
       AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
       accessTokenDTO.setCode(code);
       accessTokenDTO.setRedirect_uri(redirectUri);
       accessTokenDTO.setState(state);
       accessTokenDTO.setClient_id(clientId);
       accessTokenDTO.setClient_secret(clientSecret);
       String accessToken = githubProvider.getAccessToken(accessTokenDTO);
       GithubUser user = githubProvider.getUser(accessToken);
       System.out.println(user.getName());
       return "index";
   }

}
