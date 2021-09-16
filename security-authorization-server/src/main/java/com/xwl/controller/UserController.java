//package com.xwl.controller;
//
//import com.xwl.util.R;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author xueWenLiang
// * @date 2021/9/7 15:41
// * @Description 登录接口
// */
//@RestController
//@RequestMapping("/oauth")
//@RequiredArgsConstructor
//public class UserController {
//    private final TokenEndpoint tokenEndpoint;
//
//
//    @PostMapping("token")
//    public R<DefaultOAuth2AccessToken> getOauthTokenByUser(Principal principal, @RequestParam
//            Map<String, String> parameters) {
//        OAuth2AccessToken oauthToken = null;
//        try {
//            oauthToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
//        } catch (HttpRequestMethodNotSupportedException e) {
//            e.printStackTrace();
//        }
//        DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) oauthToken;
//        Map<String, Object> jwtData = new HashMap<>(defaultOAuth2AccessToken.getAdditionalInformation());
//        jwtData.put("enhance", "我是新增的");
//        defaultOAuth2AccessToken.setAdditionalInformation(jwtData);
//        return R.OK(defaultOAuth2AccessToken);
//    }
//}
