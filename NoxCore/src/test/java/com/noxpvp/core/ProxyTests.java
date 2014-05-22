package com.noxpvp.core;

import com.bergerkiller.bukkit.common.proxies.Proxy;
import com.dsh105.holoapi.util.ConsoleLogger;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import org.junit.Before;
import org.junit.Test;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.logging.*;

import static org.junit.Assert.*;

public class ProxyTests {
	private static String newline = System.lineSeparator();

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

	private static boolean checkClass(Class<? extends Proxy<?>> proxy) {
		try {
			boolean succ = true;
			boolean loggedHeader = false;
			for (Method method : proxy.getDeclaredMethods()) {
				if (method.getDeclaringClass() != proxy) {
					succ = false;
					if (!loggedHeader) {
						loggedHeader = true;
						print("[Proxy] Some method(s) are not overrided in '" + proxy.getName() + "':");
					}
					print("    - '" + method.toGenericString());
				}
			}
			return succ;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	@Test
	public void checkNoxPlayerAdapter() throws Exception {
		assertTrue(checkClass(BaseNoxPlayerAdapter.class));
	}

}
