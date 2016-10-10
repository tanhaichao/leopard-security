package io.leopard.security.admin.version2;

import java.util.List;

public class AdminVO {
	/**
	 * 管理员ID
	 */
	private long adminId;
	/**
	 * 角色
	 */
	private List<String> roleList;
	/**
	 * 管理员姓名
	 */
	private String name;

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
