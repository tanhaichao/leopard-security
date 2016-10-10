package io.leopard.security.admin.version2;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.leopard.burrow.lang.LeopardValidUtil;
import io.leopard.burrow.util.StringUtil;
import io.leopard.core.exception.forbidden.PasswordWrongException;
import io.leopard.core.exception.invalid.UsernameInvalidException;
import io.leopard.core.exception.other.AdminDisabledException;
import io.leopard.data.kit.password.LoginInfo;
import io.leopard.data.kit.password.PassportTokenUtil;
import io.leopard.data.kit.password.PasswordUtil;
import io.leopard.data.kit.password.PasswordVerifier;
import io.leopard.data.kit.password.PasswordVerifierImpl;
import io.leopard.json.Json;

@Service
public class AdminBizImpl implements AdminBiz {

	@Autowired
	private AdminService adminService;

	@Resource
	private AdminApi adminApi;

	private PasswordVerifier passwordVerifier = new PasswordVerifierImpl();

	@Override
	public boolean login(long adminId, String password) throws AdminNotFoundException, PasswordWrongException, AdminDisabledException {
		if (adminId <= 0) {
			throw new AdminIdInvalidException(adminId);
		}
		AdminVO admin = adminApi.get(adminId);
		if (admin == null) {
			throw new AdminNotFoundException(adminId);
		}
		if (admin.isDisabled()) {
			throw new AdminDisabledException(admin.getUsername());
		}

		String salt = admin.getSalt();
		String dbPassword = admin.getPassword();

		boolean correctPassword = passwordVerifier.verify(Long.toString(adminId), password, salt, dbPassword);
		if (!correctPassword) {
			throw new PasswordWrongException("密码[" + adminId + "]不正确.");
		}
		return correctPassword;
	}

	@Override
	public LoginInfo login(String username, String password) throws AdminNotFoundException, PasswordWrongException {
		if (!LeopardValidUtil.isValidUsername(username)) {
			throw new UsernameInvalidException(username);
		}
		AdminVO admin = adminApi.getByUsername(username);
		if (admin == null) {
			throw new AdminNotFoundException(0);
		}
		String salt = admin.getSalt();
		String dbPassword = admin.getPassword();

		boolean correctPassword = passwordVerifier.verify(username, password, salt, dbPassword);
		if (!correctPassword) {
			throw new PasswordWrongException("密码[" + username + "]不正确.");
		}

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setUid(admin.getAdminId());
		loginInfo.setAcccount(admin.getUsername());
		loginInfo.setToken(PassportTokenUtil.makeToken(admin.getPassword()));
		loginInfo.setUser(admin);
		return loginInfo;
	}

	@Override
	public String getName(long adminId) throws AdminNotFoundException {
		AdminVO admin = this.adminApi.get(adminId);
		if (admin == null) {
			throw new AdminNotFoundException(adminId);
		}
		return admin.getName();
	}

	@Override
	public boolean add(String username, String password, String name) {
		String salt = StringUtil.uuid().substring(0, 7);

		String encryptedPassword = PasswordUtil.encryptPassword(password, salt);

		Admin admin = new Admin();
		admin.setAdminId(System.currentTimeMillis());
		admin.setUsername(username);
		admin.setPassword(password);
		admin.setName(name);
		admin.setSalt(salt);
		admin.setPassword(encryptedPassword);
		return this.adminService.add(admin);
	}

	public static <T> T convert2(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}
		String json = Json.toJson(obj);
		return Json.toObject(json, clazz, true);
	}

	@Override
	public AdminVO getByUsername(String username) {
		if (this.adminApi == null) {
			Admin admin = adminService.getByUsername(username);
			return convert2(admin, AdminVO.class);
		}
		return this.adminApi.getByUsername(username);
	}

	@Override
	public AdminVO get(long adminId) {
		if (this.adminApi == null) {
			Admin admin = adminService.get(adminId);
			return convert2(admin, AdminVO.class);
		}
		return this.adminApi.get(adminId);
	}

	@Override
	public boolean isTopdomainCookie() {
		if (this.adminApi == null) {
			return false;
		}
		return adminApi.isTopdomainCookie();
	}

}
