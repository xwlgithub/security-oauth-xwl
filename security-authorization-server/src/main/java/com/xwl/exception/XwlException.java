package com.xwl.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:11
 * @Description 描述信息
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class XwlException  extends RuntimeException{
    private ExceptionEnum exceptionEnum;
}
