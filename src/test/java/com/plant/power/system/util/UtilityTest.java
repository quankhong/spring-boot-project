package com.plant.power.system.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.plant.power.system.util.Utility;

class UtilityTest {

	@Test
	void testIsNull() {
		String nullStr = null;

		assertEquals(Utility.isNullOrBlank(nullStr), true);
	}

	@Test
	void testIsEmpty() {
		String emptyStr = "";

		assertEquals(Utility.isNullOrBlank(emptyStr), true);
	}

	@Test
	void testIsBlank() {
		String blankStr = " 	";

		assertEquals(Utility.isNullOrBlank(blankStr), true);
	}

	@Test
	void testIsNotBlank() {
		String str = "abc";

		assertEquals(Utility.isNullOrBlank(str), false);
	}

}
