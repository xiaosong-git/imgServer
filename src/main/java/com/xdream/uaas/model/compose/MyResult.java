package com.xdream.uaas.model.compose;


import com.xdream.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果
 * @Date  2016/7/25 16:41
 * @author linyb
 */
public class MyResult {

    protected String desc ="操作成功";   //返回提示信息
    protected String sign = "success"; //成功或者失败  fail
    protected Map<String,Object> verify;  //数据


    public MyResult(Integer result, String desc) {
        this.desc = desc;

    }
    public MyResult(String sign, String desc) {
        this.sign = sign;
        this.desc = desc;

    }

    public MyResult(){}

    public Map getVerify() {
        return verify;
    }

    public void setVerify(Map verify) {
        this.verify = verify;
    }


    /**
     * 没有返回数据
     * @Date  2016/7/25 17:18
     * @author linyb
     */
    public static MyResult unDataResult(String sign, String desc){
        MyResult result = new MyResult();
        Map map = new HashMap();
        map.put("sign",sign);
        map.put("desc",desc);
        result.verify = map;
        return result;
    }

    /**
     * 操作成功
     * @Date  2016/7/25 17:18
     * @author linyb
     */
    public static MyResult success(){
        return unDataResult("success","操作成功");
    }
    /**
     * 操作成功
     * @Date  2016/7/25 17:18
     * @author linyb
     */
    public static MyResult fail(){
        return unDataResult("fail","操作失败");
    }

    public static MyResult ResultCode(String sign, String desc, String code){
        MyResult result = new MyResult();
        Map map = new HashMap();
        map.put("sign",sign);
        map.put("desc",desc);
        map.put("code",code);
        result.verify = map;

        return result;
    }

    public static void main(String[] args) {
        MyResult result=unDataResult("success","成功");
        try {

           String json= JsonUtils.toJson(result);
            System.out.println(json);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}