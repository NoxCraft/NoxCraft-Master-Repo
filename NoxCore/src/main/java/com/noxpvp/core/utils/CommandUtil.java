package com.noxpvp.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bergerkiller.bukkit.common.utils.StringUtil;

public class CommandUtil {
	private static Pattern FLAG_PATTERN = Pattern.compile("^-(.+)$");
	public static String[] parseFlags(Map<String, Object> data, String[] args)
	{
		boolean isQuoted = false;
		boolean isFlagged = false;
		String quoteChar = null;
		String flag = null;
		StringBuilder quoteBuilder = new StringBuilder();
		List<String> newData = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {
			if (isQuoted)
			{
				if (args[i].endsWith(quoteChar))
					isQuoted = false;
				quoteBuilder.append(" ").append(args[i]);
				if (!isQuoted){
					String d = quoteBuilder.toString();
					newData.add(d.substring(1, d.length()-1)); //TODO: Double check this actually removes ONLY one char off the end of string and begginning of string.
				}
				else
					continue;
			} else if (!isQuoted && args[i].startsWith("\"")) {
				isQuoted = true;
				quoteChar = "\"";
				quoteBuilder = new StringBuilder();
				quoteBuilder.append(args[i]);
				continue;
			} else if (!isQuoted && args[i].startsWith("'")) {
				isQuoted = true;
				quoteChar = "'";
				quoteBuilder = new StringBuilder();
				quoteBuilder.append(args[i]);
				continue;
			} else {
				newData.add(args[i]);
			}
		}
		args = newData.toArray(new String[newData.size()]);
		newData.clear();
		for (int i = 0; i < args.length; i++) {
			if (isFlagged && !args[i].matches(FLAG_PATTERN.pattern()))
			{
				isFlagged = false;
				data.put(flag, args[i]);
				continue;
			} else if (isFlagged){
				data.put(flag, true);
				flag = args[i];
			} else {
				Matcher m = FLAG_PATTERN.matcher(args[i]);
				if (m.matches())
				{
					flag = m.group(1);
					isFlagged = true;
				}
				else
					newData.add(args[i]);
				continue;

			}
		}
		String argline = StringUtil.combine(" ", newData.toArray(new String[newData.size()]));
		return argline.split(" ");
	}
}
