package com.xdream.goldccm.controller.inAndOut;

import com.xdream.goldccm.controller.base.MyBaseController;
import com.xdream.goldccm.service.inAndOutService.IInAndOutService;
import com.xdream.uaas.model.compose.MyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author chenwf
 * @date 2019/8/12 14:36
 */
@Controller
@RequestMapping("/inAndOut")
public class inAndOutController extends MyBaseController {
    @Autowired
    private IInAndOutService inAndOutService;

    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        try {
            inAndOutService.save(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /** 
     * 下发人脸进出文件
     * @param request	 
     * @return com.goldccm.model.compose.Result 
     * @throws Exception    
     * @author cwf 
     * @date 2019/8/25 16:11
     */
    @RequestMapping("/getInOutTxt")
    @ResponseBody
    public MyResult getInOutTxt(HttpServletRequest request, HttpServletResponse response){

        try {
            Map<String,Object> paramMap = getParamsToMap(request);
            return inAndOutService.getInOutTxt(paramMap, response);
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.unDataResult("fail", "系统异常");
        }
    }

}
