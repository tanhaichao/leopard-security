package io.leopard.security.admin;

import io.leopard.web.servlet.RegisterHandlerInterceptor;
import io.leopard.web.servlet.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminInterceptor extends RegisterHandlerInterceptor {
	protected Log logger = LogFactory.getLog(this.getClass());

	private static boolean checkAllowIp = true;

	@Autowired
	private AdminDao adminDao;

	/**
	 * 判断所在IP能否登录后台系统.
	 * 
	 * @param request
	 */
	protected void checkIp(HttpServletRequest request) {
		// FIXME ahai 代码被注释掉了
		// String ip = RequestUtil.getProxyIp(request);
		// // System.out.println("ip:" + ip);
		// if (!loginHandler.isAllowAdminIp(ip)) {
		// throw new AdminIpForbiddenRuntimeException(ip);
		// }
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// admin过滤器
		if (!isAdminFolder(request)) {
			return true;
		}

		// 检查用户所在IP
		if (checkAllowIp) {
			checkIp(request);
		}

		String sessUsername = adminDao.getUsername(request, response);
		if (sessUsername == null || sessUsername.length() == 0) {
			String ip = RequestUtil.getProxyIp(request);
			String message = "您[" + ip + "]未登录,uri:" + request.getRequestURI() + " sessUsername:" + sessUsername;
			logger.warn(message);
			adminDao.forwardLoginUrl(request, response);
			return false;
		}
		this.adminDao.login(sessUsername, request);
		return true;
	}

	public static boolean isAdminFolder(HttpServletRequest request) {
		String contextUri = RequestUtil.getRequestContextUri(request);
		return contextUri.startsWith("/admin/");
	}

}
