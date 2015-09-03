package io.leopard.security.admin;

import io.leopard.jdbc.Jdbc;
import io.leopard.web.passport.PassportValidate;
import io.leopard.web.xparam.SessUsernameXParam;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class AdminDaoMysqlImpl implements AdminDao {

	@Autowired
	private Jdbc jdbc;

	private PassportValidate passportValidate;

	@Autowired(required = false)
	private SessUsernameXParam sessUsernameXParam;

	@Override
	public String getUsername(HttpServletRequest request, HttpServletResponse response) {
		return (String) sessUsernameXParam.getValue(request, null);
	}

	@Override
	public void forwardLoginUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		passportValidate.showLoginBox(request, response);
	}

	@Override
	public void login(String username, HttpServletRequest request) {
		Admin admin = this.get(username);
		if (admin == null) {
			// logger.info("您[" + username + "]不是管理员.");
			throw new AdminNotFoundException("您" + username + "不是管理员.");
		}
	}

	@Override
	public Admin get(String username) {
		String sql = "SELECT * FROM admin where username=? limit 1;";
		// StatementParameter param = new StatementParameter();
		// param.setString(username);

		return jdbc.query(sql, Admin.class, username);
	}
}
