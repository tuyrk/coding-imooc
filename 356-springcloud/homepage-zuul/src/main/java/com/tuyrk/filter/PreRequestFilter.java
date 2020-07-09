package com.tuyrk.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * 自定义网关过滤器
 * 存储客户算发起请求的时间戳
 *
 * @author tuyrk
 */
@Component
public class PreRequestFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 过滤器的类型。发起请求前
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器优先级。数值越小，级别越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 是否启用当前过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 用于在过滤器之间传递消息（路由信息、错误、Request、Response）
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());
        return null;
    }
}
