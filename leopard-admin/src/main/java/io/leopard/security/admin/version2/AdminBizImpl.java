package io.leopard.security.admin.version2;

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

@Service
public class AdminBizImpl implements AdminBiz {

	@Autowired
	private AdminService adminService;

	private PasswordVerifier passwordVerifier = new PasswordVerifierImpl();

	@Override
	public boolean login(long adminId, String password) throws AdminNotFoundException, PasswordWrongException, AdminDisabledException {
		if (adminId <= 0) {
			throw new AdminIdInvalidException(adminId);
		}

		Admin admin = adminService.get(adminId);
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
		Admin admin = adminService.getByUsername(username);
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
		Admin admin = this.adminService.get(adminId);
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

}
