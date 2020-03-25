package com.xdream.goldccm.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Linyb
 * @Date 2017/1/10.
 */
public class BaseUtil {

    /**
     * 随机获取一串字符串
     * @Author Linyb
     * @Date 2017/1/10 16:16
     */
    public static String getRandomStr(int length){
        return UUID.randomUUID().toString().substring(0,4);
    }

    public static Integer objToInteger(Object obj, Integer def){
        if(obj == null) {
            return def;
        }
        return Integer.parseInt(obj.toString());
    }

    public static String objToStr(Object obj,String def){
        if(obj == null) return def;
        return obj.toString();
    }

    public static BigDecimal objToBigdecimal(Object obj, BigDecimal def){
        if(obj == null) return def;
        return new BigDecimal(obj.toString());
    }

    public static Long objToLong(Object obj, Long def){
        if(obj == null) return def;
        String s = obj.toString();
        return Long.valueOf(s);
    }

    public static Map<String,Object> remove(Map<String,Object> beRemoveMap ,String ... keys){
        for (String key : keys) {
            beRemoveMap.remove(key);
        }
        return beRemoveMap;
    }

    /**
     * 判断字符串中是否包含中文，存在中文->返回true,否则返回false
     * @param str 要判断的字符串
     * @return
     * LZ
     */
    public static boolean isContainChinese(String str) {
        if(str != null){
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(str);
            if (m.find()) {
                return true;
            }
            return false;
        }
        return false;
    }


}
