package life.majiang.community.advice;

import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/*
* 这个在调试的时候发现有的异常断点根本不停，说明这个Controller Advice只能处理部分异常
* 其余的异常在ErrorController里处理
*
* */
@ControllerAdvice
public class CustomizeExceptionHandler {
    //这里能回答 这个类能捕获什么样的异常 所有Exception.class子类的异常 Exception又继承了throwble
    @ExceptionHandler(Exception.class)
    //HttpServletRequest 存了所有请求头的信息
    @ResponseBody
    Object handle(Throwable e,  Model model,HttpServletRequest request) {
        String contentType=request.getContentType();
        //12.28疑问 是json格式的就不存在问题吗
        if("application/json".equals(contentType)){
            //处理json格式
            if(e instanceof CustomizeException){
                //ResualtDTO能把异常信息 封装到自己的对象里 供@Responsbody格式化并返回
                return ResultDTO.errorOf((CustomizeException) e);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

        }else
        {
            //处理非json格式
             if(e instanceof CustomizeException){
                        model.addAttribute("message",e.getMessage());
                    }else {
                 //本来写的未知错误 但是不利与调试
                 // 现在是输出 e的原生错误信息到页面
                        model.addAttribute("message",e.getMessage());
                    }
            return new ModelAndView("error");
        }

    }

}
