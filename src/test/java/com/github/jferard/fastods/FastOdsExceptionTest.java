package com.github.jferard.fastods;

import org.junit.Assert;
import org.junit.Test;

public class FastOdsExceptionTest {

	@Test
	public final void testUnkownTableName() {
		Exception e = FastOdsException.unkownTableName("tab");
		Assert.assertEquals("Unknown table name [tab]", e.getMessage());
	}

}
