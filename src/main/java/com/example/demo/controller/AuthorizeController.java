package com.example.demo.controller;

import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;


    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${github.client.secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callBack(@RequestParam (name="code") String code,@RequestParam(name="state") String state,
                           HttpServletRequest request){
       AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
       accessTokenDTO.setCode(code);
       accessTokenDTO.setRedirect_uri(redirectUri);
       accessTokenDTO.setState(state);
       accessTokenDTO.setClient_id(clientId);
       accessTokenDTO.setClient_secret(clientSecret);
       String accessToken = githubProvider.getAccessToken(accessTokenDTO);
       GithubUser githubUser = githubProvider.getUser(accessToken);
       if(githubUser != null){
           // 登录成功操作
           User user = new User();
           user.setToken(UUID.randomUUID().toString());
           user.setName(githubUser.getName());
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           user.setAccountId(String.valueOf(githubUser.getId()));
           userMapper.insert(user);
           request.getSession().setAttribute("user",githubUser);
           return "redirect:/";
       }else{

           return "redirect:/";
       }

   }

}
