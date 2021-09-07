package com.xwl.controller;

import com.xwl.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueWenLiang
 * @date 2021/9/7 17:28
 * @Description 描述信息
 */
@RestController

@RequestMapping("d")
public class DemoController {


    @GetMapping("ss")
    public R<String> sss(){
        return R.OK("dsafas");
    }
}
