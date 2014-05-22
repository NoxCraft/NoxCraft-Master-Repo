package com.noxpvp.core;

import com.noxpvp.core.utils.TimeUtils;
import com.noxpvp.core.utils.UUIDUtil;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.UUID;

public class UuidTest {

	private final static String newline = System.lineSeparator();

	private static void print(Object... objects) {

		StringBuilder builder = new StringBuilder();
		builder.append("Tester -> ").append(UuidTest.class.getSimpleName()).append(":").append(newline).append('\t');
		builder.append(Thread.currentThread().getStackTrace()[2]);
		boolean first = true;
		for (Object ob : objects)
			if (first) {
				first = false;
				builder.append(ob);
			} else
				builder.append(", ").append(ob);

		System.out.println(builder.toString());
	}

	@Test
	public void testUUIDCompression() throws Exception {
		UUID id = UUID.randomUUID();
		String expected = id.toString().replaceAll("-","");
		String actual = UUIDUtil.compressUUID(id);
		assertEquals(expected, actual);
	}

	@Test
	public void testCompressionSpeed() throws Exception {
		UUID id = UUID.randomUUID();
		String expected = id.toString().replaceAll("-","");
		TimeUtils.StopWatch watch = new TimeUtils.StopWatch();
		watch.setUsingNanos(true);

		watch.start("compression");
		String actual = UUIDUtil.compressUUID(id);
		print(TimeUtils.getReadableNanosTime(watch.stop("compression")));

		assertEquals(expected, actual);
	}

	@Test
	public void testUUIDValidation() throws Exception {
		assertTrue("Not validating a real UUID that was generated!", UUIDUtil.isUUID(UUID.randomUUID()));
	}

	@Test
	public void testInvalidation() throws Exception {
		assertFalse("This was supposed to be invalid!", UUIDUtil.isUUID(true));
		assertFalse("This was supposed to be invalid!", UUIDUtil.isUUID("somethingspecial"));
		assertFalse("This was supposed to be invalid!", UUIDUtil.isUUID(""));
		assertFalse("This was supposed to be invalid!", UUIDUtil.isUUID("a-b-d-c-d"));
	}
}
