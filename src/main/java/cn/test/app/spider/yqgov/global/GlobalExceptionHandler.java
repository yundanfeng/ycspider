package cn.test.app.spider.yqgov.global;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * @remark
 * @author luzh 
 * @createTime 2017年6月27日 下午5:06:10
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ErrorInfo jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        ErrorInfo r = new ErrorInfo();
        r.setCode(Globals.CODE_500);
        r.setMsg(e.getMessage());
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
}
