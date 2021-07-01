package com.xwl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:53
 * @Description 描述信息
 */
@RequestMapping("/author")
@RestController
public class ReseController {
    @GetMapping("showData")
    public ResponseEntity<String> showData() {
        return ResponseEntity.ok("成功");
    }
}
