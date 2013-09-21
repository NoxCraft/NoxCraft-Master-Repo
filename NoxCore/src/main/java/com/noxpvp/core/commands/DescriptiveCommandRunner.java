package com.noxpvp.core.commands;

public interface DescriptiveCommandRunner extends CommandRunner {
	public String[] getDescription();
	public String[] getFlags();
}
