package com.trading.instrument.data.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConcurrrentDateComparatorTest {

	@Test
	public void compareTwoDates()
	{
		assertTrue(new ConcurrrentDateComparator().isDateAfter("15-03-2018", "14-03-2018"));
		assertFalse(new ConcurrrentDateComparator().isDateAfter("13-03-2018", "14-03-2018"));
	}
}
