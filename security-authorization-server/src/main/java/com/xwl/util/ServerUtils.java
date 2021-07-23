package com.xwl.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:00
 * @Description 工具类
 */
public class ServerUtils {

    /**
     * 产生4位随机数(0000-9999)
     *
     * @return 4位随机数
     */
    public static String getFourRandom() {
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if (randLength < 4) {
            for (int i = 1; i <= 4 - randLength; i++) {
                fourRandom = "0" + fourRandom;
            }
        }
        return fourRandom;
    }
    /**
     * 判断是否为数字格式不限制位数
     * @param o
     *     待校验参数
     * @return
     *     如果全为数字，返回true；否则，返回false
     */
    public static boolean isNumber(Object o){
        return  (Pattern.compile("[0-9]*")).matcher(String.valueOf(o)).matches();
    }

    public static void main(String[] args) {
        String fourRandom = getFourRandom();
        System.out.println(fourRandom);
    }

}
