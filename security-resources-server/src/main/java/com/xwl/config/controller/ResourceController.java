package com.xwl.config.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueWenLiang
 * @date 2021/7/1 14:22
 * @Description 模拟资源请求
 */
@RestController
@RequestMapping("demo")
public class ResourceController {
    @GetMapping("/user/{username}")
    public ResponseEntity user(@PathVariable String username){
        return  ResponseEntity.ok(username);
    }

}
