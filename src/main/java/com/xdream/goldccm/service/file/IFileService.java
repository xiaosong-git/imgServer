package com.xdream.goldccm.service.file;

import com.xdream.uaas.service.base.IMyBaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: visitor
 * @description: 处理上传下载文件，临时文件等等
 * @author: cwf
 * @create: 2019-08-27 16:48
 **/
public interface IFileService extends IMyBaseService {
    /**
     * 下载txt文件
     *
     * @param request
     * @return java.lang.String
     * @throws Exception
     * @author cwf
     * @date 2019/8/27 10:51
     */
    String uploadTxt(HttpServletRequest request) throws Exception;

    /**
     * 文件下发
     * update by cwf  2019/8/26 11:34
     */
    void sendFile(String path, String filename, HttpServletResponse response) throws Exception;

    /**
     * 通过txt文件批量上传数据库
     * update by cwf  2019/8/27 15:13 cause
     */
    int batchUpdate(String path, String table, String suffix) throws Exception;

    /**
     * 上传文件
     *
     * @param myfiles
     * @param request
     * @return
     * @throws Exception
     */
    int uploadMore(MultipartFile[] myfiles, HttpServletRequest request) throws Exception;

    /**
     * 回复封装
     *
     * @param response
     * @param code
     * @throws Exception
     */
    void response(HttpServletResponse response, int code) throws Exception;

    /**
     *
     * @param Path
     * @param myfile
     * @param userId
     * @param originalFilename
     * @throws Exception
     */
    void copyFile(String Path, MultipartFile myfile, String userId, String originalFilename) throws Exception;

    void findFileUnqualified() throws Exception;

    void test();
}