package com.tennetcn.free.security.intceptor.valid;

import cn.hutool.json.JSONUtil;
import com.tennetcn.free.core.cache.ICached;
import com.tennetcn.free.core.utils.CommonApplicationContextUtil;
import com.tennetcn.free.security.annotation.ApiAuthPassport;
import com.tennetcn.free.security.core.JwtHelper;
import com.tennetcn.free.security.handle.ILoginModelIntceptor;
import com.tennetcn.free.security.message.LoginModel;
import com.tennetcn.free.security.webapi.AuthorityApi;
import com.tennetcn.free.web.message.WebResponseStatus;
import com.tennetcn.free.web.webapi.BaseResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class ApiAuthPassportValid {

	@Autowired
	private ICached cached;
	
	public boolean valid(HttpServletRequest request,HttpServletResponse response, Object handler) throws IOException, Exception{
		if(checkAppAuthorizeRule(handler)){
			BaseResponse rm=new BaseResponse(WebResponseStatus.AUTHORIZE_ERROR);

			boolean authorizeResult=checkAuthorizeJwt(request);

			
			if(authorizeResult){
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				return true;
			}
			
			response(response,rm);
			return false;
		}
		return true;
	}
	
	private void response(HttpServletResponse response,BaseResponse rm) throws IOException{
		response.setContentType("application/json; charset=utf-8");
		OutputStream os =  response.getOutputStream();
		String mapJsonStr=JSONUtil.toJsonStr(rm);
		os.write(mapJsonStr.getBytes());
		os.flush();
		os.close();
	}
	
	private boolean checkAppAuthorizeRule(Object handler){
		ApiAuthPassport authorizePassport = ((HandlerMethod) handler).getMethodAnnotation(ApiAuthPassport.class);
		ApiAuthPassport authorizePassportBean = ((HandlerMethod) handler).getBeanType().getAnnotation(ApiAuthPassport.class);
		
		//return false 则不验证 true则要验证
		if(authorizePassportBean!=null){  //类上面有
			if(authorizePassport==null){  //方法上没有  不验证
				return false;
			}else if(authorizePassport.validate()){   //方法上有，切标记validate 为true 则验证
				return true;
			}
		}else if(authorizePassportBean==null){  //类上面没有
			if(authorizePassport==null||authorizePassport.validate()){  //方法上没有或是有标记validate为true 则验证
				return true;
			}else if(!authorizePassport.validate()){  //方法上面有 且标记validate 为false 则不验证
				return false;
			}
		}
		return true;
	}
	

	
	private boolean checkAuthorizeJwt(HttpServletRequest request) throws Exception{
		String token=request.getHeader("Authorization");
		if(StringUtils.isEmpty(token)){
			token=request.getParameter("signCode");

			if(StringUtils.isEmpty(token)){
			     return false;
			}
		}
		
		Claims claims = JwtHelper.instance().parseJWT(token);
		if(claims == null || JwtHelper.instance().isTokenExpired(claims.getExpiration())){
			cached.remove(token);
			return false;
		}
		
		LoginModel loginModel=(LoginModel)cached.get(token);
		if(loginModel==null) {
			// 如果cache为空，则由loginModelIntceptor进行注册一次
			ILoginModelIntceptor loginModelIntceptor = CommonApplicationContextUtil.getCurrentContext().getBean(ILoginModelIntceptor.class);
			if(loginModelIntceptor==null){
				return false;
			}

			loginModel = loginModelIntceptor.registerLoginModel(token,claims);
			if(loginModel==null){
				return false;
			}
		}
		request.setAttribute(AuthorityApi.LOGIN_KEY, loginModel);
		
		return true;
	}
}