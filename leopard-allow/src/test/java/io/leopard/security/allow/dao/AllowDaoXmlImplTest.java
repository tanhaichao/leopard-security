package io.leopard.security.allow.dao;

import io.leopard.security.allow.dao.AllowDaoXmlImpl;

import org.junit.Test;

public class AllowDaoXmlImplTest {

	private AllowDaoXmlImpl allowDaoXmlImpl = new AllowDaoXmlImpl() {
		@Override
		protected java.io.InputStream read() throws java.io.IOException {
			return AllowDaoXmlImplTest.class.getResourceAsStream("allow.xml");
		};
	};

	
	@Test
	public void load() {
		allowDaoXmlImpl.load();
	}

}