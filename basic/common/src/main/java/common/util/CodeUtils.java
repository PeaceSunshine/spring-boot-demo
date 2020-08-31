package common.util;

import java.util.Random;

/**
 * @Author lx
 * @Date 2020/8/19
 * @Description 验证码工具类
 **/
public class CodeUtils {

    public static String generateCode(int len){
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(
                Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0,len);
    }
}
