package io.leopard.security.admin;

import javax.servlet.http.HttpServletRequest;

import io.leopard.web.passport.PassportChecker;
import io.leopard.web.servlet.RequestUtil;

public class PassportCheckerAdminImpl implements PassportChecker {

	@Override
	public boolean isNeedCheckLogin(HttpServletRequest request, Object handler) {
		new Exception("isNeedCheckLogin").printStackTrace();
		if (isAdminFolder(request)) {
			return true;
		}
		return false;
	}

	public static boolean isAdminFolder(HttpServletRequest request) {
		String contextUri = RequestUtil.getRequestContextUri(request);
		return contextUri.startsWith("/admin/");
	}
}
