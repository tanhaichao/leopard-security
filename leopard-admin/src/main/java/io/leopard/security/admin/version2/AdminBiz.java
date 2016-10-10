package io.leopard.security.admin.version2;

import io.leopard.core.exception.forbidden.PasswordWrongException;
import io.leopard.core.exception.other.AdminDisabledException;
import io.leopard.data.kit.password.LoginInfo;

/**
 * 管理员
 * 
 * @author 谭海潮
 *
 */
public interface AdminBiz {

	boolean login(long adminId, String password) throws AdminNotFoundException, PasswordWrongException, AdminDisabledException;

	LoginInfo login(String username, String password) throws AdminNotFoundException, PasswordWrongException;

	boolean add(String username, String password, String name);

	String getName(long adminId) throws AdminNotFoundException;

	////
	AdminVO getByUsername(String username);

	AdminVO get(long adminId);

	/**
	 * 是否向顶级域名写cookie.
	 * 
	 * @return
	 */
	boolean isTopdomainCookie();

}
