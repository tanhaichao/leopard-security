package io.leopard.security.admin;

import java.util.Date;

/**
 * 
 * 
 * @author 阿海
 */
public class Admin {

	private String username;

	private String name;
	private String type;

	private Date posttime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPosttime() {
		return posttime;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
