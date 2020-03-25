package com.xdream.goldccm.service.inAndOutService;

import com.xdream.uaas.model.compose.MyResult;
import com.xdream.uaas.service.base.IMyBaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author chenwf
 * @date 2019/8/12 14:36
 */
public interface IInAndOutService extends IMyBaseService {


  void save (HttpServletRequest request,HttpServletResponse response) throws Exception;

  MyResult getInOutTxt(Map<String, Object> paramMap, HttpServletResponse response) throws Exception;
}
