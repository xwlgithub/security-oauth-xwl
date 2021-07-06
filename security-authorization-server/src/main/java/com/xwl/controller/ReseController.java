package com.xwl.controller;

import com.xwl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:53
 * @Description 描述信息
 */
@RequestMapping("/author")
@RestController
@RequiredArgsConstructor
public class ReseController {
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
}
