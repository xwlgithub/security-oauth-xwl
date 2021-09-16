package com.xwl.controller;

import com.xwl.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xueWenLiang
 * @date 2021/9/16 10:17
 * @Description 描述信息
 */
@RestController
@RequestMapping("/xwl-oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final TokenEndpoint tokenEndpoint;


    @PostMapping("getToken")
    public R<Map<String, Object>> getToken(Principal principal,@RequestParam Map<String, String> parameters) {
        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
        Map<String, Object> data = new LinkedHashMap<>(token.getAdditionalInformation());
        data.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refreshToken", token.getRefreshToken().getValue());
        }
        return R.OK(data);
    }


}
