package com.xdream.goldccm.service.file.impl;

import com.xdream.goldccm.service.file.IFileService;
import com.xdream.goldccm.third.FileConfig;
import com.xdream.goldccm.util.DateUtil;
import com.xdream.goldccm.util.ParamDef;
import com.xdream.kernel.util.ResponseUtil;
import com.xdream.uaas.dao.base.IMyBaseDao;
import com.xdream.uaas.service.base.impl.MyBaseServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;


/**
 * @program: visitor
 * @description: 文件接口实现类
 * @author: cwf
 * @create: 2019-08-27 16:54
 **/
@Service("fileService")
public class IFileServiceImpl extends MyBaseServiceImpl implements IFileService {


    @Autowired
    private IMyBaseDao baseDao;
    @Autowired
    public JdbcTemplate jdbcTemplate;
    private static Logger logger = Logger.getLogger(IFileServiceImpl.class);

    /**
     * 下载txt文件
     *
     * @param request
     * @return java.lang.String
     * @throws Exception
     * @author cwf
     * @date 2019/8/27 10:51
     */
    public String uploadTxt(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile myfile = null;
        Iterator<String> iter = multipartRequest.getFileNames();
        String systemTimeFourteen = DateUtil.getSystemTimeFourteen();
        if (iter.hasNext()) {
            String key = iter.next();
            myfile = multipartRequest.getFile(key);
            if (!myfile.isEmpty()) {
                String originalFilename = myfile.getOriginalFilename();
                if (originalFilename.matches(".+(.txt)$")) {
                    //临时文件下载地址
                    String realPath = ParamDef.findFileByName("inOutDir");
                    File file = new File(realPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File files = new File(realPath, systemTimeFourteen + "_" + originalFilename);
                    // 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
                    FileUtils.copyInputStreamToFile(
                            myfile.getInputStream(), files);
                    System.out.println("路径：" + files.getAbsolutePath() + "上传成功");
                    return files.getAbsolutePath();
                }
            }
        }
        return "-1";
    }

    /**
     * 文件下发
     * update by cwf  2019/8/26 11:34
     */
    @Override
    public void sendFile(String path, String filename, HttpServletResponse response) throws Exception {
        //否则直接使用response.setHeader("content-disposition", "attachment;filename=" + filename);即可
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(path); //获取文件的流
            int len = 0;
            //缓存作用
            byte buf[] = new byte[1024];
            //输出流
            out = response.getOutputStream();
            while ((len = in.read(buf)) > 0) {
                //向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
                out.flush();
            }
        }
    }

    /**
     * 通过txt文件批量上传
     *
     * @param path
     * @param table
     * @param suffix
     * @return
     * @throws Exception
     * @author cwf
     * @date 2019/9/9 14:08
     */

    @Override
    public int batchUpdate(String path, String table, String suffix) throws Exception {
        StringBuffer strRead = new StringBuffer("insert into " + table + " values");
        //读取文本行数
        int count = 0;
        //每次插入数量为5000
        int size = 5000;
        //插入次数
        int inTimes = 1;
        String str = null;
        //批量插入返回值
        int[] ints = null;
        //插入数据库条数
        int inCount = 0;
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(new FileInputStream(path), "UTF-8");
            reader = new BufferedReader(is);

            while (true) {
                str = reader.readLine();
                if (str != null) {
                    strRead.append("(0,'").append(str.replace("|", "','")).append("'," + suffix + "),");
                    count++;
                    //插入数大于5000分批插入
                    //如果总插入条数除以每次插入条数大于等于插入次数，说明总还有数据未插入数据库
                    if (count / size >= inTimes) {
                        inTimes++;
                        ints = baseDao.batchUpdate(strRead.substring(0, strRead.length() - 1));
                        strRead = new StringBuffer("insert into " + table + " values");
                        inCount += ints[0];
                    }
                } else {
                    //如果插入数小于5000的方法
                    if (count < size) {
                        ints = baseDao.batchUpdate(strRead.substring(0, strRead.length() - 1));
                        inCount += ints[0];
                    }
                    break;
                }

            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            //读取文本条数>插入条数时，最后执行一次批量插入
            if (count > inCount) {
                baseDao.batchUpdate(strRead.substring(0, strRead.length() - 1));
            }
            if (is != null) {
                is.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public int uploadMore(MultipartFile[] myfiles, HttpServletRequest request) {

        try {

//            System.out.println(ParamDef.findFileByName("prefix"));
//            String userId = request.getParameter("userId");
            String resource = request.getParameter("resource");
            String suffix = request.getParameter("suffix");
            String prefix = ParamDef.findFileByName("prefix");
//            String image=".JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png";
//            String
            if ("app".equals(resource)) {
                prefix = FileConfig.prefixApp;
                //文档也放在img文件夹中
            } else if ("img".equals(resource)) {
                prefix = FileConfig.prefixImg;
            }
            String path = null;
            int count = 0;
            for (MultipartFile file : myfiles) {
                count++;
                if (file.isEmpty()) {
                    logger.info("文件未上传");
                } else {
                    logger.info("文件长度: " + file.getSize());
                    logger.info("文件类型: " + file.getContentType());
                    logger.info("文件名称: " + file.getName());
                    logger.info("文件原名: " + file.getOriginalFilename());
                    String originalFilename = file.getOriginalFilename();
//                    if (originalFilename.matches(".+("+image+")$")) {
//                        path= prefix+"/img"+ParamDef.findFileByName("suffixImage");
//                        copyFile(path,file,null,originalFilename);
//                    }else {
                    path = prefix + File.separator + suffix;
                    copyFile(path, file, null, originalFilename);
//                    }
                }

            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据code回复页面
     *
     * @param response
     * @param code
     * @throws Exception
     */
    @Override
    public void response(HttpServletResponse response, int code) throws Exception {
        JSONObject jsonObject = new JSONObject();

        switch (code) {
            case -1:
                jsonObject.put("sign", "fail");
                jsonObject.put("desc", "提交失败");
                jsonObject.put("code", code);
                break;
            case 0:
                jsonObject.put("sign", "fail");
                jsonObject.put("desc", "文件未上传");
                jsonObject.put("code", code);
                break;
            default:
                jsonObject.put("sign", "success");
                jsonObject.put("desc", "提交成功");
                jsonObject.put("code", code);
                break;
        }
        String json = jsonObject.toString();
        logger.info("--回复类容为：" + json);
        ResponseUtil.responseJson(response, json);

    }

    @Override
    //文件转储
    public void copyFile(String Path, MultipartFile myfile, String userId, String originalFilename) throws Exception {

        File file = new File(Path);
        logger.info("path " + Path);
        System.out.println(Path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String newFileName = myfile.getOriginalFilename();
        if (userId != null) {
            newFileName = userId + myfile.getOriginalFilename();
        }
        // 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
//
//        int index = originalFilename.lastIndexOf(".");
//
//        String suffix = originalFilename.substring(index);
        //生成文件 文件地址+文件名
        FileUtils.copyInputStreamToFile(myfile.getInputStream(),
                new File(Path, newFileName));
        System.out.println(newFileName);
        System.out.println("上传成功");
    }

//    @Override
//    public void findFileUnqualified() throws Exception {
//        long start = System.currentTimeMillis();
////        List<Map<String, Object>> list = findList("select id,idHandleImgUrl  ", " from " + TableList.USER + " where idHandleImgUrl is not null and idHandleImgUrl<>'' and isAuth='T' and idHandleImgUrl<>'user/headImg/z192.png' ");
////        List<Map<String, Object>> list = findList("select id,idHandleImgUrl  ", " from " + TableList.USER + " where failReason between '55' and '60' ");
//        List<Map<String, Object>> list = findList("select id,idHandleImgUrl ,realName ", " from " + TableList.USER + " where id in(1564,2159,\n" +
//                "        2161,\n" +
//                "        2163,\n" +
//                "        2479,\n" +
//                "        2473,\n" +
//                "        2658,\n" +
//                "        2427,\n" +
//                "        2428,\n" +
//                "        2432,\n" +
//                "        2100,\n" +
//                "        2326,2037,2803,2244,1517,1298,2768)");
//        String pic64_1;
//        RetMsg retMsg;
////        StringBuffer str = new StringBuffer("(");
//        String inPath = null;
//        List maplist = new LinkedList<>();
//        for (Map<String, Object> map : list) {
//            try {
//                String idHandleImgUrl = map.get("idHandleImgUrl").toString();
//                inPath = "http://47.98.205.206/imgserver/" + idHandleImgUrl;
//                byte[] imageFromNetByUrl = FilesUtils.getImageFromNetByUrl(inPath);
//                pic64_1 = Base64.encodeBase64String(imageFromNetByUrl);
//                String before = idHandleImgUrl.substring(0, idHandleImgUrl.lastIndexOf("/"));
//                String after = idHandleImgUrl.substring(idHandleImgUrl.lastIndexOf("/") + 1);
//                File fileFromBytes = FilesUtils.getFileFromBytes(imageFromNetByUrl, ImageConfig.imageSaveDir + before + "/", after);
//
//                retMsg = FaceModuleUtil.buildFaceModel(pic64_1, 1, ImageConfig.imageSaveDir + "照片间距/", idHandleImgUrl.substring(idHandleImgUrl.lastIndexOf("/") + 1));
////                FaceModuleUtil.cutFacePic2Base64(HJFaceModel hjFaceModel, String originPic)
////                if (retMsg.getResult_code() < 0) {
//                maplist.add(new Object[]{retMsg.getResult_code() + 45, map.get("id")});
//                String realName = map.get("realName").toString();
//
//                String newFileName = "id" + map.get("id") + "眼间距" + (retMsg.getResult_code() + 45) + "_name" + realName + "_" + idHandleImgUrl.substring(idHandleImgUrl.lastIndexOf("/") + 1);
//
//                FilesUtils.nioCopy(ImageConfig.imageSaveDir + idHandleImgUrl, ImageConfig.imageSaveDir + "照片间距/" + newFileName);
////                    str.append(map.get("id")).append(",");
////                }
//            } catch (Exception e) {
//                logger.error("图片地址错误：{}", e);
//                logger.info(inPath);
//            }
//        }
//        int count = 0;
////        if(list.size()>0){
////            str.deleteCharAt(str.length() - 1);
////            str.append(")");
////            String s = String.valueOf(str);
////            System.out.println(s);
////        }
//        if (maplist.size() > 1) {
////            str.deleteCharAt(str.length() - 1);
////            str.append(")");
////            String s = String.valueOf(str);
////            System.out.println(s);
//            String sql = "update " + TableList.USER + " set failReason=? where id=?";
//
//            int[] ints = jdbcTemplate.batchUpdate(sql, maplist);
//            count = ints[0];
////            int update = deleteOrUpdate("update " + TableList.USER + " set failReason='眼间距小于60',isAuth='F' where id in" + s);
////					logger.debug("pic64_1:\n" + pic64_1);}
//            //创建人脸模型
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("耗时" + (end - start));
//        logger.info(count);
//    }

//    @Override
//    public void test() {
//        String pic64_1;
//        RetMsg retMsg = null;
////        StringBuffer str = new StringBuffer("(");
//        String inPath = null;
//        Path path = Paths.get(ImageConfig.imageSaveDir + "下发失败图片");
//        //第二个参数必须用"*"开头，第二个参数是非必输的
//        try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
//            for (Path entity : entries) {
//                System.out.println(entity.getFileName());
//                inPath = ImageConfig.imageSaveDir + "下发失败图片/" + entity.getFileName();
//                pic64_1 = FilesUtils.ImageToBase64ByLocal(inPath);
//                retMsg = FaceModuleUtil.buildFaceModel(pic64_1, 1);
//                if ((retMsg.getResult_code() + 60) >= 54 && (retMsg.getResult_code() + 60) < 60) {
//                    String s = entity.getFileName() + "间距" + (retMsg.getResult_code() + 60) + ".jpg";
//
//                    FilesUtils.nioCopy(inPath, ImageConfig.imageSaveDir + "照片间距1/" + s);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    }

//    /**
//     * 下载文件
//     * @param request
//     * @return java.lang.String
//     * @throws Exception
//     * @author cwf
//     * @date 2019/8/27 10:51
//     */
//    public String uploadDocument(HttpServletRequest request) throws Exception {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile myfile = null;
//        Iterator<String> iter = multipartRequest.getFileNames();
//        String systemTimeFourteen = DateUtil.getSystemTimeFourteen();
//        if (iter.hasNext()) {
//            String key = iter.next();
//            myfile = multipartRequest.getFile(key);
//            if (!myfile.isEmpty()) {
//                String originalFilename = myfile.getOriginalFilename();
//                if (originalFilename.matches(".+(.txt|)$")) {
//                    //文件下载地址
//                    String realPath = ParamDef.findFileByName("prefix")+ParamDef.findFileByName("suffixDocument");
//                    File file = new File(realPath);
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    File files = new File(realPath, systemTimeFourteen+"_"+originalFilename);
//                    // 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
//                    FileUtils.copyInputStreamToFile(
//                            myfile.getInputStream(), files);
//                    System.out.println("路径：" + files.getAbsolutePath());
//                    System.out.println("上传成功");
//                    return files.getAbsolutePath();
//                }
//            }
//        }
//        return "-1";
//    }


    public static void main(String[] args) {

//       try {
//           long start = System.currentTimeMillis();
////            String str="ifc|swjifc01|2019-08-22|17:32:24|in|OUT1|FACE|192.168.2.3|staff|吴桂民|D11515B3268BBB924DBD516A305EBA75377D61B01A7CE44C";
//           StringBuffer strRead = new StringBuffer("insert into " + TableList.IN_OUT + " values");
//           int count = 0;
//           int size = 5000;
//           int tatle = 1;
//           String path ="D:\\测试/test.txt";
//
//           InputStream is = new FileInputStream(path);
//           BufferedReader reader = new BufferedReader(
//                   new InputStreamReader(is));
//           String str = null;
//           int[] ints=null;
//           while (true) {
//               str = reader.readLine();
//               if (str != null) {
//
//                   if (count / size >= tatle) {
//
////                       String str1=strRead.substring(0,strRead.length()-1);
//                       System.out.println(count);
//                       tatle++;
//
////                       strRead = new StringBuffer("insert into " + TableList.IN_OUT + " values");
//                   }
////                   strRead.append("(0,'").append(str.replace("|", "','")).append("',null,null,null),");
//                  count++;
////                   System.out.println(strRead);
//               } else {
//                   break;
//               }
//           }
//           is.close();
//           long end = System.currentTimeMillis();
//           System.out.println(end - start + "ms");
//       }catch (Exception e){
//           e.printStackTrace();
//       }

    }
}