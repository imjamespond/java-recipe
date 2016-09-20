package org.copycat.test;

import org.copycat.framework.BeanMapper;
import org.copycat.framework.CollectionMap;
import org.junit.Test;

public class BeanMapperTest {

	@Test
	public void test() {
		CollectionMap map = BeanMapper.convertCollection(User.class);
		System.out.println(map);
	}
}
