package cn.tedu.sp11.filter;


import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.tedu.web.util.JsonResult;
@Component
public class AccessFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String serviceId= (String)ctx.get(FilterConstants.SERVICE_ID_KEY);
		if(serviceId.equals("item-service")) {
			return true;
		}
		return false;
	}

	@Override
	public Object run() throws ZuulException {
	RequestContext ctx = RequestContext.getCurrentContext();
	HttpServletRequest req = ctx.getRequest();
	String token = req.getParameter("token"); //请求中的令牌
	if(token==null) {
		//此设置会阻止请求被路由到后台微服务.
		ctx.setSendZuulResponse(false);
		//向客户端的相应
		ctx.setResponseStatusCode(200);
		ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).toString());
	}
		return null;//暂时没有用
	}

	@Override
	public String filterType() {
		// 过滤器类型
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// 过滤器的顺序号		
		return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
	}

}
