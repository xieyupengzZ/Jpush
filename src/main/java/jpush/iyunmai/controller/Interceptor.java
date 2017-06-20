
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Interceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("进入拦截器");
		/*user loginUser = (user)request.getSession().getAttribute("usersession");
		if(loginUser == null){
			System.out.println(">>>>>>>loginUser==null");
			//如果是ajax请求响应头会有，x-requested-with；  
			if(request.getHeader("x-requested-with") != null&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				response.setHeader("sessionstatus", "timeout");//在响应头设置session状态 在js中判断响应状态
		        return false; 
			}
			else{
				request.getRequestDispatcher("/timeOut.html").forward(request, response);
				return false;
			}
		}
		else{
			System.out.println(">>>>>>>loginUser ："+loginUser.getUsername());
			return super.preHandle(request, response, handler);
		}*/
		return super.preHandle(request, response, handler);
	}
}
