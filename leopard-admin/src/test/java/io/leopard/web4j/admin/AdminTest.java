package io.leopard.web4j.admin;

import io.leopard.test.mock.BeanAssert;

import org.junit.Test;

public class AdminTest {

	@Test
	public void Admin() {
		BeanAssert.assertModel(Admin.class);
	}

}