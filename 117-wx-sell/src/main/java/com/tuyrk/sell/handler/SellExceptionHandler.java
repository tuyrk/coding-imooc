package com.tuyrk.sell.handler;

import com.tuyrk.sell.config.ProjectUrlConfig;
import com.tuyrk.sell.exception.SellException;
import com.tuyrk.sell.exception.SellerAuthorizeException;
import com.tuyrk.sell.utils.ResultVOUtil;
import com.tuyrk.sell.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/17 15:44 星期四
 * Description:
 */
@ControllerAdvice
public class SellExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    //http://tyk.nat300.top/sell/wechat/qrAuthorize?returnUrl=http://tyk.nat300.top/sell/seller/login
    @ExceptionHandler(SellerAuthorizeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenAuthorize()
                        .concat("/sell/wechat/qrAuthorize")
                        .concat("?returnUrl=")
                        .concat(projectUrlConfig.getSell())
                        .concat("/sell/seller/login")));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = SellException.class)
    public ResultVO handlerSellerException(SellException e) {
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
