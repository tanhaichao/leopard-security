package io.leopard.security.admin.version2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.leopard.json.Json;

@Service
public class AdminApiImpl implements AdminApi {

	@Autowired
	private AdminService adminService;

	@Override
	public AdminVO getByUsername(String username) {
		Admin admin = adminService.getByUsername(username);
		return convert2(admin, AdminVO.class);
	}

	public static <T> T convert2(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}
		String json = Json.toJson(obj);
		return Json.toObject(json, clazz, true);
	}

	@Override
	public boolean isTopdomainCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AdminVO get(long adminId) {
		// TODO Auto-generated method stub
		return null;
	}

}
