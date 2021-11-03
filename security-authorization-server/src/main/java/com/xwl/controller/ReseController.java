package com.xwl.controller;

import com.xwl.service.UserService;
import com.xwl.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:53
 * @Description 描述信息
 */
@RequestMapping("/author")
@RestController
@RequiredArgsConstructor
public class ReseController {
    private final TokenEndpoint tokenEndpoint;
    private final UserService userService;
    @GetMapping("showData")
    public ResponseEntity<String> showData() {
        return ResponseEntity.ok("成功");
    }

    @PostMapping("sendMobileMsg/{mobile}")
    public ResponseEntity<String> sendMobileMsg(@PathVariable("mobile")String mobile){
        Boolean isSuccess=userService.sendMobileMsg(mobile);
        return ResponseEntity.ok(isSuccess.equals(Boolean.TRUE)?"发送成功,请查收验证码":"发送失败,服务异常");
    }
    @PostMapping("getToken")
    public R<Map<String, Object>> getToken(Principal principal, @RequestParam Map<String, String> parameters) {
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
