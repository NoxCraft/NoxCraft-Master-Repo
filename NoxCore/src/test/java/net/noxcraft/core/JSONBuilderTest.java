package net.noxcraft.core;

import static org.junit.Assert.*;

import org.bukkit.ChatColor;
import org.json.JSONStringer;
import org.junit.Ignore;
import org.junit.Test;

import com.noxpvp.core.utils.chat.TellRawUtil;
import com.noxpvp.core.utils.chat.TellRawUtil.JSONBuilder.FLAGS;

public class JSONBuilderTest {

	@Test
	public void testChildlessJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123").getJson());
	}
	
	@Test
	public void testEmbeddedFormat(){
		String expected = new JSONStringer().object()
				.key("text").value("")
				.key("extra").array().object()
					.key("text").value("OMG TEST")
					.key("color").value(ChatColor.BLUE)
					.key("italic").value(true)
				.endObject().endArray().endObject().toString();
		String actual = new TellRawUtil.JSONBuilder().addEnclosedText(ChatColor.BLUE, "OMG TEST", FLAGS.ITALIC).getJson();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void bitTest() {
		int expected = 1 | 2 | 4;
		
		int actual = FLAGS.BOLD | FLAGS.UNDERLINE | FLAGS.ITALIC;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testChildlessFormattedJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).key("italic").value(true).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123", FLAGS.ITALIC).getJson());
	}
	
	@Test
	public void testChildlessMultiFormattedJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).key("bold").value(true).key("italic").value(true).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123", FLAGS.BOLD | FLAGS.ITALIC).getJson());
	}
	
	@Test
	public void testEmbeddedTest() {
		String expected = new JSONStringer().object()
				.key("text").value("")
				.key("extra").array()
					.object()
						.key("text").value("Embedded")
						.key("color").value(ChatColor.DARK_RED)
					.endObject()
					.endArray()
				.endObject().toString();
		
		
		String json = new TellRawUtil.JSONBuilder().addEnclosedText(ChatColor.DARK_RED, "Embedded").getJson();
		assertEquals(expected, json);
	}
	
	
	
	@Test
	@Ignore
	public void test() {
		fail("Not yet implemented");
	}

}
