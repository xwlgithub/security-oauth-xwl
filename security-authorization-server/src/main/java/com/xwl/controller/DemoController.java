package com.xwl.controller;

import com.xwl.util.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author xueWenLiang
 * @date 2021/9/16 13:56
 * @Description 描述信息
 */
@RestController
@RequestMapping("/de")
public class DemoController {

    @PostMapping("haha")
    public R<Object> getHaa(Principal principal,@RequestParam Map<String,Object> params){
        return R.OK("2554");
    }
}
