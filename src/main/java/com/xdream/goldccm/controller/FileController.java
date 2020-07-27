package com.xdream.goldccm.controller;

import com.xdream.goldccm.service.file.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: goldccm-imgServer
 * @description: 文件管理
 * @author: cwf
 * @create: 2019-08-29 09:18
 **/
@Controller
@RequestMapping(value = "/goldccm/file")
public class FileController {

    @Autowired
    private IFileService fileService;

    /**
     * 文件上传（多个）
     * 未完待续
     * @param file
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/uploadMore")
    public void uploadMore(@RequestParam MultipartFile[] file,
                           HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//        System.out.println("--------文件服务器");
            //登入验证+文件分类 code=0 成功 1失败
          int code=fileService.uploadMore(file, request);
          fileService.response(response,code);
    }

    @RequestMapping(value = "/findFileUnqualified")
    public void findFileUnqualified(HttpServletResponse response)
            throws Exception {
//        System.out.println("--------文件服务器");
        fileService.findFileUnqualified();
        //登入验证+文件分类 code=0 成功 1失败
        fileService.response(response,1);
    }
    @RequestMapping(value = "/test")
    public void test(HttpServletResponse response)
            throws Exception {
//        System.out.println("--------文件服务器");
        fileService.test();
        //登入验证+文件分类 code=0 成功 1失败
        fileService.response(response,1);
    }
}
