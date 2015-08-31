package io.leopard.web4j.admin;

import org.junit.Assert;
import org.junit.Test;

public class AdminAllowIpTest {

	@Test
	public void isIpBlock() {
		AdminAllowIp allowIp = new AdminAllowIp();

		Assert.assertTrue(allowIp.isIpBlock("172.17"));
		Assert.assertTrue(allowIp.isIpBlock("172.17.1"));
		Assert.assertFalse(allowIp.isIpBlock("172.17.1.1"));
	}

}