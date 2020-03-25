package com.xdream.goldccm.service.inAndOutService.impl;

import com.xdream.JsonUtils;
import com.xdream.goldccm.service.file.IFileService;
import com.xdream.goldccm.service.inAndOutService.IInAndOutService;
import com.xdream.goldccm.util.BaseUtil;
import com.xdream.goldccm.util.MD5Util;
import com.xdream.goldccm.util.ParamDef;

import com.xdream.kernel.util.ResponseUtil;
import com.xdream.uaas.model.compose.MyResult;
import com.xdream.uaas.model.compose.TableList;
import com.xdream.uaas.service.base.impl.MyBaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service("inAndOutService")
public class IInAndOutSerivceImpl extends MyBaseServiceImpl implements IInAndOutService {
    Logger logger = LoggerFactory.getLogger(IInAndOutSerivceImpl.class);
    @Autowired
    private IFileService fileService;

    @Autowired
    private TaskExecutor taskExecutor;

    /**
     *
     * @param request
     * @return com.goldccm.model.compose.Result
     * @throws Exception
     * @author cwf
     * @date 2019/8/27 17:33
     *  update by cwf  2019/8/27 17:34 cause 改为txt上传数据库
     */
    @Override
    public void save(HttpServletRequest request,HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();

        String orgCode = request.getParameter("orgCode");
        String pospCode = request.getParameter("pospCode");
        String sign = request.getParameter("sign");
        String path=null;
        MyResult myResult=null;
        String  json=null;

        try {

            boolean check = check(orgCode, pospCode, sign);
            System.out.println("身份验证 "+check);
            if (!check){
                logger.error("上位机上传进出日志时：身份验证失败：pospCode="+pospCode+" orgCode="+orgCode+" sign ="+sign);
                myResult = MyResult.unDataResult("fail", "身份验证失败");
                return ;
            }
            //返回值为0是代表传输失败 做了文件转移位置
             path = fileService.uploadTxt(request);
            System.out.println(path);
            if (path.equals("-1")) {
                System.out.println("文件传输失败");
                myResult = MyResult.unDataResult("fail", "文件传输失败");
                return;
            }
            long end = System.currentTimeMillis();
            //开启线程进行插入数据库
            final String finalPath = path;
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("开启线程");
                    try {
                        int count = fileService.batchUpdate(finalPath, TableList.IN_OUT, "null,null,null");
                        System.out.println("插入数据数？"+count);
                        if (count>0){
//                            File file = new File(finalPath);
//                            boolean delete = file.delete();
                            logger.info(finalPath+"插入通行日志总数："+count);
//                            logger.info("临时文件删除成功？"+delete);
                            logger.info("没有删除？");
                        }
                        System.out.println(count);
                        Thread.sleep(1000);
                        Thread.interrupted();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            long end1=System.currentTimeMillis();
            System.out.println(end1-end);
            myResult=MyResult.unDataResult("success","上传成功");
            return ;
        }catch (Exception e){
            e.printStackTrace();
            myResult =MyResult.unDataResult("fail","系统错误，请联系管理员");
            return ;
        }finally {
            System.out.println("有返回");
            json = JsonUtils.toJson(myResult);
            System.out.println("json:" + json);
            ResponseUtil.responseJson(response, json);
        }



    }



    /**
     * 下发进出日志
     * update by cwf  2019/8/26 16:42
     */
    @Override
    public MyResult getInOutTxt(Map<String, Object> paramMap, HttpServletResponse response) throws Exception {

        String merchantNum = BaseUtil.objToStr(paramMap.get("merchantNum"), null);
        String orgCode = BaseUtil.objToStr(paramMap.get("orgCode"), null);
        String companyCode = BaseUtil.objToStr(paramMap.get("companyCode"), "");
        String timestamp = BaseUtil.objToStr(paramMap.get("timestamp"), null);
        String startDate = BaseUtil.objToStr(paramMap.get("startDate"), null);
        String endDate = BaseUtil.objToStr(paramMap.get("endDate"), null);
        String sign = BaseUtil.objToStr(paramMap.get("sign"), null);
        //获取私钥
        Map<String, Object> merchant = findFirstBySql("select private_key from " + TableList.MERCHANT + " where merchant_num=" + merchantNum);
        String privateKey = BaseUtil.objToStr(merchant.get("privateKey"),null) ;
        String verify = merchantNum + orgCode + companyCode + timestamp + privateKey;
        System.out.println(MD5Util.MD5Encode(verify));
        //验证签名是否正确
        if (!MD5Util.MD5Encode(verify).equals(sign)) {
            return MyResult.ResultCode("fail", "签名验证失败","-2");
        }
        String sql = "";
        String leftSql = "";
        //根据是否有公司code来拼接sql
        if (StringUtils.isNotBlank(companyCode)) {
            sql = "select org_name,companyName from  " + TableList.ORG + " o " +
                    "left join " + TableList.COMPANY + " c on c.orgId=o.id where org_code ='" + orgCode + "' and " +
                    "companyCode='" + companyCode + "'";
            leftSql = "left join " + TableList.ORG + " o on o.org_code=io.orgCode\n" +
                    "left join " + TableList.COMPANY + " c on c.orgId=o.id";
        } else {
            sql = "select org_name from  " + TableList.ORG + " where org_code ='" + orgCode + "'";
        }
        Map<String, Object> org = findFirstBySql(sql);
        String orgName = (String) org.get("org_name");
        String companyName = BaseUtil.objToStr(org.get("companyName"), "");

        StringBuffer txtStr = new StringBuffer("");
        String path = ParamDef.findFileByName("inOutDir") + orgName + companyName + "进出日志" + startDate + "_" + endDate + ".txt";
        String filename = orgName + companyName + "进出日志" + startDate + "_" + endDate + ".txt";

        int page = 0;
        int size = 10000;
        int totle = size;
        String comlumSql = "select userName,pospCode,scanDate,scanTime,inOrOut ";
        String fromSql = " from " + TableList.IN_OUT + " io " + leftSql + " where orgCode='" + orgCode + "' and scanDate between '" + startDate + "' and '" + endDate + "'";
        //分页获取数据库信息
        String limit = " limit " + page + "," + size;
        Map<String, Object> firstMap = findFirstBySql(comlumSql + fromSql + limit);
        if (firstMap==null||firstMap.isEmpty()){
                return MyResult.ResultCode("success","暂无数据","0");
            }
        File f = new File(path);
        //如果文件存在，则追加内容；如果文件不存在，则创建文件
        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);
        try {
        while (true) {
            List<Map<String, Object>> list = findList(comlumSql, fromSql + limit);
            if (list==null||list.size()==0||list.isEmpty()){
                return MyResult.ResultCode("success","暂无数据","0");
            }
            page = totle;
            totle = (totle / size + 1) * size;
            limit = " limit " + page + "," + size;
            if (list != null && !list.isEmpty()) {
                for (Map<String, Object> map : list) {
                    txtStr.append(map.get("userName")).append("|")
                            .append(map.get("pospCode")).append("|")
                            .append(map.get("scanDate")).append("|")
                            .append(map.get("scanTime")).append("|")
                            .append(map.get("inOrOut"));
                    pw.println(txtStr);
                    txtStr = new StringBuffer("");
                }
            } else {
                break;
            }
        }
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.ResultCode("fail","系统异常","-1");
        }finally {
            pw.flush();
            fw.flush();
            if (pw!=null){
                pw.close();
            }
            if (fw!=null){
                fw.close();
            }
        }
        fileService.sendFile(path, filename, response);
        f.delete();
        return MyResult.ResultCode("success","获取数据成功","100");
    }

    /**
     * 验证传入上位机密码正确
     * @param orgCode
     * @param pospCode
     * @param sign
     * @return
     * @throws Exception
     */
    public boolean check(String orgCode,String pospCode,String sign) throws Exception{

        String sql="select privateKey from "+TableList.POSP+" p" +
                " left join "+TableList.ORG+" o on p.orgId =o.id where org_code='"+orgCode +
                "' and pospCode='"+pospCode+"'";
        Map<String, Object> posp = findFirstBySql(sql);
        try {

            //验证规则orgCode + pospCode+privateKey的md5码是否正确
            if (posp != null) {
                String privateKey = BaseUtil.objToStr(posp.get("privateKey"), null);
                String mySign = MD5Util.MD5(orgCode + pospCode+privateKey);
                logger.info("mySign:"+mySign);
                logger.info("sign:"+sign);
                if (mySign.equals(sign)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
