package cn.study.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginFilter extends ZuulFilter {


    /**
     * 过滤器的类型：pre   route   post    error
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的优先级别，值越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     * 是否执行run方法
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //初始化context上下文对象，不是servlet，spring上下文
        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();

        //获取参数
        String token = request.getParameter("token");

        if(StringUtils.isBlank(token)){
            //拦截，不转发请求
            context.setSendZuulResponse(false);
            //提示 401身份未认证
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            //设置响应提示
            context.setResponseBody("request error!");
        }

        //返回值null,代表过滤器什么都不做
        return null;
    }

}